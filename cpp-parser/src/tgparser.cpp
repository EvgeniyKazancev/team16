#include "tgparser.h"

#include <string>

#include <mysqlx/xdevapi.h>

TgParser::TgParser(const Lib::dataSource &src, const std::string &working_dir, volatile bool &terminate_signal) :
	Parser(src, working_dir, terminate_signal) {

}

void TgParser::parse (mysqlx::Session &db_session) {

}

TgParser::~TgParser() {

}
