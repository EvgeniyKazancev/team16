#pragma once
#include <iconv.h>
#include <string>

class Converter final {
public:
	Converter();
	~Converter();
	void resize(size_t new_size);
	std::string convert(const std::string &, const std::string &, const std::string &);
private:
	size_t size_{ 2 };
	char *buffer_;
	iconv_t converter_;
};

