#pragma once

#include "parser.h"

#include <string>

#include <mysqlx/xdevapi.h>

struct TgPost {
	std::string link;
	std::string text;
	std::vector<std::string> images;
};

class TgParser final : public Parser {
public:
	TgParser() = delete;
	TgParser(const TgParser&) = delete;
	TgParser &operator=(const TgParser&) = delete;
	TgParser(const Lib::dataSource &src, const std::string &working_dir, volatile bool &terminate_signal);
	void parse(mysqlx::Session &db_session) override;
	~TgParser();

private:
	void fillDatabase(mysqlx::Session &db_session) const;
	std::string getBackgroundImage(htmlNodePtr node) const;
	std::map<std::string, std::string> getCss(htmlNodePtr node) const;
	std::string getAttribute(xmlNode *node, const std::string &attribute_for_search) const;
	std::set<std::string> getClasses(htmlNodePtr node) const;
	void extractDataFromNode(htmlNodePtr node, TgPost &post_data) const;
	void traverseTree(xmlNode *node);

	htmlDocPtr html_;
	htmlNodePtr root_;
	std::vector<TgPost> posts_;
};

