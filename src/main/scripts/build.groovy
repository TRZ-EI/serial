println "Create console JAR"
def proc = "mvn clean compile assembly:single".execute()
proc.waitForProcessOutput(System.out, System.err);
print System.out

println "Create graphic JAR"
proc = "mvn compile assembly:single -Dgui=true package".execute()
proc.waitForProcessOutput(System.out, System.err);
print System.out

println "Delete previous folder and distribution tar"
proc = "sshpass -p glamdring ssh talamona@192.168.1.101 rm -rf /Users/talamona/trz/trzpoc-gui && rm -f trzpoc-gui-bin.tar.gz".execute()
proc.waitForProcessOutput(System.out, System.err);
print System.out
print System.err

println "Copy and expand distribution tar"
proc = "sshpass -p glamdring scp target/trzpoc-gui-bin.tar.gz talamona@192.168.1.101:~/trz/".execute()
proc.waitForProcessOutput(System.out, System.err);
print System.out
print System.err

proc = "sshpass -p glamdring ssh talamona@192.168.1.101 cd ~/trz/ && tar -zxvf trzpoc-gui-bin.tar.gz".execute()
proc.waitForProcessOutput(System.out, System.err);
print System.out
print System.err

