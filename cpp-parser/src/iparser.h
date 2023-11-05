#pragma once

#include <string>

class IParser {
public:
	IParser() = delete;
	IParser(const IParser &) = delete;
	IParser &operator=(const IParser&) = delete;
	IParser(const std::string& url);
	virtual void Parse() = 0;
	virtual ~IParser() = 0;
protected:
	unsigned parse_depth_;
	std::string url_;
};

