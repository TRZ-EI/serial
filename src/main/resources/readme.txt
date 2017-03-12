mvn clean compile assembly:single  -- for console version
mvn clean compile assembly:single -Dgui=true -- for gui version


java -Djava.library.path=/usr/lib/jni -jar serial-1.0-SNAPSHOT-jar-with-dependencies.jar ./config.properties

