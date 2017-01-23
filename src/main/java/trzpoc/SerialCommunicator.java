package trzpoc;

import crc.CRC32Calculator;
import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.TooManyListenersException;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 19/01/17
 * Time: 12.42
 */
public class SerialCommunicator implements SerialPortEventListener{

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


         private Properties properties;
         final private String propertiesName = "config.properties";

        //input and output streams for sending and receiving data
        private InputStream input;
        private OutputStream output;


        //the timeout value for connecting with the port
        final static int TIMEOUT = 2000;

        //some ascii values for for certain things
        final static int SPACE_ASCII = 32;
        final static int DASH_ASCII = 45;
        final static int NEW_LINE_ASCII = 10;

        //a string for recording what goes on in the program
        //this string is written to the GUI
        String logText = "";

        private CommPortIdentifier selectedPortIdentifier;
        private SerialPort serialPort;
        private StringBuffer buffer;


    public SerialCommunicator() throws IOException {
            this.properties = new Properties();
            InputStream s = this.getClass().getClassLoader().getResourceAsStream(this.propertiesName);
            this.properties.load(s);
            s.close();
            this.buffer = new StringBuffer();
        }


        public void connect() {
            String selectedPort = this.properties.getProperty(Keys.PORT.toString());

            try {
                selectedPortIdentifier = CommPortIdentifier.getPortIdentifier(selectedPort);
                //the method below returns an object of type CommPort
                this.serialPort = (SerialPort) selectedPortIdentifier.open("TRZ-poc", TIMEOUT);
                this.setSerialPortParameters();
                this.initIOStream();
                this.initListener();
                //logging
                logText = selectedPort + " opened successfully.";
                System.out.println(logText);
            }
            catch (NoSuchPortException e){
                logText = selectedPort + " doesn't exist. (" + e.toString() + ")";
                System.out.println(logText);
            }
            catch (PortInUseException e){
                logText = selectedPort + " is in use. (" + e.toString() + ")";
                System.out.println(logText);
            }
            catch (Exception e){
                logText = "Failed to open " + selectedPort + "(" + e.toString() + ")";
                System.out.println(logText);
            }
        }
        //open the input and output streams
        //pre: an open port
        //post: initialized intput and output streams for use to communicate data
        public boolean initIOStream(){
            boolean successful = false;
            try {
                input = serialPort.getInputStream();
                output = serialPort.getOutputStream();
                successful = true;
            } catch (IOException e) {
                logText = "I/O Streams failed to open. (" + e.toString() + ")";
            }
            return successful;
        }
        //starts the event listener that knows whenever data is available to be read
        //pre: an open serial port
        //post: an event listener for the serial port that knows when data is recieved
        public void initListener(){
            try{
                serialPort.addEventListener(this);
                serialPort.notifyOnDataAvailable(true);
            }catch (TooManyListenersException e){
                logText = "Too many listeners. (" + e.toString() + ")";
                System.out.println(logText);
            }
        }
        public void disconnect(){
            try{
                serialPort.removeEventListener();
                serialPort.close();
                input.close();
                output.close();
                logText = "Disconnected.";
                System.out.println(logText);
            }catch (Exception e){
                logText = "Failed to close " + serialPort.getName() + "(" + e.toString() + ")";
                System.out.println(logText);
            }
        }

        //what happens when data is received
        //pre: serial event is triggered
        //post: processing on the data it reads
        public void serialEvent(SerialPortEvent evt) {

            if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE){
                try{


                    byte singleData = (byte)input.read();

                    if (singleData != NEW_LINE_ASCII){
                        this.buffer.append(new String(new byte[] {singleData}));
                    } else {
                        this.calculateChecksumAndSendResponse(buffer.toString());
                        System.out.println(buffer.toString());
                        buffer.setLength(0);

                    }
                } catch (Exception e) {
                    logText = "Failed to read data. (" + e.toString() + ")";
                    System.out.println(logText);
                    buffer.setLength(0);

                }
            }
        }

    private void calculateChecksumAndSendResponse(String s) throws IOException {
            String receveid = s.replace('\r', ' ').trim();
            if (receveid.contains("*")) {
                String delimiter = "*";
                String[] parts = receveid.split(Pattern.quote(delimiter));
                String message = parts[0];
                String checksum = parts[1];
                long checksumValue = Long.valueOf(checksum);

                CRC32Calculator calculator = CRC32Calculator.getInstance();


                long calculatedChecksum = calculator.calculateCRC(message);

                if (checksumValue == calculatedChecksum) {
                    this.output.write(new byte[]{'O', 'K', '\n'});
                    this.output.flush();
                }
            }






    }

    //method that can be called to send data
        //pre: open serial port
        //post: data sent to the other device
        public void writeData(byte[] data) {
            try {
                output.write(data);
                output.flush();
                //this is a delimiter for the data
                output.write(DASH_ASCII);
                output.flush();
            } catch (Exception e) {
                logText = "Failed to write data. (" + e.toString() + ")";
                System.out.println(logText);
            }
        }
        private void setSerialPortParameters() throws IOException {
            String baudRate = this.properties.getProperty(Keys.BAUD_RATE.toString());
            int baudRateValue = Integer.valueOf(baudRate); // 57600bps
            try {
                // Set serial port to 57600bps-8N1..my favourite
                this.serialPort.setSerialPortParams(
                        baudRateValue,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);

                this.serialPort.setFlowControlMode(
                        SerialPort.FLOWCONTROL_NONE);
            } catch (UnsupportedCommOperationException ex) {
                throw new IOException("Unsupported serial port parameter");
            }
        }
        public static void main(String[] args) throws IOException {
            SerialCommunicator sc = new SerialCommunicator();
            sc.connect();
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
            String input = "run";
            while ((input = reader.readLine()).length() != 0) {
                System.out.println(input);
                if (input.equalsIgnoreCase("stop")){
                    sc.disconnect();
                    System.exit(0);
                }
            }
        }
}

enum Keys{
    PORT,BAUD_RATE;
}
