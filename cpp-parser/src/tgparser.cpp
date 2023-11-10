#include "tgparser.h"

#include <string>

#include <mysqlx/xdevapi.h>

TgParser::TgParser(const Lib::dataSource &src, const std::string &working_dir) :
	IParser(src, working_dir) {

}

void TgParser::parse (mysqlx::Session &db_session) {

}

TgParser::~TgParser() {

}
