#
# Makefile for RLSH
#
# Under the BSD-3-Clause License, see LICENSE or http://spdx.org/licenses/BSD-3-Clause.html.
#

TEMP_DIR=/tmp
CURRENT_DIR=$(shell pwd)

rlsh:
	./gradlew jar

run:
	./gradlew jar
	bash run.sh
