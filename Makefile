#
# Makefile for RLSH
#
# Under the BSD-3-Clause License, see LICENSE or http://spdx.org/licenses/BSD-3-Clause.html.
#

TEMP_DIR=/tmp
CURRENT_DIR=$(shell pwd)
SPHINX=sphinx-build

COMPILE=YES

ifeq ($(COMPILE),YES)
	COMPILER=./gradlew jar
else
	COMPILER=@echo 'Compiling Disabled!'
endif

.PHONY: docs

rlsh:
	$(COMPILER)

# TODO Add this to build.gradle
run:
	$(COMPILER)
	-wget "https://wapidstyle.bitbucket.io/wapidstyle/wapi/1.0.6/wapi-1.0.6.jar" -O "build/libs/wapi-1.0.6.jar" -nc
	-wget "https://search.maven.org/remotecontent?filepath=org/apache/commons/commons-lang3/3.5/commons-lang3-3.5.jar"  \
	-nc -O "build/libs/commons-lang3-3.5.jar"
	-wget "https://search.maven.org/remotecontent?filepath=commons-io/commons-io/2.5/commons-io-2.5.jar" -nc \
	-O "build/libs/commons-io-2.5.jar"
	java -cp "build/libs/wapi-1.0.6.jar:build/libs/commons-lang3-3.5.jar:build/libs/rlsh.jar:build/libs/commons-io-2.5.jar" rlsh.RlshShell

docs:
	$(SPHINX) "docs" "docs/built"
	$(SPHINX) -b man "docs" "docs/built"
