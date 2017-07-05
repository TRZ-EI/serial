package trzpoc.comunication;


import gnu.io.*;
import trzpoc.crc.CRC32Calculator;

import java.io.FileInputStream;
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
public class SerialCommunicator{

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


    private Properties properties;
    final private String propertiesName = "application.properties";

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

    private SerialWriter writer;


    public SerialCommunicator() throws IOException {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(this.propertiesName);
        this.init(stream);
    }
    public SerialCommunicator(String arg) throws IOException {
        InputStream stream = new FileInputStream(arg);
        this.init(stream);
    }


    private void init(InputStream propertiesStream) throws IOException {
        this.properties = new Properties();
        this.properties.load(propertiesStream);
        propertiesStream.close();
        this.buffer = new StringBuffer();

    }

    public SerialPort connect() {
        String selectedPort = this.properties.getProperty(Keys.PORT.toString());

        try {
            this.serialPort = this.connectToSerialPort();

            //this.serialPort.setLowLatency();

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
        return this.serialPort;
    }

    public SerialPort connectToSerialPort() throws NoSuchPortException, PortInUseException, IOException {
        String selectedPort = this.properties.getProperty(Keys.PORT.toString());

        selectedPortIdentifier = CommPortIdentifier.getPortIdentifier(selectedPort);
        //the method below returns an object of type CommPort
        this.serialPort = (SerialPort) selectedPortIdentifier.open("TRZ-poc", TIMEOUT);
        this.serialPort.disableReceiveTimeout();
        this.setSerialPortParameters();
        return this.serialPort;
    }

    //open the input and output streams
    //pre: an open port
    //post: initialized intput and output streams for use to communicate data
    public boolean initIOStream(){
        boolean successful = false;
        try {
            input = serialPort.getInputStream();
            output = serialPort.getOutputStream();
            this.writer = new SerialWriter(output);
            Thread t = new Thread(this.writer);
            t.setPriority(Thread.MAX_PRIORITY);
            t.start();
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
            serialPort.addEventListener(new SerialReader(this.input, this));
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
            System.out.println("Disconnected.");
        }catch (Exception e){
            System.out.println("Failed to close " + serialPort.getName() + "(" + e.toString() + ")");
        }
    }

    //what happens when data is received
    //pre: serial event is triggered
    //post: processing on the data it reads

    public void manageDataReceivedFromSerialPort(String s) throws IOException {
        long startNanoseconds = System.nanoTime();
        this.output.write(new byte[]{'O','K','\n'});
        this.output.flush();
        long endNanoSeconds = System.nanoTime();

        System.out.println("Latency time(micros): " + (endNanoSeconds - startNanoseconds)/1000);
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
                //this.writer.writeMessage(String.copyValueOf(new char[]{'O', 'K', '\n'}));

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
        SerialCommunicator sc = null;

        if (args.length > 0 && args[0] != null) {
            sc = new SerialCommunicator(args[0]);
        }else{
            sc = new SerialCommunicator();
        }
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
    PORT, BAUD_RATE, SOURCE;
}