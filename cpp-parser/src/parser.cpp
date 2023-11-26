#include "parser.h"
#include "project_lib.h"

#include <iostream>
#include <iomanip>
#include <regex>
#include <algorithm>

Parser::Parser(const Lib::dataSource &src, const std::string &working_dir, volatile bool &terminate_signal) :
working_dir_ { working_dir },
url_{ src.url },
parse_depth_{ src.parse_depth },
source_id_{ src.id },
terminate_signal_caught_ { terminate_signal } {}

std::string Parser::extractTextFromNode(xmlDocPtr doc, xmlNodePtr node) const {
	xmlBufferPtr buffer = xmlBufferCreate();
	int size = xmlNodeDump(buffer, doc, node, 0, 1);
	if (size == -1 || buffer == nullptr || buffer->content == nullptr) {
		xmlBufferFree(buffer);
		return std::string{};
	}
	std::string buffer_content{ reinterpret_cast<const char *>(buffer->content) };
	xmlBufferFree(buffer);
	buffer_content = std::regex_replace(buffer_content, std::regex{ "<\\s*br.*?>" }, "\n"); // removing <br>
	buffer_content = std::regex_replace(buffer_content, std::regex{ "<.*?>" }, " "); // removing other tags
	std::erase(buffer_content, '\r'); // removing CR - the LF is enough for us
	buffer_content = std::regex_replace(buffer_content, std::regex{ "\\s{2,}" }, " "); // removing multiple spaces
	buffer_content = std::regex_replace(buffer_content, std::regex{ "\\'" }, "&apos;"); // removing apostrophs
	buffer_content = std::regex_replace(buffer_content, std::regex{ "\\\\" }, "&bsol;"); // removing backslashes

	Lib::trim(buffer_content);
	return buffer_content;
}


