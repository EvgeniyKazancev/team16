#pragma once

#include "iparser.h"

#include <string>

#include <mysqlx/xdevapi.h>

class TgParser final : public IParser {
	TgParser() = delete;
	TgParser(const TgParser&) = delete;
	TgParser &operator=(const TgParser&) = delete;
	TgParser(const Lib::dataSource &src, const std::string &working_dir);
	void parse(mysqlx::Session &db_session) override;
	~TgParser();
};

