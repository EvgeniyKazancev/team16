#include "download_file.h"

#include <cstdlib>
#include <stdexcept>
#include <iostream>
#include <string>
#include <functional>
#include <sstream>
#include <iomanip>
#include <algorithm>

#include <unistd.h>

DownloadFile::DownloadFile(const std::string &url, const std::string &filename) {
	auto filename_cstr = filename.c_str();
	unsigned short attemp_number{ 1 };

	curl_global_init(CURL_GLOBAL_ALL);
	curl_handle_ = curl_easy_init();
	curl_easy_setopt(curl_handle_, CURLOPT_URL, url.c_str());
	curl_easy_setopt(curl_handle_, CURLOPT_VERBOSE, 1L);
	curl_easy_setopt(curl_handle_, CURLOPT_NOPROGRESS, 1L);
	curl_easy_setopt(curl_handle_, CURLOPT_WRITEFUNCTION, write_data);
	curl_easy_setopt(curl_handle_, CURLOPT_HEADERFUNCTION, read_header);
	curl_easy_setopt(curl_handle_, CURLOPT_HEADERDATA, &headers_);
	curl_easy_setopt(
		curl_handle_,
		CURLOPT_USERAGENT,
		"Mozilla/5.0 (Linux; Android 13; SAMSUNG SM-F916B) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/22.0 Chrome/111.0.5563.116 Mobile Safari/537.36"
	);

	file_ = fopen(filename_cstr, "wb");
	if (!file_) {
		throw std::runtime_error{ "Can not open file for write" };
	}

	curl_easy_setopt(curl_handle_, CURLOPT_WRITEDATA, file_);
	curl_easy_perform(curl_handle_);
	fclose(file_);
	char *ct = nullptr;
	auto res = curl_easy_getinfo(curl_handle_, CURLINFO_CONTENT_TYPE, &ct);
	if (res == 0 && ct != nullptr) {
		content_type_ = ct;
	}
	long http_code = 0;
	curl_easy_getinfo(curl_handle_, CURLINFO_RESPONSE_CODE, &http_code);
	if (http_code == 200) {
		curl_easy_cleanup(curl_handle_);
		return;
	}
	if (http_code == 404) {
		throw std::runtime_error{ "404 not found" };
	}
	if (http_code == 403) {
		throw std::runtime_error{ "403 forbidden" };
	}
	if (http_code != 301 && http_code != 302) {
		std::stringstream ss;
		ss << "http response code is " << http_code;
		throw std::runtime_error{ ss.str() };
	}
	auto location_pos = headers_.find("location: ");
	if (location_pos == std::string::npos) {
		throw std::runtime_error{ "unable to get a new location url after redirect" };
	}
	auto newline_pos = headers_.find("\n", location_pos + 10);
	auto location = headers_.substr(location_pos + 10, newline_pos - (location_pos + 10));
	std::erase(location, ' ');
	std::erase(location, '\r');
	std::erase(location, '\n');
	std::cout << http_code << ": " << std::quoted(location) << std::endl;
	
	curl_easy_cleanup(curl_handle_);
}

DownloadFile::~DownloadFile() {
	curl_global_cleanup();
}

size_t DownloadFile::write_data(void *ptr, size_t size, size_t nmemb, void *stream) {
	size_t written = fwrite(ptr, size, nmemb, (FILE *) stream);
	return written;
}

size_t DownloadFile::read_header(char *buffer, size_t size, size_t nitems, void *userdata) {
	std::string *headers = reinterpret_cast<std::string *>(userdata);
	int i = 0;
	for (; i < nitems; ++i) {
		*headers += buffer[i];
	}
	std::cout << *headers << std::endl;

	return i * size;
}
