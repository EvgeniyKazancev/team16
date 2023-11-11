#pragma once

#include "parser.h"

#include <mysqlx/xdevapi.h>

class RssParser final : public Parser {
public:
	RssParser() = delete;
	RssParser(const RssParser &) = delete;
	RssParser &operator=(const RssParser&) = delete;
	RssParser(const Lib::dataSource &src, const std::string &working_dir, volatile bool &terminate_signal);
	void parse(mysqlx::Session &db_session) override;
	~RssParser();
private:
	
};

