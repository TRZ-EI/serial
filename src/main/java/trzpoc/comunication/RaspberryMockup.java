package trzpoc.comunication;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 11/07/17
 * Time: 11.40
 */

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 19/01/17
 * Time: 12.42
 */
public class RaspberryMockup{
    private SerialCommunicator communicator;
    private String[] messages = {"^tS0104PROVA N 1","^tP0139Prova.","^V07S310146","^tP0152sec","^tP0304Press.  P1","^V08S130315","^tP0321Bar","^tP0330Press.  P2","^V10S130341","^tP0347Bar","^tA0502DP1=    ","^V11A110510","^tA0519mBar","^tP0704S2 = ","^V12P110712","^tP0717mBar","^tP0725S1 = ","^V13P010733","^tP0736mBar","^B17-00000030000000209","^tA1102DP2=    ","^V14A111110","^tA1119mBar","^tP1304S4 = ","^V15P111312","^tP1317mBar","^tP1325S3 = ","^V16P111333","^tP1337mBar","^B18-00000020000000115"};

    public void sendStringsUsingCommunicator() throws IOException {
        communicator = new SerialCommunicator();
        communicator.connect();
        for (int i = 0; i < messages.length; i ++){
            System.out.println(messages[i]);
            communicator.writeData(messages[i].getBytes());
        }
    }
    public static void main(String[] args) throws IOException {
        RaspberryMockup raspberryMockup = new RaspberryMockup();
        raspberryMockup.sendStringsUsingCommunicator();
    }
}

