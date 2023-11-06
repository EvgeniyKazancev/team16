#pragma once

#include "iparser.h"

#include <string>

#include <mysqlx/xdevapi.h>

class TgParser final : public IParser {
	TgParser() = delete;
	TgParser(const TgParser&) = delete;
	TgParser &operator=(const TgParser&) = delete;
	TgParser(const Lib::dataSource &src);
	void parse(const mysqlx::Session &db_session) override;
	~TgParser();
};

