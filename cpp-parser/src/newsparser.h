#pragma once

#include "project_lib.h"
#include "config_file.h"
#include "converter.h"
#include "download_file.h"
#include "htmlparser.h"
#include "tgparser.h"
#include "rssparser.h"

#include <string>
#include <vector>
#include <chrono>
#include <memory>

#include <mysqlx/xdevapi.h>

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
	std::vector<Lib::dataSource> sources_;
	std::vector<std::shared_ptr<IParser>> parsers_;
};

