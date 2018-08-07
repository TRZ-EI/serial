#!/bin/bash

BUILD_HOME=/home/pi/trz/trzpoc-console
JAR_TO_RUN=trzpoc-console-jar-with-dependencies.jar

#****************************************************************************************
# Options useful for remote debugging
#****************************************************************************************

DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"

#****************************************************************************************
#cd $BUILD_HOME && sudo java $DEBUG_OPTS -Djava.library.path=/usr/lib/jni/ -jar $JAR_TO_RUN ./application.properties
cd $BUILD_HOME
java -Djava.library.path=/usr/lib/jni/ -jar $JAR_TO_RUN ./application.properties
