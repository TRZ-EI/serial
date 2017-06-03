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

        if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE){
            try{
                byte singleData = (byte)input.read();

                if (singleData != NEW_LINE_ASCII){
                    this.buffer.append(new String(new byte[] {singleData}));
                } else {
                    // CALL COMMUNICATOR
                    this.communicator.manageDataReceivedFromSerialPort(this.buffer.toString());
                    buffer.setLength(0);
                    //this.calculateChecksumAndSendResponse(buffer.toString());
                    //System.out.println(buffer.toString());
                }
            } catch (Exception e) {
                System.out.println("Failed to read data. (" + e.toString() + ")");
            }

        }
    }

}
