package trzpoc.comunication;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.InputStream;



/**
 * Handles the input coming from the serial port. A new line character
 * is treated as the end of a block in this example.
 */
public class SerialReader implements SerialPortEventListener {


    private InputStream input;
    private SerialCommunicator communicator;
    private StringBuilder buffer;

    //some ascii values for for certain things
    private final int SPACE_ASCII = 32;
    private final int DASH_ASCII = 45;
    private final int NEW_LINE_ASCII = 10;


    public SerialReader (InputStream in, SerialCommunicator communicator) {
        this.input = in;
        this.communicator = communicator;
        this.buffer = new StringBuilder();
    }

    public void serialEvent(SerialPortEvent evt) {
        StringBuilder message = new StringBuilder();
        if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE){
            try{
                int value = this.input.available();
                byte[] data = new byte[value];
                int read = this.input.read(data);
                for (byte b: data){
                    message.append((char)b);
                }
                if (message.toString().indexOf(NEW_LINE_ASCII) > 0) {
                    String[] values = message.toString().split("\n");
                    message.setLength(0);
                    for (int i = 0; i < values.length; i ++) {
                        System.out.println("RECEIVED: " + values[i]);
                        this.communicator.convertStringDataToNumericValue(values[i]);
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed to read data. (" + e.toString() + ")");
            }

        }
    }

}

