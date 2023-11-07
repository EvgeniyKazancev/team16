#include "converter.h"

#include <algorithm>

extern "C" {
	#include <cstring>
}

Converter::Converter() {
	buffer_ = new char[size_ + 1];
}

std::string Converter::convert(
	const std::string &from,
	const std::string &to,
	const std::string &src) {
	converter_ = iconv_open(to.c_str(), from.c_str());
	auto src_len = src.length();
	auto dst_len = src_len * 4;
	resize(dst_len);
	std::fill(buffer_, buffer_ + size_ + 1, '\0');
	auto p_out = buffer_;
	char *p_in = new char[src.length() + 1];
	auto p_in_copy = p_in;
	strcpy(p_in, src.c_str());
	iconv(converter_, &p_in, &src_len, &p_out, &dst_len);
	iconv_close(converter_);
	delete[] p_in_copy;
	return std::string{ buffer_ };
}

void Converter::resize(size_t new_size) {
	if (new_size <= size_) {
		return;
	}
	auto old_buffer = buffer_;
	buffer_ = new char[new_size + 1];
	memcpy(buffer_, old_buffer, size_ + 1);
	size_ = new_size;
	delete[] old_buffer;
}

Converter::~Converter() {
	delete[] buffer_;
}


