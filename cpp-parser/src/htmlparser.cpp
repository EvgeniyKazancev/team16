#include "htmlparser.h"
#include "download_file.h"
#include "project_lib.h"

#include <string>
#include <iostream>
#include <filesystem>
#include <stdexcept>

namespace fs = std::filesystem;

HtmlParser::HtmlParser(const Lib::dataSource &src, const std::string &working_dir, volatile bool &terminate_signal) :
	Parser(src, working_dir, terminate_signal) {
	if (src.type != "Web") {
		throw std::invalid_argument{ "source is not Web page" };
	}
	domain_ = getDomainFromUrl(url_);
	proto_ = getProtoFromUrl(url_);

}

std::string HtmlParser::completeUrl(const std::string &url) const {
	if (url.empty()) {
		return std::string{};
	}
	if (url.substr(0, 7) == "mailto:") {
		return url;
	}

	std::string correct_url{ url };
	if (correct_url[correct_url.length() - 1] == '/') {
		correct_url = correct_url.substr(0, correct_url.length() - 1);
	}
	
	if (correct_url.length() >= 7 && correct_url.substr(0, 4) == "http") {
		return correct_url;
	}
	if (correct_url.length() >= 2 && correct_url.substr(0, 2) == "//") {
		return proto_ + ':' + correct_url;
	}
	if (url[0] == '/') {
		return proto_ + "://" + domain_ + correct_url;
	}

	return proto_ + "://" + domain_ + '/' + correct_url;
}

bool HtmlParser::urlEqual(const std::string &url1, const std::string &url2) const {
	if (url1.empty() || url2.empty()) {
		return false;
	}
	
	return completeUrl(url1) == completeUrl(url2);
}

std::string HtmlParser::getProtoFromUrl(const std::string &url) const {
	if (url.length() < 4 || url.substr(0, 4) != "http") {
		return proto_;
	}
	if (url[4] == 's') {
		return std::string{ "https" };
	}
	return std::string{ "http" };
}

std::string HtmlParser::getDomainFromUrl(const std::string &url) const {
	if (url.length() >= 2 && url[0] == '/' && url[1] != '/') {
		return domain_;
	}
	if (url[0] != '/' && (url.length() < 4 || url.substr(0, 4) != "http")) {
		return domain_;
	}
	auto proto_pos = url.find("//");
	auto domain_pos = url.find('/', proto_pos + 2);

	return url.substr(proto_pos + 2, domain_pos - (proto_pos + 2));
}

void HtmlParser::searchForText(
	htmlDocPtr doc,
	xmlNode *node,
	std::set<std::pair<bool, std::string>> &text_blocks
) {
	if (node->type != XML_ELEMENT_NODE) {
		return;
	}
	auto childNode = node->xmlChildrenNode;
	if (childNode == nullptr || childNode->type != XML_TEXT_NODE) {
		return;
	}

	std::string node_name;
	if (node->name != nullptr) {
		node_name = reinterpret_cast<const char *>(node->name);
	}
	std::string content_str{ extractTextFromNode(doc, node) };

	if (content_str.length() <= 8) {
		return;
	}
	if (
		node_name == "title" || (
			node_name.length() >= 2 &&
			node_name[0] == 'h' &&
			std::isdigit(node_name[1])
		)
	) {
		auto pair = make_pair(true, content_str);
		if (!text_blocks.contains(pair)) {
			text_blocks.insert(pair);
		}
	}
	else if (content_str.length() >= 50) {
		auto pair = make_pair(false, content_str);

		if (!text_blocks.contains(pair)) {
			text_blocks.insert(pair);
		}
	}
}

std::string HtmlParser::getMainPageAddress() const {
	return proto_ + "://" + domain_;
}

void HtmlParser::traverseTree(
	htmlDocPtr doc,
	xmlNode *node,
	const std::string &url,
	std::set<std::string> &links,
	std::map<std::string, std::string> &open_graph,
	std::set<std::pair<bool, std::string>> &text_blocks
) {
	for (auto current_node = node; current_node != nullptr; current_node = current_node->next) {
		std::string node_name;
		if (current_node->name != nullptr) {
			node_name = std::string{ reinterpret_cast<const char *>(current_node->name) };
		}
		if (
			node_name.empty() || !(
				node_name == "title" ||
				node_name == "div" ||
				node_name == "p" || (
					node_name.length() >= 2 &&
					node_name[0] == 'h' &&
					std::isdigit(node_name[1])
				)
			)
		) {
			if (node_name == "meta") {
				parseOpenGraph(doc, current_node, open_graph);
			}
			else if (node_name == "a") {
				parseLinks(doc, current_node, url, links);
			}
			traverseTree(doc, current_node->children, url, links, open_graph, text_blocks);
			continue;
		}
		searchForText(doc, current_node, text_blocks);
		traverseTree(doc, current_node->children, url, links, open_graph, text_blocks);
	}	
}

void HtmlParser::parse(mysqlx::Session &db_session) {
	parseUrl(db_session, url_, parse_depth_);
}

void HtmlParser::fillDatabase(mysqlx::Session &db_session, const std::string &url, const std::set<std::pair<bool, std::string>> &text_blocks, const std::map<std::string, std::string> &open_graph) const {
	std::stringstream ss;
	long new_id{ 0 };

	auto result = db_session.sql("SELECT max(`id`) FROM `publications`").execute();
	auto row = result.fetchOne();
	if (!row[0].isNull()) {
		new_id = row[0].get<long>() + 1;
	}
	db_session.startTransaction();
	ss.str(std::string{});
	try {
		ss.str(std::string{});
		ss << "INSERT INTO `publications` (`id`, `source_id`, `url`) VALUES (" << new_id << ", " << source_id_ << ", '" << url << "');";
		db_session.sql(ss.str()).execute();
		for (auto it: text_blocks) {
			ss.str(std::string{});
			ss << "INSERT INTO `publications_text` (`publication_id`, `is_header`, `text`) VALUES (" << new_id << ", " << it.first << ", '" << it.second << "');";
			db_session.sql(ss.str()).execute();
		}
		for (auto it: open_graph) {
			ss.str(std::string{});
			ss << "INSERT INTO `publications_data` (`publication_id`, `property`, `content`) VALUES (" << new_id << ", '" << it.first << "', '" << it.second << "');";
			db_session.sql(ss.str()).execute();
		}
		db_session.commit();
	}
	catch (const mysqlx::Error &e) {
		db_session.rollback();
		std::cerr << "Error while processing MySQL query: " << e << std::endl;
	}
}

void HtmlParser::parseUrl(mysqlx::Session &db_session, const std::string &url, const unsigned parse_depth, const unsigned fileno) {
	if (parse_depth == 0) {
		return;
	}
	if (terminate_signal_caught_) {
		return;
	}

	std::cout << "Parsing " << url << " (" << getDomainFromUrl(url) << ") with depth " << parse_depth << "\n" << std::endl;
	std::string filename { working_dir_ + "/webpage-" + std::to_string(fileno) + ".html" };
	//std::string filename { "reddit.html" };
	DownloadFile df;
	
	df.download(url, filename);
	if (!df.success()) {
		std::cerr << "HTTP code: " << df.getHttpCode() << std::endl;
		std::cerr << "Error: " << df.getError() << std::endl;

		return;
	}
	if (df.getContentType() != "text/html") {
		std::cout << "Content-Type: " << df.getContentType() << ". Skipping url..." << std::endl;
		return;
	}
	
	htmlDocPtr doc{ htmlReadFile(filename.c_str(), df.getCharset().c_str(), HTML_PARSE_NOBLANKS | HTML_PARSE_NOERROR | HTML_PARSE_NOWARNING | HTML_PARSE_NONET) };
	if (doc == nullptr) {
		fs::remove(filename);
		throw std::runtime_error{ "HTML parse error" };
	}

	xmlNode *root_element{ xmlDocGetRootElement(doc) };
	if (root_element == nullptr) {
		xmlFreeDoc(doc);
		throw std::runtime_error{ "document is empty" };
	}

	std::map<std::string, std::string> open_graph;
	std::set<std::string> links;
	std::set<std::pair<bool, std::string>> text_blocks;

	traverseTree(doc, root_element, url, links, open_graph, text_blocks);

	fs::remove(filename);
	
	fillDatabase(db_session, url, text_blocks, open_graph);
	for (const auto &link: links) {
		if (terminate_signal_caught_) {
			xmlFreeDoc(doc);
			return;
		}

		try {
			std::stringstream ss;
			ss << "DELETE FROM `publications` WHERE `url` = '" << link << "' AND `created` + interval 60 minute < CURRENT_TIMESTAMP";
			db_session.sql(ss.str()).execute();
			ss.str(std::string{});
			ss << "SELECT `id` FROM `publications` WHERE `url` = '" << link << "'";
			auto result = db_session.sql(ss.str()).execute();
			auto row = result.fetchOne();
			if (row) {
				ss.str(std::string{});
				ss << "UPDATE `publications` SET `copies_count` = `copies_count` + 1 WHERE `id` = " << row[0].get<long>();
				db_session.sql(ss.str()).execute();
				std::cout << "\nPage " << std::quoted(link) << " has been already parsed. Skipping..." << std::endl;
				continue;
			}	
		}
		catch (const mysqlx::Error &e) {
			std::cerr << "error while accessing the database: " << e << std::endl;
		}
		parseUrl(db_session, link, parse_depth - 1, fileno + 1);
	}	
	xmlFreeDoc(doc);
}

void HtmlParser::parseOpenGraph(htmlDocPtr doc, xmlNode *node, std::map<std::string, std::string> &open_graph) {
	std::string key;
	std::string value;

	for (xmlAttr *attribute = node->properties; attribute != nullptr; attribute = attribute->next) {
		auto attribute_name{ reinterpret_cast<const char *>(attribute->name) };
		auto attribute_value{ reinterpret_cast<const char *>(xmlNodeListGetString(doc, attribute->children, 1)) };
		if (strcmp(attribute_name, "content") == 0) {
			value = std::string{ attribute_value };
		}
		else if (strncmp(attribute_value, "og", 2) == 0) {
			key = std::string{ attribute_value }.substr(3, strlen(attribute_value) - 3);
		}
	}

	if (!(key.empty() || value.empty())) {
		open_graph.insert({ key, value });
	}
}

void HtmlParser::parseLinks(htmlDocPtr doc, xmlNode *node, const std::string &current_url, std::set<std::string> &links) {
	std::string href;

	for (xmlAttr *attribute = node->properties; attribute != nullptr; attribute = attribute->next) {
		auto attribute_name{ reinterpret_cast<const char *>(attribute->name) };
		auto attribute_value{ reinterpret_cast<const char *>(xmlNodeListGetString(doc, attribute->children, 1)) };
		if (strcmp(attribute_name, "href") == 0) {
			href = std::string{ attribute_value };
		}
	}

	if (href.empty()) {
		return;
	}

	href = completeUrl(href);
	if (href.find(getMainPageAddress()) != std::string::npos && !urlEqual(current_url, href) && !links.contains(href)) {
		links.insert(href);
	}
}

HtmlParser::~HtmlParser() {
	xmlCleanupParser();
}
