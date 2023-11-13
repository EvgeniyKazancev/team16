SRC = src/main.cpp \
	src/download_file.cpp \
	src/converter.cpp \
	src/project_lib.cpp \
	src/config_file.cpp \
	src/parser.cpp \
	src/rssparser.cpp \
	src/newsparser.cpp \
	src/htmlparser.cpp \
	src/tgparser.cpp
BINDIR = bin
TARGET = newsparser
STD = c++20
INCLUDES = -I/usr/include/libxml2/ -I/usr/include/mysql-cppconn-8/
LIBS = -ltinyxml2 -lcurl -lmysqlcppconn8 -lxml2

all:
	mkdir -p ${BINDIR}
	g++ -o ${BINDIR}/${TARGET} --std=${STD} ${INCLUDES} ${SRC} ${LIBS}
