#include "tgparser.h"

#include <string>

#include <mysqlx/xdevapi.h>

TgParser::TgParser(const Lib::dataSource &src) :
	IParser(src) {

}

void TgParser::parse (const mysqlx::Session &db_session) {

}

TgParser::~TgParser() {

}
