TO BUILD
-- for console version
1)mvn clean compile assembly:single
-- to obtain distribution package
2)mvn compile assembly:single -Dgui=true package -- to obtain distribution package

TO RUN
for Raspberry: -Djava.library.path=/usr/lib/jni
for Test: -Djava.library.path=./serialLib/

GRAPHICAL INTERFACE
a) load TRZ-send-analog-data-toJFX-potenziometers.ino
   /media/luigi/SAMSUNG/talamonasoftware/lavori/TRZ/progetti/Arduino/TRZ-send-analog-data-toJFX-potenziometers

b)          sudo chrt -r 99 $JAVA_HOME/bin/java -Djava.library.path=./serialLib/ -jar trzpoc-gui-jar-with-dependencies.jar ./application.properties

raspberry)  sudo chrt -r 99 $JAVA_HOME/bin/java -Djava.library.path=/usr/lib/jni/ -jar trzpoc-gui-jar-with-dependencies.jar ./application.properties

TEST FOR VELOCITY RESPONSE
a) load TRZ-send-commands-serial-receive-response-event-infinity.ino
   /media/luigi/SAMSUNG/talamonasoftware/lavori/TRZ/progetti/Arduino/TRZ-send-commands-serial-receive-response-event-infinity

b)          sudo chrt -r 99 $JAVA_HOME/bin/java -Djava.library.path=./serialLib/ -jar trzpoc-console-jar-with-dependencies.jar ./application.properties

raspberry)  sudo chrt -r 99 $JAVA_HOME/bin/java -Djava.library.path=/usr/lib/jni/ -jar trzpoc-console-jar-with-dependencies.jar ./application.properties

JARS
trzpoc-console-jar-with-dependencies.jar
trzpoc-gui-jar-with-dependencies.jar


To find hosts on network
****************************************************
nmap -sn 192.168.188.0/24

-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005

java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -Djava.library.path=/usr/lib/jni/ -jar trzpoc-gui-jar-with-dependencies.jar ./application.properties



