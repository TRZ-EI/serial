package trzpoc.comunication;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import trzpoc.crc.CRC16CCITT;
import trzpoc.crc.CRCCalculator;
import trzpoc.crc.Crc16CcittKermit;
import trzpoc.structure.serial.MultipleCommandSplitter;
import trzpoc.utils.ConfigurationHolder;

import java.io.IOException;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 26/08/17
 * Time: 14.31
 */
public class SerialDataManager {
    private final int NEW_LINE_ASCII = 10;

    private SerialPort serialPort;
    private SerialCommunicator serialCommunicator;
    private MultipleCommandSplitter multipleCommandSplitter;
    private BlockingQueue<String> serialBuffer;
    private BooleanProperty isDataAvalaible = new SimpleBooleanProperty(false);



    public static SerialDataManager createNewInstance(){
        return new SerialDataManager();
    }

    public static SerialDataManager createNewInstanceByQueue(BlockingQueue<String> serialBuffer) {
        return new SerialDataManager(serialBuffer);
    }

    private SerialDataManager(){
        this.serialBuffer = new LinkedBlockingQueue<>();
        this.multipleCommandSplitter = MultipleCommandSplitter.getNewInstance();
    }

    private SerialDataManager(BlockingQueue<String> serialBuffer){
        this.serialBuffer = serialBuffer;
        this.multipleCommandSplitter = MultipleCommandSplitter.getNewInstance();
    }


    public boolean connectToSerialPort() throws IOException, NoSuchPortException, PortInUseException {
        this.serialCommunicator = new SerialCommunicator();
        this.serialPort = this.serialCommunicator.connectToSerialPort();
        StringBuilder message = new StringBuilder();
        try {
            serialPort.notifyOnDataAvailable(true);
            serialPort.addEventListener((SerialPortEvent serialPortEvent) -> {
                if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                    try {
                        int value = serialPort.getInputStream().available();
                        byte[] data = new byte[value];
                        int read = serialPort.getInputStream().read(data);

                        for (byte b: data){
                            message.append((char)b);
                        }
                        if (message.toString().indexOf(NEW_LINE_ASCII) > 0) {
                            final String[] messagesSent = message.toString().trim().split("\n");
                            message.setLength(0);
                            // ONLY ONE MESSAGE
                            boolean isValid = this.calculateCRC(messagesSent);
                            if (isValid) {
                                this.serialPort.getOutputStream().write(new byte[]{'O', 'K', '\n'});
                                this.serialPort.getOutputStream().flush();
                            }
                            else{
                                this.serialPort.getOutputStream().write(new byte[]{'K', 'O', '\n'});
                                this.serialPort.getOutputStream().flush();

                            }
                            if (messagesSent.length > 0 && isValid) {
                                for (String tempValue: messagesSent){
                                    //tempValue += '\n';
                                    // TO MANAGE MULTIPLE COMMANDS IN A SINGLE ROW
                                    List<String> commands = this.multipleCommandSplitter.splitMultipleCommand(tempValue);
                                    for(String command: commands) {
                                        command += '\n';
                                        this.serialBuffer.add(command);
                                    }
                                }
                                this.isDataAvalaible.set(!this.serialBuffer.isEmpty());
                            }
                        }
                    } catch (Exception e) {
                        String error = message.toString();
                        System.out.println("Failed to read data. (" + e.toString() + ")");
                    }
                }
            });
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
        return this.serialPort != null;
    }

    private boolean calculateCRC(String[] messages) {
        int score = 0;
        int crcDigits = 4; // 4 hex digits
        for (String message: messages){
            int size = message.length();
            if (size > crcDigits){
                String hexCrc = message.substring(size - crcDigits);
                String messageToCalculate = message.substring(0, size - crcDigits);
                CRCCalculator calculator = this.selectCalculator();
                int crc = calculator.calculateCRCForStringMessage(messageToCalculate);
                String crcHex = Integer.toHexString(crc);
                if (crcHex.length() < crcDigits){
                    crcHex = "0" + crcHex;
                }
                score += (hexCrc.equalsIgnoreCase(crcHex))? 1: 0;
            }
        }
        return (score == messages.length);
    }

    private CRCCalculator selectCalculator() {
        String crc = ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.CRC);
        return (crc.equalsIgnoreCase("kermit"))? Crc16CcittKermit.getNewInstance(): CRC16CCITT.getNewInstance();
    }

    public boolean disconnectFromSerialPort(){
        boolean retValue = true;
        if(this.serialPort != null){
            System.out.println("disconnectFromSerialPort()");
            this.serialPort.removeEventListener();
            try {
                this.serialPort.getOutputStream().close();
                this.serialPort.getInputStream().close();
                this.serialPort.close();
            } catch (Exception e) {
                retValue = false;
                e.printStackTrace();
            }
        }
        return retValue;
    }


    public BooleanProperty getIsDataAvalaible() {
        return isDataAvalaible;
    }

    public synchronized BlockingQueue<String> getSerialBuffer() {
        return serialBuffer;
    }

    public void setIsDataAvalaible(boolean value) {
        this.isDataAvalaible.set(value);
    }

}
