#!/bin/bash
# TODO Put this in build.gradle and Makefile
wget -nc "https://wapidstyle.bitbucket.io/wapidstyle/wapi/1.0.6/wapi-1.0.6.jar" -O build/libs/wapi-1.0.6.jar
wget -nc "https://search.maven.org/remotecontent?filepath=org/apache/commons/commons-lang3/3.5/commons-lang3-3.5.jar" -O build/libs/commons-lang3-3.5.jar

java -cp build/libs/wapi-1.0.6.jar:build/libs/commons-lang3-3.5.jar:build/libs/rlsh.jar rlsh.RlshShell
