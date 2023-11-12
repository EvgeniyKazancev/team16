#include "project_lib.h"

#include <iostream>
#include <map>

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

// split string to set
std::set<std::string> Lib::splitToSet(const std::string &src, const std::string &delimiter) {
	size_t pos = 0;
	std::string src_copy { src };
	std::set<std::string> result;

	while ((pos = src_copy.find(delimiter)) != std::string::npos) {
		std::string temp{ src_copy.substr(0, pos) };
		if (!temp.empty() && !result.contains(temp)) {
			result.insert(temp);
		}
		src_copy.erase(0, pos + delimiter.length());
	}
	if (!src_copy.empty() && !result.contains(src_copy)) {
		result.insert(src_copy);
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

void Lib::encodeHtml(std::string &content_str) {
	std::map<char, std::string> html_entities = {
		{ '\'', "&apos;" },
		{ '\\', "&bsol;" }
	};
	for (auto it: html_entities) {
		for (
			auto apos = content_str.find(it.first);
			apos != std::string::npos;
			apos = content_str.find(it.first)
		) {
			content_str = content_str.substr(0, apos) + it.second + content_str.substr(apos + 1, content_str.length() - (apos + 1));
		}
	}
}

void Lib::removeFromString(std::string &str, const std::string pattern) {
	auto start = pattern.length();
	for (auto pos = str.find(pattern); pos != std::string::npos; pos = str.find(pattern)) {
		str = str.substr(0, pos) + str.substr(pos + start, str.length() - (pos + start));
	}
}

