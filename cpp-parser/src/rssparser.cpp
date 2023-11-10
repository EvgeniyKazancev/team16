#include "rssparser.h"

RssParser::RssParser(const Lib::dataSource &src, const std::string &working_dir) :
IParser(src, working_dir) {

}

void RssParser::parse(mysqlx::Session &db_session) {

}

RssParser::~RssParser() {

}
