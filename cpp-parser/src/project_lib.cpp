#include "project_lib.h"
#include <iostream>

// split string to vector
std::vector<std::string> Lib::split(const std::string &src, const std::string &delimiter) {
	size_t pos = 0;
	std::string src_copy { src };
	std::vector<std::string> result;

	while ((pos = src_copy.find(delimiter)) != std::string::npos) {
		result.emplace_back(src_copy.substr(0, pos));
		src_copy.erase(0, pos + delimiter.length());
	}
	if (!src_copy.empty()) {
		result.push_back(src_copy);
	}
	return result;
}

void Lib::ltrim(std::string &s) {
	s.erase(
		s.begin(),
		std::find_if(
			s.begin(),
			s.end(),
			[](char c) {
				return !std::isspace(c);
			}
		)
	);
}

void Lib::rtrim(std::string &s) {
	s.erase(
		std::find_if(
			s.rbegin(),
			s.rend(),
			[](char c) {
				return !std::isspace(c);
			}
		).base(),
		s.end()
	);
}

void Lib::trim(std::string &s) {
	ltrim(s);
	rtrim(s);
}

