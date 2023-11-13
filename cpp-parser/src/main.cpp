#include "newsparser.h"

#include <iostream>

extern "C" {
	#include <signal.h>
}

int main() {
	struct sigaction new_action;

	static NewsParser news_parser;
	new_action.sa_handler = [] (int signum) {
		news_parser.cleanExit();
	};
	sigaction(SIGINT, &new_action, nullptr);
	sigaction(SIGTERM, &new_action, nullptr);
	
	try {
		news_parser.run();
	}
	catch (const std::exception &e) {
		std::cerr << "Runtime error: " << e.what() << std::endl;
	}

	return EXIT_SUCCESS;
}
