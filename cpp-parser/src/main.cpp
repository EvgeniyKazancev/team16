#include "newsparser.h"

#include <iostream>

int main() {
	NewsParser news_parser;
	try {
		news_parser.run();
	}
	catch (const std::exception &e) {
		std::cerr << "Runtime error: " << e.what() << std::endl;
	}

	return EXIT_SUCCESS;
}
