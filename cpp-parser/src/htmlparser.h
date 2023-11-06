#pragma once

#include "iparser.h"

#include <string>

#include <mysqlx/xdevapi.h>

class HtmlParser final : public IParser {
public: 
	HtmlParser() = delete;
	HtmlParser(const HtmlParser &) = delete;
	HtmlParser &operator=(const HtmlParser&) = delete;
	HtmlParser(const Lib::dataSource &src);
	void parse(const mysqlx::Session &db_session) override;
	~HtmlParser();
};

