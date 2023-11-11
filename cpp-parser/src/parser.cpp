#include "parser.h"

Parser::Parser(const Lib::dataSource &src, const std::string &working_dir, volatile bool &terminate_signal) :
working_dir_ { working_dir },
url_{ src.url },
parse_depth_{ src.parse_depth },
source_id_{ src.id },
terminate_signal_caught_ { terminate_signal } {}

