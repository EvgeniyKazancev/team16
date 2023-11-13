#include "tgparser.h"
#include "download_file.h"

#include <string>
#include <iostream>
#include <filesystem>
#include <sstream>

#include <mysqlx/xdevapi.h>

namespace fs = std::filesystem;

TgParser::TgParser(const Lib::dataSource &src, const std::string &working_dir, volatile bool &terminate_signal) :
Parser(src, working_dir, terminate_signal) {
	std::stringstream ss;
	ss << "https://t.me/s/" << url_ << '/';
	url_ = ss.str();
}

void TgParser::parse(mysqlx::Session &db_session) {
	if (terminate_signal_caught_) {
		std::cerr << "Terminate signal caught. Exiting..." << std::endl;
		return;
	}

	std::cout << "Parsing " << url_ << std::endl;
	std::string filename { working_dir_ + "/tg-channel-dump.html" };
	DownloadFile df;
	df.download(url_, filename);
	if (!df.success()) {
		std::cerr << "HTTP code: " << df.getHttpCode() << std::endl;
		std::cerr << "Error: " << df.getError() << std::endl;

		return;
	}
	if (df.getContentType() != "text/html") {
		std::cout << "Content-Type: " << df.getContentType() << ". Skipping url..." << std::endl;
		return;
	}
	
	html_ =  htmlReadFile(filename.c_str(), df.getCharset().c_str(), HTML_PARSE_NOBLANKS | HTML_PARSE_NOERROR | HTML_PARSE_NOWARNING | HTML_PARSE_NONET);
	if (html_ == nullptr) {
		fs::remove(filename);
		throw std::runtime_error{ "HTML parse error" };
	}
	xmlNode *root_element{ xmlDocGetRootElement(html_) };
	if (root_element == nullptr) {
		xmlFreeDoc(html_);
		throw std::runtime_error{ "document is empty" };
	}
	fs::remove(filename);
	traverseTree(root_element);
	fillDatabase(db_session);

	xmlFreeDoc(html_);
}

void TgParser::fillDatabase(mysqlx::Session &db_session) const {
	for (const auto &post: posts_) {
		std::stringstream ss;
		long new_id{ 0 };
		
		ss << "DELETE FROM `publications` WHERE `url` = '" << post.link << "' AND `created` + interval 60 minute < CURRENT_TIMESTAMP";
		db_session.sql(ss.str()).execute();
		ss.str(std::string{});
		ss << "SELECT `id` FROM `publications` WHERE `url` = '" << post.link << "'";
		auto result = db_session.sql(ss.str()).execute();
		auto row = result.fetchOne();
		if (row) {
			std::cout << "\nPage " << std::quoted(post.link) << " has been already parsed. Skipping..." << std::endl;
			continue;
		}

		result = db_session.sql("SELECT max(`id`) FROM `publications`").execute();
		row = result.fetchOne();
		if (!row[0].isNull()) {
			new_id = row[0].get<long>() + 1;
		}
		db_session.startTransaction();
		ss.str(std::string{});
		try {
			ss.str(std::string{});
			ss << "INSERT INTO `publications` (`id`, `source_id`, `url`) VALUES (" << new_id << ", " << source_id_ << ", '" << post.link << "');";
			db_session.sql(ss.str()).execute();
			ss.str(std::string{});
			ss << "INSERT INTO `publications_text` (`publication_id`, `text`) VALUES (" << new_id << ", '" << post.text << "');";
			db_session.sql(ss.str()).execute();
			for (const auto &image: post.images) {
				ss.str(std::string{});
				ss << "INSERT INTO `publications_data` (`publication_id`, `property`, `content`) VALUES (" << new_id << ", 'image', '" << image << "');";
				db_session.sql(ss.str()).execute();
			}
			db_session.commit();
		}
		catch (const mysqlx::Error &e) {
			db_session.rollback();
			std::cerr << "Error while processing MySQL query: " << e << std::endl;
		}
	}
}

void TgParser::traverseTree(xmlNode *node) {
	for (auto current_node = node; current_node != nullptr; current_node = current_node->next) {
		std::string node_name;
		if (current_node->name != nullptr) {
			node_name = std::string{ reinterpret_cast<const char *>(current_node->name) };
		}
		if (node_name == "div") {
			auto classes = getClasses(current_node);
			if (classes.contains("tgme_widget_message")) {
				TgPost post_data;
				extractDataFromNode(current_node, post_data);
				posts_.push_back(post_data);
				continue;
			}
		}
		traverseTree(current_node->children);
	}	
	
}
	
void TgParser::extractDataFromNode(htmlNodePtr node, TgPost &post_data) const {
	for (auto current_node = node; current_node != nullptr; current_node = current_node->next) {
		std::string node_name;
		if (current_node->name != nullptr) {
			node_name = std::string{ reinterpret_cast<const char *>(current_node->name) };
		}
		auto classes = getClasses(current_node);
		if (node_name == "a") {
			if (classes.contains("tgme_widget_message_date")) {
				post_data.link = getAttribute(current_node, "href");
			}
			else if (classes.contains("tgme_widget_message_photo_wrap")) {
				auto bg_image = getBackgroundImage(current_node);
				post_data.images.push_back(bg_image);
			}
		}
		else if (node_name == "div") {
			if (classes.contains("tgme_widget_message_text")) {
				auto text = extractTextFromNode(html_, current_node);
				post_data.text = text;
			}
		}
		extractDataFromNode(current_node->children, post_data);
	}	
}

std::string TgParser::getBackgroundImage(htmlNodePtr node) const {
	std::string result{ getAttribute(node, "style") };
	std::erase_if(result, [](char c){
		return std::isspace(c);
	});
	auto pos = result.find("background-image:");
	if (pos == std::string::npos) {
		return std::string{};
	}
	auto length = strlen("background-image:");
	result = result.substr(pos + length, result.length() - (pos + length));
	Lib::removeFromString(result, "url");
	std::erase_if(result, [](char c){
		return c == '\'' || c == ';' || c == '(' || c == ')'; 
	});

	return result;
}

std::map<std::string, std::string> TgParser::getCss(htmlNodePtr node) const {
	std::map<std::string, std::string> result;
	std::string style{ getAttribute(node, "style") };
	if (style.empty()) {
		return result;
	}
	
	auto styles{ Lib::split(style, ";") };
	for (const auto &element: styles) {
		auto pair = Lib::split(element, ":");
		if (pair.size() != 2) {
			continue;
		}
		Lib::trim(pair[0]);
		Lib::trim(pair[1]);
		if (!result.contains(pair[0])) {
			result.insert({
				pair[0],
				pair[1]
			});
		}
	}

	return result;
}

std::set<std::string> TgParser::getClasses(htmlNodePtr node) const {
	for (xmlAttr *attribute = node->properties; attribute != nullptr; attribute = attribute->next) {
		auto attribute_name{ reinterpret_cast<const char *>(attribute->name) };
		auto attribute_value{ reinterpret_cast<const char *>(xmlNodeListGetString(html_, attribute->children, 1)) };
		if (strcmp(attribute_name, "class") == 0) {
			return Lib::splitToSet(std::string{ attribute_value }, " ");
		}
	}
	
	return std::set<std::string>{};
}

std::string TgParser::getAttribute(htmlNodePtr node, const std::string &attribute_for_search) const {
	for (xmlAttr *attribute = node->properties; attribute != nullptr; attribute = attribute->next) {
		auto attribute_name{ reinterpret_cast<const char *>(attribute->name) };
		auto attribute_value{ reinterpret_cast<const char *>(xmlNodeListGetString(html_, attribute->children, 1)) };
		if (strcmp(attribute_name, attribute_for_search.c_str()) == 0) {
			return std::string{ attribute_value };
		}
	}
	
	return std::string{};
}

TgParser::~TgParser() {
	xmlCleanupParser();
}
