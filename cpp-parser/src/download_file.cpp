#include "download_file.h"
#include "project_lib.h"

#include <stdexcept>
#include <iostream>
#include <string>
#include <functional>
#include <sstream>
#include <iomanip>
#include <algorithm>
#include <thread>
#include <chrono>

extern "C" {
	#include <cstdlib>
	#include <unistd.h>
}

bool DownloadFile::success() const {
	return !error_;
}

long DownloadFile::getHttpCode() const {
	return http_code_;
}

const std::string &DownloadFile::getError() const {
	return error_string_;
}

const std::string &DownloadFile::getContentType() const {
	return content_type_;
}

const std::string &DownloadFile::getCharset() const {
	return charset_;
}

DownloadFile::DownloadFile() {
	curl_global_init(CURL_GLOBAL_ALL);
}

void DownloadFile::download(const std::string &url, const std::string &filename, const unsigned short max_redirects) {
	using namespace std::literals::chrono_literals;
	std::string current_url{ url };

	std::this_thread::sleep_for(500ms);
	for (unsigned short attemp_number = 0; attemp_number < max_redirects; ++attemp_number) {
		headers_.clear();
		auto filename_cstr = filename.c_str();

		std::cout << "URL: " << current_url << " Attemp:" << attemp_number << " Max Redirects:" << max_redirects << std::endl;
		curl_handle_ = curl_easy_init();
		curl_easy_setopt(curl_handle_, CURLOPT_URL, current_url.c_str());
		curl_easy_setopt(curl_handle_, CURLOPT_VERBOSE, 0L);
		curl_easy_setopt(curl_handle_, CURLOPT_NOPROGRESS, 1L);
		curl_easy_setopt(curl_handle_, CURLOPT_WRITEFUNCTION, write_data);
		curl_easy_setopt(curl_handle_, CURLOPT_HEADERFUNCTION, read_header);
		curl_easy_setopt(curl_handle_, CURLOPT_HEADERDATA, &headers_);
		curl_easy_setopt(
			curl_handle_,
			CURLOPT_USERAGENT,
			"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36 Edg/119.0.0.0"
		);

		file_ = fopen(filename_cstr, "wb");
		if (!file_) {
			error_ = true;
			error_string_ = "unable to open file for write";
		}

		curl_easy_setopt(curl_handle_, CURLOPT_WRITEDATA, file_);
		curl_easy_perform(curl_handle_);
		fclose(file_);
		char *ct = nullptr;
		auto res = curl_easy_getinfo(curl_handle_, CURLINFO_CONTENT_TYPE, &ct);
		if (res == 0 && ct != nullptr) {
			std::string temp{ ct };
			content_type_ = ct;
			auto charset_pos = content_type_.find("charset");
			if (charset_pos != std::string::npos) {
				auto equal_pos = content_type_.find('=', charset_pos);
				if (equal_pos != std::string::npos) {
					charset_ = content_type_.substr(equal_pos + 1, content_type_.length() - (equal_pos + 1));
					Lib::trim(charset_);
				}
			}
			auto semicolon_pos = content_type_.find(';');
			if (semicolon_pos != std::string::npos) {
				content_type_ = content_type_.substr(0, semicolon_pos);
			}

		}
		http_code_ = 0;
		curl_easy_getinfo(curl_handle_, CURLINFO_RESPONSE_CODE, &http_code_);
		if (http_code_ == 200) {	// success
			curl_easy_cleanup(curl_handle_);
			return;
		}
		if (http_code_ == 404) {
			error_ = true;
			error_string_ = "not found";
			return;
		}
		if (http_code_ == 403) {
			error_ = true;
			error_string_ = "forbidden";
			return;
		}
		if (http_code_ != 301 && http_code_ != 302) {
			error_ = true;
			error_string_ = "unknown error";
			return;
		}
		// follow redirect
		auto location_pos = headers_.find("location: ");
		if (location_pos == std::string::npos) {
			error_ = true;
			error_string_ = "unable to get a new location after redirect";
			return;
		}
		auto newline_pos = headers_.find("\n", location_pos + 10);
		auto location = headers_.substr(location_pos + 10, newline_pos - (location_pos + 10));
		Lib::trim(location);
		//std::cout << "Headers: " << headers_;
		//std::cout << "New location: " << location << '\n' << headers_.substr(location_pos, 100) << std::endl;
		if (location.substr(0, 7) == "http://" || location.substr(0, 8) == "https://") {
			current_url = location;
		}
		else if (location[0] == '/') {
			auto proto_pos = current_url.find("://");
			auto domain_pos = current_url.find('/', proto_pos + 3);
			current_url = current_url.substr(0, domain_pos) + location;
		}
		else {
			auto last_slash_pos = current_url.rfind('/');
			current_url = current_url.substr(0, last_slash_pos + 1) + location;
		}
		//std::cout << http_code_ << ": " << std::quoted(location) << " " << std::quoted(current_url) << std::endl;
	
		curl_easy_cleanup(curl_handle_);
	}

	error_ = true;
	error_string_ = "too many redirects";
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

	return i * size;
}
