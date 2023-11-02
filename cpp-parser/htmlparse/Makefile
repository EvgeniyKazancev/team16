SRC = main.cpp
TARGET = newsparser
STD = c++20
LIBS = -ltinyxml2 -lcurl -lmysqlcppconn8 -lxml2
INCLUDES = /usr/include/libxml2/

all: ${SRC}
	g++ --std=${STD} -I${INCLUDES} -o ${TARGET} ${SRC} ${LIBS}
