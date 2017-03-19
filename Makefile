#
# Makefile for RLSH
#
# Under the BSD-3-Clause License, see LICENSE or http://spdx.org/licenses/BSD-3-Clause.html.
#

TEMP_DIR=/tmp
CURRENT_DIR=$(shell pwd)

rlsh:
	./gradlew jar

# TODO Add this to build.gradle
run:
	./gradlew jar
	-wget "https://wapidstyle.bitbucket.io/wapidstyle/wapi/1.0.6/wapi-1.0.6.jar" -O "build/libs/wapi-1.0.6.jar" -nc
	-wget "https://search.maven.org/remotecontent?filepath=org/apache/commons/commons-lang3/3.5/commons-lang3-3.5.jar" \
	-nc -O "build/libs/commons-lang3-3.5.jar"
	java -cp "build/libs/wapi-1.0.6.jar:build/libs/commons-lang3-3.5.jar:build/libs/rlsh.jar" rlsh.RlshShell
