#include "iparser.h"

IParser::IParser(const Lib::dataSource &src, const std::string &working_dir) :
working_dir_ { working_dir } {
	url_ = src.url;
	parse_depth_ = src.parse_depth;
}

IParser::~IParser() {}
