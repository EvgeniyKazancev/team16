#pragma once

#include "parser.h"

#include <vector>
#include <string>

#include <libxml/parser.h>
#include <mysqlx/xdevapi.h>

struct RssPost {
	std::string link;
	std::string title;
	std::string description;
	std::vector<std::string> images;
};

class RssParser final : public Parser {
public:
	RssParser() = delete;
	RssParser(const RssParser &) = delete;
	RssParser &operator=(const RssParser&) = delete;
	RssParser(const Lib::dataSource &src, const std::string &working_dir, volatile bool &terminate_signal);
	void parse(mysqlx::Session &db_session) override;
	~RssParser();

private:
	std::string getAttribute(xmlNodePtr node, const std::string &attribute_for_search) const;
	void fillDatabase(mysqlx::Session &db_session) const;
	void extractDataFromNode(xmlNodePtr node, RssPost &post_data) const;
	void traverseTree(xmlNodePtr node);

	xmlDocPtr xml_;
	xmlNodePtr root_;
	std::vector<RssPost> posts_;
};

