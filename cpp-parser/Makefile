SRC = src/main.cpp \
	src/download_file.cpp \
	src/converter.cpp \
	src/project_lib.cpp \
	src/config_file.cpp \
	src/iparser.cpp \
	src/rssparser.cpp \
	src/newsparser.cpp
TARGET = newsparser
STD = c++20
INCLUDES = /usr/include/mysql-cppconn-8
LIBS = -ltinyxml2 -lcurl -lmysqlcppconn8

all:
	g++ -o ${TARGET} --std=${STD} -I${INCLUDES} ${SRC} ${LIBS}
