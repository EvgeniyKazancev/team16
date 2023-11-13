#include "parser.h"
#include "project_lib.h"

#include <iostream>

Parser::Parser(const Lib::dataSource &src, const std::string &working_dir, volatile bool &terminate_signal) :
working_dir_ { working_dir },
url_{ src.url },
parse_depth_{ src.parse_depth },
source_id_{ src.id },
terminate_signal_caught_ { terminate_signal } {}

void Parser::removeTags(std::string &str) const {
	for (auto lt_pos = str.find('<'); lt_pos != std::string::npos; lt_pos = str.find('<')) {
		auto gt_pos = str.find('>');
		if (gt_pos == std::string::npos) {
			return;
		}

		str = str.substr(0, lt_pos) + str.substr(gt_pos + 1, str.length() - (gt_pos + 1));
	}
}

std::string Parser::extractTextFromNode(xmlDocPtr doc, xmlNodePtr node) const {
	xmlBufferPtr buffer = xmlBufferCreate();
	int size = xmlNodeDump(buffer, doc, node, 0, 1);
	if (size == -1 || buffer == nullptr || buffer->content == nullptr) {
		xmlBufferFree(buffer);
		return std::string{};
	}
	std::string buffer_content{ reinterpret_cast<const char *>(buffer->content) };
	removeTags(buffer_content);
	Lib::trim(buffer_content);
	Lib::encodeHtml(buffer_content);
	xmlBufferFree(buffer);
	return buffer_content;
}


