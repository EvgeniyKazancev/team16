#pragma once

#include "iparser.h"

#include <string>
#include <map>

#include <libxml/HTMLparser.h>
#include <mysqlx/xdevapi.h>

class HtmlParser final : public IParser {
public: 
	HtmlParser() = delete;
	HtmlParser(const HtmlParser &) = delete;
	HtmlParser &operator=(const HtmlParser&) = delete;
	HtmlParser(const Lib::dataSource &src, const std::string &working_dir);
	void parse(const mysqlx::Session &db_session) override;
	~HtmlParser();

private:
	void traverseTree(xmlDoc *doc, xmlNode *node);

	std::map<std::string, std::string> open_graph_;
	std::string caption_;
	std::string text_;
};

