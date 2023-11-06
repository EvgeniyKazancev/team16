#include "iparser.h"

IParser::IParser(const Lib::dataSource &src) {
	url_ = src.url;
	parse_depth_ = src.parse_depth;
}

IParser::~IParser() {}
