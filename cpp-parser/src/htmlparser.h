#pragma once

#include "iparser.h"

#include <string>
#include <map>
#include <set>
#include <vector>

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
	void parseUrl(const mysqlx::Session &db_session, const std::string &url, unsigned parse_depth);
	void traverseTree(htmlDocPtr doc, xmlNode *node, const std::string &url, std::set<std::string> &links, std::map<std::string, std::string> &open_graph);
	std::string getProtoFromUrl(const std::string &url) const;
	std::string getDomainFromUrl(const std::string &url) const;
	std::string completeUrl(const std::string &url) const;
	bool urlEqual(const std::string &url1, const std::string &url2) const;
	void parseOpenGraph(htmlDocPtr doc, xmlNode *node, std::map<std::string, std::string> &open_graph);
	void parseLinks(htmlDocPtr doc, xmlNode *node, const std::string &current_url, std::set<std::string> &links);

	std::map<std::string, std::string> open_graph_;
	std::string proto_;
	std::string domain_;
	std::string caption_;
	std::string text_;
	std::set<std::string> links_;
	std::set<std::string> headers_;
	std::set<std::string> text_blocks_;
};

