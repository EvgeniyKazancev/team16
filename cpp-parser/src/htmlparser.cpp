#include "htmlparser.h"

#include <string>
#include <iostream>

#include <mysqlx/xdevapi.h>

HtmlParser::HtmlParser(const Lib::dataSource &src) :
	IParser(src) {
	if (src.type != "Web") {
		throw std::invalid_argument{ "source is not Web page" };
	}
}

void HtmlParser::parse (const mysqlx::Session &db_session) {
	std::cout << "Parsing " << url_ << std::endl;
}

HtmlParser::~HtmlParser() {

}
