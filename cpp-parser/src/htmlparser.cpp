#include "htmlparser.h"
#include "download_file.h"

#include <string>
#include <iostream>
#include <filesystem>

#include <mysqlx/xdevapi.h>

namespace fs = std::filesystem;

HtmlParser::HtmlParser(const Lib::dataSource &src, const std::string &working_dir) :
	IParser(src, working_dir) {
	if (src.type != "Web") {
		throw std::invalid_argument{ "source is not Web page" };
	}
}

void HtmlParser::parse (const mysqlx::Session &db_session) {
	std::cout << "Parsing " << url_ << std::endl;
	//std::string filename { working_dir_.append("/webpage.html") };
	std::string filename { "/tmp/webpage.html" };
	DownloadFile{ url_, filename };

	//fs::remove(filename);
}

HtmlParser::~HtmlParser() {

}
