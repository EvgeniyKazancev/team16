#pragma once

#include "parser.h"

#include <string>

#include <mysqlx/xdevapi.h>

class TgParser final : public Parser {
	TgParser() = delete;
	TgParser(const TgParser&) = delete;
	TgParser &operator=(const TgParser&) = delete;
	TgParser(const Lib::dataSource &src, const std::string &working_dir, volatile bool &terminate_signal);
	void parse(mysqlx::Session &db_session) override;
	~TgParser();
};

