#pragma once

#include "iparser.h"

class RssParser final : public IParser {
public:
	RssParser() = delete;
	RssParser(const RssParser &) = delete;
	RssParser &operator=(const RssParser&) = delete;
	RssParser(const std::string& url);
	void Parse() override;
	~RssParser();
private:
	
};

