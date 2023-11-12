#pragma once

#include "project_lib.h"

#include <string>

#include <libxml/HTMLparser.h>
#include <mysqlx/xdevapi.h>

class Parser {
public:
	Parser() = delete;
	Parser(const Lib::dataSource &src, const std::string &working_dir, volatile bool &terminate_signal);
	Parser(const Parser &) = delete;
	Parser &operator=(const Parser&) = delete;
	virtual void parse(mysqlx::Session &db_session) = 0;
	virtual ~Parser() = default;

protected:	
	std::string extractTextFromNode(xmlDocPtr doc, xmlNodePtr node) const;
	void removeTags(std::string &str) const;

	unsigned parse_depth_;
	std::string url_;
	std::string working_dir_;
	unsigned source_id_;
	volatile bool &terminate_signal_caught_;
};

