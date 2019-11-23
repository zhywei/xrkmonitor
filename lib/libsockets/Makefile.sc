# Linux makefile for $(TARGET)
# 编译完成后生成的文件在 Dist 目录下

# This file can be generated by ./gensrclist.sh
include Makefile.srcs
include ../../make_env
VERSION = $(VER_MAJOR).$(VER_MINOR)

# platforms:
#  linux-x86-32
#  linux-x86-64
#  win32-cygwin
#  solaris9-sparc-64
#  macosx
#  solaris8
#PLATFORM = linux-x86-64
#include	Makefile.Defines.$(PLATFORM)

# General configuration variables:
INCDIR = $(MTLIB_INCLUDE_PATH)/Sockets
LIBDIR = $(MTLIB_LIB_PATH)

TARGET  = Sockets
LIBRARIES = -lstdc++ 

# Converts cr/lf to just lf
DOS2UNIX = dos2unix

MODULES = $(SRCS:.c=.o)
MODULES := $(MODULES:.cpp=.o)

CXXFLAGS = $(CXXFLAGS_LIB)
CXXFLAGS += -D_VERSION='"$(VERSION)"'
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

$(TARGET): $(SHAREDLIB) $(STATICLIB)

.c.o:
	$(CC) $(CFLAGS) -c $< -o $@

.cpp.o:
	$(CXX) $(CXXFLAGS) -c $< -o $@

$(STATICLIB): $(MODULES)
	$(AR) r $@ $(MODULES)

$(SHAREDLIB): $(MODULES)
	$(CC) -s -shared -Wl,-soname,$(VERLIBNAME) $(LDFLAGS) -o $@ $(MODULES) $(LIBRARIES)

install:
	rm -fr $(INCDIR)
	install -d $(INCDIR) $(LIBDIR)
	install -m 644 $(INSTALL_INC) $(INCDIR)
	install -m 644 $(STATICLIB) $(LIBDIR)
	install -m 755 $(SHAREDLIB) $(LIBDIR)
	ln -sf $(SHAREDLIB) $(LIBDIR)/$(VERLIBNAME)
	ln -sf $(VERLIBNAME) $(LIBDIR)/$(LIBNAME)	

clean:
	rm -f $(MODULES) $(STATICLIB) $(SHAREDLIB) $(LIBNAME)
	rm -f *.d
	rm -fr $(INCDIR)
	rm -fr $(LIBDIR)/$(TARGET).*
	rm -fr $(LIBDIR)/$(TARGET)-*

