#include "download_file.h"
#include "converter.h"

#include <iostream>
#include <string>
#include <list>
#include <map>

#include <tinyxml2.h>
#include <mysqlx/xdevapi.h>
#include <nlohmann/json.hpp>

namespace tx = tinyxml2;
using json = nlohmann::json;

int main() {
	//DownloadFile{ "https://ria.ru/export/rss2/archive/index.xml", "index.xml" };
	try {
		mysqlx::Session session{ "localhost", 33060, "mh16", "exPwpf6SSb" };
	session.sql("USE test").execute();
	session.sql("DELETE FROM articles").execute();
	//mysqlx::Schema session{ session.getSchema("mh16") };

	tx::XMLDocument doc;
	std::string path{ "main.xml" };

	doc.LoadFile(path.c_str());
	tx::XMLPrinter printer;
	doc.Print(&printer);
	std::string text{ printer.CStr() };
	size_t declaration_start = text.find("<?xml");
	size_t declaration_end;
	std::string declaration;
	std::string encoding;
	if (declaration_start != std::string::npos) {
		declaration_end = text.find("?>", declaration_start) + 2;
		if (declaration_end != std::string::npos) {
			declaration = text.substr(declaration_start, declaration_end - declaration_start);
		}
	}
	if (!declaration.empty()) {
		auto encoding_defined = declaration.find("encoding");
		char quote_type{ '\0' };
		size_t quote_pos{ std::string::npos };
		if (encoding_defined != std::string::npos) {
			auto single_quote_pos = declaration.find("'", encoding_defined);
			auto double_quote_pos = declaration.find('"', encoding_defined);
			if (single_quote_pos == std::string::npos && double_quote_pos != std::string::npos) {
				quote_type = '"';
				quote_pos = double_quote_pos;
			}
			else if (single_quote_pos != std::string::npos && double_quote_pos == std::string::npos) {
				quote_type = '\'';
				quote_pos = single_quote_pos;
			}
			else if (single_quote_pos != std::string::npos && double_quote_pos != std::string::npos) {
				auto min_pos = std::min(single_quote_pos, double_quote_pos);
				if (min_pos == single_quote_pos) {
					quote_type = '\'';
					quote_pos = single_quote_pos;
				}
				else {
					quote_type = '"';
					quote_pos = double_quote_pos;
				}
			}
		}
		if (quote_pos != std::string::npos) {
			size_t end_quote_pos = declaration.find(quote_type, quote_pos + 1);
			encoding = declaration.substr(quote_pos + 1, end_quote_pos - (quote_pos + 1));
		}
	}
	std::cout << "Encoding: " << encoding << std::endl;

	tx::XMLElement *pRootElement = doc.RootElement();

	if(!pRootElement) { 
		std::cerr << "Document is empty. Exiting..." << std::endl;
	}

	Converter converter;
	auto channel = pRootElement->FirstChildElement("channel");
	auto src = channel->FirstChildElement("title");
	auto src_str{ converter.convert(encoding, "UTF-8", src->GetText()) };
	std::cout << "Source: " << src_str << std::endl;
	
	std::list<std::map<std::string, std::string>> articles;
	auto item = channel->FirstChildElement("item");
	session.startTransaction();
	while (item != nullptr) {
		auto title = item->FirstChildElement("title");
		//std::cout << "\tTitle: " << converter.convert(encoding, "UTF-8", title->GetText()) << std::endl;
		auto link = item->FirstChildElement("link");
		//std::cout << "\tLink: " << converter.convert(encoding, "UTF-8", link->GetText()) << std::endl;
		std::map<std::string, std::string> article_map {
			{ "title", converter.convert(encoding, "UTF-8", title->GetText()) },
			{ "link", converter.convert(encoding, "UTF-8", link->GetText()) } 
		};
		articles.push_back(article_map);
		item = item->NextSiblingElement("item");
		std::string req{
			std::string{ "INSERT INTO `articles`(`source`, `caption`, `link`) VALUES ('" } + 
			converter.convert(encoding, "UTF-8", src->GetText()) + 
			"', '" +
			converter.convert(encoding, "UTF-8", title->GetText()) +
			"', '" +
			converter.convert(encoding, "UTF-8", link->GetText()) +
			"')"};
		session.sql(req).execute();
		
	}
	session.commit();
	json j1;
	j1["source"] = src_str;
	json j2(articles);
	j2 += j1;
	std::cout << j2.dump() << std::endl;

	}
	catch (const mysqlx::Error &err) {
		std::cerr << "Error: " << err << std::endl;
	}
	exit(EXIT_FAILURE);
	exit(EXIT_SUCCESS);
}

