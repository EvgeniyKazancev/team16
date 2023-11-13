#include "config_file.h"

#include <algorithm>
#include <filesystem>

namespace fs = std::filesystem;

ConfigFile::ConfigFile(const std::string &filename) {
	std::string current_filename{ filename };
	std::cout << current_filename << std::endl;
	if (!fs::exists(current_filename)) {
		auto path = Lib::split(current_filename, "/");
		current_filename = std::string{ "/etc/" + path.back() };
	}
	std::ifstream ifs(current_filename, std::ios::in);
	if (!ifs.is_open()) {
		throw std::runtime_error{ "Cannot open file '" + filename + "' for read" };
	}
	std::string buf;
	while (!ifs.eof()) {
		getline(ifs, buf);
		// Removing space characters from string
		std::erase(buf, ' ');
		std::erase(buf, '\t');
		// Removing comments
		auto octotorp = buf.find('#');
		if (octotorp != std::string::npos) {
			buf.erase(octotorp, buf.length() - octotorp);
		}
		auto tokens = Lib::split(buf, "=");
		if (tokens.size() >= 2 && options_.find(tokens[0]) == options_.end()) {
			options_.insert(make_pair(tokens[0], tokens[1]));
		}
	}
	ifs.close();
}

const std::string &ConfigFile::operator[](const std::string &index) const {
	return options_.at(index);
}

