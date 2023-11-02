
#include <string>

#include <curl/curl.h>

class DownloadFile {
public:
	DownloadFile() = delete;
	DownloadFile(const std::string &url, const std::string &filename);
	~DownloadFile();
private:
	static size_t write_data(void *ptr, size_t size, size_t nmemb, void *stream);
	CURL *curl_handle_;
	FILE *file_;
};

