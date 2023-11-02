SRC = src/main.cpp src/download_file.cpp src/converter.cpp
TARGET = xmlparse
STD = c++20
INCLUDES = /usr/include/mysql-cppconn-8
LIBS = -ltinyxml2 -lcurl -lmysqlcppconn8

all:
	g++ -o ${TARGET} --std=${STD} -I${INCLUDES} ${SRC} ${LIBS}
