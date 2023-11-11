#pragma once
#include <string>

extern "C" {
	#include <curl/curl.h>
}

class DownloadFile {
public:
	DownloadFile();
	DownloadFile(const DownloadFile &other) = delete;
	DownloadFile &operator=(const DownloadFile &other) = delete;
	void download(const std::string &url, const std::string &filename, unsigned short max_redirects = 5);
	~DownloadFile();
	std::string &contentType() const;
	bool success() const;
	long getHttpCode() const;
	const std::string &getError() const;
	const std::string &getContentType() const;
	const std::string &getCharset() const;

private:
	static size_t write_data(void *ptr, size_t size, size_t nmemb, void *stream);
	static size_t read_header(char *buffer, size_t size, size_t nitems, void *userdata);
	CURL *curl_handle_;
	FILE *file_;
	std::string content_type_;
	std::string charset_;
	std::string headers_;
	bool error_{false };
	long http_code_;
	std::string error_string_;
};

