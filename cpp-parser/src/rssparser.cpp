#include "rssparser.h"

RssParser::RssParser(const Lib::dataSource &src, const std::string &working_dir, volatile bool &terminate_signal) :
Parser(src, working_dir, terminate_signal) {

}

void RssParser::parse(mysqlx::Session &db_session) {

}

RssParser::~RssParser() {

}
