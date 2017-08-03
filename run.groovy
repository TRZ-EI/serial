/* RASPBERRY PARAMS
def host = "192.168.188.48"
def user = "pi"
def pwd = "raspberry"
*/

// MAC PARAMS (TO TEST)
def host = "192.168.1.101"
def user = "talamona"
def pwd = "glamdring"

println "Run trzpoc"
command = "sshpass -p " + pwd + " ssh " + user + "@" + host + " cd ~/trz/trzpoc-gui && java -Djava.library.path=./serialLib/ -jar trzpoc-gui-jar-with-dependencies.jar ./application.properties"
// sudo chrt -r 99 $JAVA_HOME/bin/java -Djava.library.path=./serialLib/ -jar trzpoc-gui-jar-with-dependencies.jar ./application.properties
proc = command.execute()
proc.waitForProcessOutput(System.out, System.err);
print System.out
print System.err
