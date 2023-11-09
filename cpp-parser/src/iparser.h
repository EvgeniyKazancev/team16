#pragma once

#include "project_lib.h"

#include <string>

#include <mysqlx/xdevapi.h>

class IParser {
public:
	IParser() = delete;
	IParser(const Lib::dataSource &src, const std::string &working_dir);
	IParser(const IParser &) = delete;
	IParser &operator=(const IParser&) = delete;
	virtual void parse(mysqlx::Session &db_session) = 0;
	virtual ~IParser() = 0;
protected:
	unsigned parse_depth_;
	std::string url_;
	std::string working_dir_;
	unsigned source_id_;
};

