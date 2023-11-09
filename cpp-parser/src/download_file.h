#pragma once
#include <string>

extern "C" {
	#include <curl/curl.h>
}

class DownloadFile {
public:
	DownloadFile() = delete;
	DownloadFile(const std::string &url, const std::string &filename, unsigned short max_redirects = 5);
	~DownloadFile();
	std::string &contentType() const;

private:
	static size_t write_data(void *ptr, size_t size, size_t nmemb, void *stream);
	static size_t read_header(char *buffer, size_t size, size_t nitems, void *userdata);
	CURL *curl_handle_;
	FILE *file_;
	std::string content_type_;
	std::string headers_;
};

