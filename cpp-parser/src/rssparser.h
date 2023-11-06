#pragma once

#include "iparser.h"

#include <mysqlx/xdevapi.h>

class RssParser final : public IParser {
public:
	RssParser() = delete;
	RssParser(const RssParser &) = delete;
	RssParser &operator=(const RssParser&) = delete;
	RssParser(const Lib::dataSource &src);
	void parse(const mysqlx::Session &db_session) override;
	~RssParser();
private:
	
};

