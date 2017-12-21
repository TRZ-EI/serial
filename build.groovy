/* RASPBERRY PARAMS */
//def host = "192.168.1.115"
//def user = "pi"
//def pwd = "raspberry"

/* LAPTOP PARAMS */
def host = "192.168.1.111"
def user = "luigi"
def pwd = "glamdring"


/*
// MAC PARAMS (TO TEST)
def host = "192.168.1.101"
def user = "talamona"
def pwd = "glamdring"
def command = "sshpass -p " + pwd + " ssh " + user + "@" + host + " rm -rf /Users/talamona/trz/trzpoc-gui && rm -f trzpoc-gui-bin.tar.gz"
*/

println "Create console JAR"
def proc = "mvn clean compile assembly:single".execute()
proc.waitForProcessOutput(System.out, System.err);
print System.out

println "Create graphic JAR"
proc = "mvn compile assembly:single -Dgui=true package".execute()
proc.waitForProcessOutput(System.out, System.err);
print System.out


def command = "sshpass -p " + pwd + " ssh " + user + "@" + host + " rm -rf ~/trz/trzpoc-gui && rm -f trzpoc-gui-bin.tar.gz"
println "Delete previous folder and distribution tar"
println command
proc = command.execute()
proc.waitForProcessOutput(System.out, System.err);
print System.out
print System.err

println "Copy and expand distribution tar"
command = "sshpass -p " + pwd + " scp target/trzpoc-gui-bin.tar.gz " + user + "@" + host + ":~/trz/"
//proc = "sshpass -p glamdring scp target/trzpoc-gui-bin.tar.gz talamona@192.168.1.101:~/trz/".execute()
println command
proc = command.execute();
proc.waitForProcessOutput(System.out, System.err);
print System.out
print System.err

command = "sshpass -p " + pwd + " ssh " + user + "@" + host + " cd ~/trz/ && tar -zxvf trzpoc-gui-bin.tar.gz"
println command

//proc = "sshpass -p glamdring ssh talamona@192.168.1.101 cd ~/trz/ && tar -zxvf trzpoc-gui-bin.tar.gz".execute()
proc = command.execute()
proc.waitForProcessOutput(System.out, System.err);
print System.out
print System.err
