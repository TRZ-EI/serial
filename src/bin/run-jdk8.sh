#!/bin/bash

### BEGIN INIT INFO
# Provides:          trz.sh
# Required-Start:    $remote_fs $syslog
# Required-Stop:     $remote_fs $syslog
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Start daemon at boot time
# Description:       Enable service provided by daemon.
### END INIT INFO
BUILD_HOME=/home/pi/trz/trzpoc-gui
JAVA_HOME=/home/pi/jdk1.8.0_144
LIB_HOME=/usr/lib/jni/
#****************************************************************************************
# Options useful for remote debugging
#****************************************************************************************

DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"

#****************************************************************************************
cd $BUILD_HOME 
$JAVA_HOME/bin/java -Djava.library.path=$LIB_HOME -jar trzpoc-gui-jar-with-dependencies.jar ./application.properties &
# cd $BUILD_HOME && sudo $JAVA_HOME/bin/java $DEBUG_OPTS -jar trzpoc-gui-jar-with-dependencies.jar ./application.properties
