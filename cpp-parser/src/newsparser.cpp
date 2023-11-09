#include "newsparser.h"

#include <list>
#include <filesystem>

#include <sys/types.h>
#include <unistd.h>

namespace chrono = std::chrono;
namespace fs = std::filesystem;

NewsParser::NewsParser() {
	working_dir_ = WORKING_DIR_PREFIX + std::to_string(getpid());
	fs::create_directory(working_dir_);
	try {
		mysqlx::Session db_session{
			config_["DBHost"],
			std::stoi(config_["DBPort"]),
			config_["DBUser"],
			config_["DBPassword"]
		};
		db_session.sql(std::string{ "USE " }.append(config_["DBName"])).execute();
		try {
			std::string req{ "SELECT `id`, `url`, `source_type`, `parse_depth`, unix_timestamp(`created`) FROM sources ORDER BY `id`" };
			auto result = db_session.sql(req).execute();

			auto epoch = chrono::time_point<chrono::high_resolution_clock>();
			auto rows = result.fetchAll();
			for (const auto &row: rows) {
				auto since_epoch = chrono::seconds((row[4].get<long>()));
				auto timestamp = epoch + since_epoch;
				auto type = row[2].get<mysqlx::string>();
				sources_.emplace_back(
					row[0].get<unsigned>(),
					row[1].get<mysqlx::string>(),
					type,
					row[3].get<unsigned>(),
					timestamp
				);
			}
		}
		catch (const mysqlx::Error &e) {
			std::stringstream ss;
			ss << "Unable to fetch sources list: " << e;
			logError(ss.str());
		}

	}
	catch (const mysqlx::Error &e) {
		std::stringstream ss;
		ss << "Unable to connect to database: " << e;
		logError(ss.str());
	}


}

void NewsParser::run() {
	try {
		mysqlx::Session db_session{
			config_["DBHost"],
			std::stoi(config_["DBPort"]),
			config_["DBUser"],
			config_["DBPassword"]
		};
		db_session.sql("USE " + config_["DBName"]).execute();
		std::shared_ptr<IParser> parser;
		for (const auto &src: sources_) {
			if (src.type == "Web") {
				try {
					parser = std::make_shared<HtmlParser>(src, working_dir_);
				}
				catch (const std::invalid_argument &e) {
					std::stringstream ss;
					ss << "Error: " << e.what();
					logError(ss.str());
				}
				parser->parse(db_session);
			}
		}
	}
	catch (const mysqlx::Error &e) {
		std::stringstream ss;
		ss << "Unable to connect to database: " << e;
		logError(ss.str());
	}
}

NewsParser::~NewsParser() {
	if (fs::exists(working_dir_)) {
		fs::remove_all(working_dir_);
	}
}

void NewsParser::logError(const std::string &str) {
	std::cerr << str << std::endl;
}
