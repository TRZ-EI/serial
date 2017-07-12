mvn clean compile assembly:single  -- for console version
mvn clean compile assembly:single -Dgui=true -- for gui version


# java -Djava.library.path=/usr/lib/jni -jar serial-1.0-SNAPSHOT-jar-with-dependencies.jar ./config.properties
java -jar trzpoc-gui-rxtx.jar ./config.properties


-Djava.library.path=./serialLib/


 java -Djava.library.path=/home/luigi/svil/domain.luigi/raspberry/TRZMaster/serial/target/trzpoc-gui/serialLib -jar trzpoc-gui-jar-with-dependencies.jar application.properties


java -Djava.library.path=/usr/lib/jni -jar trzpoc-gui-jar-with-dependencies.jar ./application.properties