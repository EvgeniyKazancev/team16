#include "htmlparser.h"
#include "download_file.h"
#include "project_lib.h"

#include <string>
#include <iostream>
#include <filesystem>
#include <stdexcept>

namespace fs = std::filesystem;

HtmlParser::HtmlParser(const Lib::dataSource &src, const std::string &working_dir) :
	IParser(src, working_dir) {
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

void HtmlParser::traverseTree(htmlDocPtr doc, xmlNode *node, const std::string &url, std::set<std::string> &links, std::map<std::string, std::string> &open_graph) {
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
			traverseTree(doc, current_node->children, url, links, open_graph);
			continue;
		}
		if (current_node->type == XML_ELEMENT_NODE) {
			auto childNode = current_node->xmlChildrenNode;
			if (childNode == nullptr) {
				traverseTree(doc, current_node->children, url, links, open_graph);
				continue;
			}
			if (childNode->type == XML_TEXT_NODE) {
				xmlChar* content;
				content = xmlNodeListGetString(doc, current_node->xmlChildrenNode, 1);
				if (content != nullptr) {
					std::string content_str{ reinterpret_cast<const char *>(content) };
					Lib::trim(content_str);
					if (content_str.length() <= 8) {
						xmlFree(content);	
						traverseTree(doc, current_node->children, url, links, open_graph);
						continue;
					}
					std::cout << node_name << ": " << std::quoted(content_str) << std::endl;
				}
				xmlFree(content);	
			}
		}
		traverseTree(doc, current_node->children, url, links, open_graph);
	}	
}

void HtmlParser::parse(const mysqlx::Session &db_session) {
	parseUrl(db_session, url_, parse_depth_);
}

void HtmlParser::parseUrl(const mysqlx::Session &db_session, const std::string &url, const unsigned parse_depth) {
	if (parse_depth == 0) {
		return;
	}

	std::cout << "Parsing " << url << " (" << getDomainFromUrl(url) << ")\n" << std::endl;
	//std::string filename { working_dir_.append("/webpage.html") };
	std::string filename { "reddit.html" };
	std::string content_type;
	//try {
	//	DownloadFile{ url, filename };
	//}
	//catch (const std::runtime_error &e) {
	//	throw std::runtime_error{ std::string{ "error while downloading html: " } + e.what() };
	//	return;
	//}
	
	htmlDocPtr doc{ htmlReadFile(filename.c_str(), "utf-8", HTML_PARSE_NOBLANKS | HTML_PARSE_NOERROR | HTML_PARSE_NOWARNING | HTML_PARSE_NONET) };
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

	traverseTree(doc, root_element, url, links, open_graph);

	for (const auto it: open_graph) {
		std::cout << it.first << ": " << it.second << std::endl;
	}

	for (const auto &link: links) {
		std::cout << "Link: " << link << " " << std::endl;
	}
	
	//fs::remove(filename);
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

	if (completeUrl(href).find(current_url) != std::string::npos && !urlEqual(current_url, href) && !links.contains(href)) {
		links.insert(href);
	}
}

HtmlParser::~HtmlParser() {
	xmlCleanupParser();
}
