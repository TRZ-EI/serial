#!/bin/bash

BUILD_HOME=/home/pi/trz/trzpoc-gui
JAVA_HOME=/home/pi/jre-9.0.4
#****************************************************************************************
# Options useful for remote debugging
#****************************************************************************************

DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
#****************************************************************************************
cd $BUILD_HOME 
# for debug
# $JAVA_HOME/bin/java $DEBUG_OPTS -jar trzpoc-gui-jar-with-dependencies.jar ./application.properties 
# for production
$JAVA_HOME/bin/java -jar trzpoc-gui-jar-with-dependencies.jar ./application.properties &
