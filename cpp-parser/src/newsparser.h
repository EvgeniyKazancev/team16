#pragma once

#include "config_file.h"
#include "converter.h"
#include "download_file.h"

#include <string>
#include <vector>
#include <chrono>
#include <ctime>

#include <mysqlx/xdevapi.h>

struct dataSource {
	unsigned id;
	std::string url;
	std::string type;
	unsigned parse_depth;
	std::chrono::system_clock::time_point created;
};

class NewsParser final {
public:
	NewsParser();
	~NewsParser();
	void run();

private:
	void logError(const std::string &str);

	const std::string CONFIG_FILE{ "parser.conf" };
	const std::string WORKING_DIR_PREFIX{ "/tmp/newsparser-" };
	std::string working_dir_;
	ConfigFile config_ { CONFIG_FILE };
	std::vector<dataSource> sources_;
};

