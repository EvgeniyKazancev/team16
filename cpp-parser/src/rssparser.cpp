#include "rssparser.h"
#include "download_file.h"

#include <iostream>
#include <filesystem>

namespace fs = std::filesystem;

RssParser::RssParser(const Lib::dataSource &src, const std::string &working_dir, volatile bool &terminate_signal) :
Parser(src, working_dir, terminate_signal) {}

void RssParser::parse(mysqlx::Session &db_session) {
	if (terminate_signal_caught_) {
		std::cerr << "Terminate signal caught. Exiting..." << std::endl;
		return;
	}

	std::cout << "Parsing " << url_ << std::endl;
	std::string filename { working_dir_ + "/feed.xml" };
	DownloadFile df;
	df.download(url_, filename);
	if (!df.success()) {
		std::cerr << "HTTP code: " << df.getHttpCode() << std::endl;
		std::cerr << "Error: " << df.getError() << std::endl;

		return;
	}
	if (df.getContentType() != "text/xml" && df.getContentType() != "application/xml") {
		std::cout << "Content-Type: " << df.getContentType() << ". Skipping url..." << std::endl;
		return;
	}
	
	xml_ =  xmlReadFile(filename.c_str(), df.getCharset().c_str(), HTML_PARSE_NOBLANKS | HTML_PARSE_NOERROR | HTML_PARSE_NOWARNING | HTML_PARSE_NONET);
	if (xml_ == nullptr) {
		fs::remove(filename);
		throw std::runtime_error{ "HTML parse error" };
	}
	xmlNodePtr root_ = xmlDocGetRootElement(xml_);
	if (root_ == nullptr) {
		xmlFreeDoc(xml_);
		throw std::runtime_error{ "document is empty" };
	}
	fs::remove(filename);
	traverseTree(root_);
	fillDatabase(db_session);

	xmlFreeDoc(xml_);
}

void RssParser::fillDatabase(mysqlx::Session &db_session) const {
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
			if (!post.title.empty()) {
				ss.str(std::string{});
				ss << "INSERT INTO `publications_text` (`publication_id`, `is_header`, `text`) VALUES (" << new_id << ", TRUE, '" << post.title << "');";
				db_session.sql(ss.str()).execute();
			}
			ss.str(std::string{});
			ss << "INSERT INTO `publications_text` (`publication_id`, `is_header`, `text`) VALUES (" << new_id << ", FALSE, '" << post.description << "');";
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

void RssParser::traverseTree(xmlNodePtr node) {
	for (auto current_node = node; current_node != nullptr; current_node = current_node->next) {
		std::string node_name;
		if (current_node->name != nullptr) {
			node_name = std::string{ reinterpret_cast<const char *>(current_node->name) };
		}
		if (node_name == "item") {
			RssPost post_data;
			extractDataFromNode(current_node->children, post_data);
			posts_.push_back(post_data);
			continue;
		}
		traverseTree(current_node->children);
	}	
}
	
void RssParser::extractDataFromNode(xmlNodePtr node, RssPost &post_data) const {
	for (auto current_node = node; current_node != nullptr; current_node = current_node->next) {
		std::string node_name;
		if (current_node->name != nullptr) {
			node_name = std::string{ reinterpret_cast<const char *>(current_node->name) };
		}
		if (node_name == "link") {
			post_data.link = extractTextFromNode(xml_, current_node);
		}
		else if (node_name == "content") {	
			auto image = getAttribute(current_node, "url");;
			post_data.images.push_back(image);
		}
		else if (node_name == "description") {
			auto text = extractTextFromNode(xml_, current_node);
			if (!text.empty()) {
				post_data.description = text;
			}
		}
		else if (node_name == "title") {
			auto text = extractTextFromNode(xml_, current_node);
			if (!text.empty()) {
				post_data.title = text;
			}
		}
	}
}

std::string RssParser::getAttribute(xmlNodePtr node, const std::string &attribute_for_search) const {
	for (xmlAttr *attribute = node->properties; attribute != nullptr; attribute = attribute->next) {
		auto attribute_name{ reinterpret_cast<const char *>(attribute->name) };
		auto attribute_value{ reinterpret_cast<const char *>(xmlNodeListGetString(xml_, attribute->children, 1)) };
		if (strcmp(attribute_name, attribute_for_search.c_str()) == 0) {
			return std::string{ attribute_value };
		}
	}
	
	return std::string{};
}

RssParser::~RssParser() {
	xmlCleanupParser();
}
