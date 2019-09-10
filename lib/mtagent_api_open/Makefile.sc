# Linux makefile for $(TARGET)
# 编译完成后生成的文件在 Dist 目录下

# This file can be generated by ./gensrclist.sh
include Makefile.srcs
include ../../make_env

# General configuration variables:
INCDIR = $(MTLIB_INCLUDE_PATH)/mtagent_api_open
LIBDIR = $(MTLIB_LIB_PATH)
INCLUDE += -I$(MYSQL_INCLUDE)/mysql

TARGET  = mtreport_api_open
LIBRARIES = 

# Converts cr/lf to just lf
DOS2UNIX = dos2unix

MODULES = $(SRCS:.c=.o)
MODULES := $(MODULES:.cpp=.o)

CXXFLAGS = $(CXXFLAGS_LIB)
CXXFLAGS += $(INCLUDE)

STATICLIB = lib$(TARGET).a
SHAREDLIB = lib$(TARGET)-$(VER_MAJOR).$(VER_MINOR).so
LIBNAME	= lib$(TARGET).so
VERLIBNAME = $(LIBNAME).$(VER_MAJOR)

default: all

all: dist

dist: $(TARGET)

dos2unix:
	@$(DOS2UNIX) $(SRCS) $(INCLS)

$(TARGET): $(STATICLIB)

.c.o:
	$(CC) $(CFLAGS) -c $< -o $@

.cpp.o:
	$(CXX) $(CXXFLAGS) -c $< -o $@

$(STATICLIB): $(MODULES)
	$(AR) r $@ $(MODULES)

$(SHAREDLIB): $(MODULES)
	$(CC) -s -shared -Wl,-soname,$(VERLIBNAME) $(LDFLAGS) -o $@ $(MODULES) $(LIBRARIES)

install:
	install -d $(INCDIR) $(LIBDIR)
	install -m 644 $(INSTALL_INC) $(INCDIR)
	install -m 644 $(STATICLIB) $(LIBDIR)

clean:
	rm -f  $(MODULES) $(STATICLIB) $(LIBNAME)

