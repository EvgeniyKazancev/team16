SRC = src/main.cpp \
	src/download_file.cpp \
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
PREFIX = /usr/local/bin
CONFIG_DIR = /etc
CONFIG_FILE_EXAMPLE = newsparser.conf.example
CONFIG_FILE = newsparser.conf
LIBS = -lcurl -lmysqlcppconn8 -lxml2

all:
	mkdir -p ${BINDIR}
	g++ -o ${BINDIR}/${TARGET} --std=${STD} ${INCLUDES} ${SRC} ${LIBS}

install:
	install ${BINDIR}/${TARGET} ${PREFIX}
	install ${CONFIG_FILE_EXAMPLE} ${CONFIG_DIR}/${CONFIG_FILE}

uninstall:
	rm -rf ${PREFIX}/${TARGET}
	rm -rf ${CONFIG_DIR}/${CONFIG_FILE}

