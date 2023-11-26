#pragma once

#include <vector>
#include <set>
#include <string>
#include <chrono>

namespace Lib {
	std::vector<std::string> split(const std::string &, const std::string &);
	std::set<std::string> splitToSet(const std::string &, const std::string &);
	void ltrim(std::string &s);
	void rtrim(std::string &s);
	void trim(std::string &s);
	//void encodeHtml(std::string &content_str);
	void removeFromString(std::string &str, const std::string pattern);
	//void removeMultipleSpaces(std::string &str);

	struct dataSource {
		unsigned id;
		std::string url;
		std::string type;
		unsigned parse_depth;
		std::chrono::system_clock::time_point created;
	};
}

