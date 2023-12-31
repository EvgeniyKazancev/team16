#pragma once

#include "parser.h"

#include <string>
#include <map>
#include <set>
#include <vector>
#include <utility>

#include <mysqlx/xdevapi.h>

class HtmlParser : public Parser {
public: 
	HtmlParser() = delete;
	HtmlParser(const HtmlParser &) = delete;
	HtmlParser &operator=(const HtmlParser&) = delete;
	HtmlParser(const Lib::dataSource &src, const std::string &working_dir, volatile bool &terminate_signal);
	void parse(mysqlx::Session &db_session) override;
	~HtmlParser();

private:
	std::string getProtoFromUrl(const std::string &url) const;
	std::string getDomainFromUrl(const std::string &url) const;
	std::string completeUrl(const std::string &url) const;
	bool urlEqual(const std::string &url1, const std::string &url2) const;
	void parseOpenGraph(htmlDocPtr doc, xmlNode *node, std::map<std::string, std::string> &open_graph);
	void parseLinks(htmlDocPtr doc, xmlNode *node, const std::string &current_url, std::set<std::string> &links);
	void fillDatabase(mysqlx::Session &db_session, const std::string &url, const std::set<std::pair<bool, std::string>> &text_blocks, const std::map<std::string, std::string> &open_graph) const;
	void traverseTree(
		htmlDocPtr doc,
		xmlNode *node,
		const std::string &url,
		std::set<std::string> &links,
		std::map<std::string, std::string> &open_graph,
		std::set<std::pair<bool, std::string>> &text_blocks
	);
	void searchForText(
		htmlDocPtr doc,
		xmlNode *node,
		std::set<std::pair<bool, std::string>> &text_blocks
	);
	void parseUrl(mysqlx::Session &db_session, const std::string &url, unsigned parse_depth, unsigned fileno = 1);
	std::string getMainPageAddress() const;


protected:

	std::string proto_;
	std::string domain_;	
};

