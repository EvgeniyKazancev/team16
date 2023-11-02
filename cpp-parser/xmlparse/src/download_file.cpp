#include "download_file.h"

#include <unistd.h>
#include <cstdlib>
#include <stdexcept>

DownloadFile::DownloadFile(const std::string &url, const std::string &filename) {
	auto filename_cstr = filename.c_str();

	curl_global_init(CURL_GLOBAL_ALL);
	curl_handle_ = curl_easy_init();
	curl_easy_setopt(curl_handle_, CURLOPT_URL, url.c_str());
	curl_easy_setopt(curl_handle_, CURLOPT_VERBOSE, 1L);
	curl_easy_setopt(curl_handle_, CURLOPT_NOPROGRESS, 1L);
	curl_easy_setopt(curl_handle_, CURLOPT_WRITEFUNCTION, write_data);

	file_ = fopen(filename_cstr, "wb");
	if (!file_) {
		throw std::runtime_error{ "Can not open file for write" };
	}

	curl_easy_setopt(curl_handle_, CURLOPT_WRITEDATA, file_);
	curl_easy_perform(curl_handle_);
	fclose(file_);
	curl_easy_cleanup(curl_handle_);
}

DownloadFile::~DownloadFile() {
	curl_global_cleanup();
}

size_t DownloadFile::write_data(void *ptr, size_t size, size_t nmemb, void *stream) {
	size_t written = fwrite(ptr, size, nmemb, (FILE *) stream);
	return written;
}
