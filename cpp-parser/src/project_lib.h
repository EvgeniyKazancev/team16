#pragma once

#include <vector>
#include <string>
#include <chrono>

namespace Lib {
	std::vector<std::string> split(const std::string &, const std::string &);
	void ltrim(std::string &s);
	void rtrim(std::string &s);
	void trim(std::string &s);
	void encodeHtml(std::string &content_str);

	struct dataSource {
		unsigned id;
		std::string url;
		std::string type;
		unsigned parse_depth;
		std::chrono::system_clock::time_point created;
	};
}

