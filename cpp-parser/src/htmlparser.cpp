#include "htmlparser.h"
#include "download_file.h"

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
}

void HtmlParser::parse (const mysqlx::Session &db_session) {
	std::cout << "Parsing " << url_ << std::endl;
	std::string filename { working_dir_.append("/webpage.html") };
	//std::string filename { "/tmp/webpage.html" };
	try {
		DownloadFile{ url_, filename };
	}
	catch (const std::runtime_error &e) {
		throw std::runtime_error{ std::string{ "error while downloading html: " } + e.what() };
		return;
	}
	
	htmlDocPtr doc{ htmlReadFile(filename.c_str(), nullptr, HTML_PARSE_NOBLANKS | HTML_PARSE_NOERROR | HTML_PARSE_NOWARNING | HTML_PARSE_NONET) };
	if (doc == nullptr) {
		fs::remove(filename);
		throw std::runtime_error{ "HTML parse error" };
	}

	xmlNode *root_element{ xmlDocGetRootElement(doc) };
	if (root_element == nullptr) {
		xmlFreeDoc(doc);
		throw std::runtime_error{ "document is empty" };
	}

	traverseTree(doc, root_element);

	for (const auto it: open_graph_) {
		std::cout << it.first << ": " << it.second << std::endl;
	}
	
	fs::remove(filename);
}

void HtmlParser::traverseTree(xmlDoc *doc, xmlNode *node) {
	xmlNode *current_node{ nullptr };

	for (current_node = node; current_node != nullptr; current_node = current_node->next) {
		if (current_node->type == XML_ELEMENT_NODE && xmlStrcmp(current_node->name, reinterpret_cast<const xmlChar *>("meta")) == 0) {
			std::string key;
			std::string value;
			
			for (xmlAttr *attribute = current_node->properties; attribute != nullptr; attribute = attribute->next) {
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
				open_graph_.insert({ key, value });
			}
		}
	}
}
HtmlParser::~HtmlParser() {
	xmlCleanupParser();
}
