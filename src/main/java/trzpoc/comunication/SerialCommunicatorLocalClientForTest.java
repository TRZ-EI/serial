package trzpoc.comunication;


import gnu.io.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import trzpoc.crc.CRC16CCITT;
import trzpoc.crc.CRC32Calculator;
import trzpoc.crc.CRCCalculator;
import trzpoc.crc.Crc16CcittKermit;
import trzpoc.utils.ConfigurationHolder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TooManyListenersException;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 19/01/17
 * Time: 12.42
 */
public class SerialCommunicatorLocalClientForTest implements SerialCommunicatorInterface, Runnable {

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


    private Properties properties;

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
    private DoubleProperty numericValue = new SimpleDoubleProperty();


    public SerialCommunicatorLocalClientForTest() throws IOException {
        this.init();
    }
    private void init() throws IOException {
        this.properties = ConfigurationHolder.getInstance().getProperties();
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
//        if (this.calculateCRC(s)) {
//            byte[] b = "OK\n".getBytes();
//            this.output.write("OK\n".getBytes());
//            this.output.flush();
//            long endNanoSeconds = System.nanoTime();
//
//            System.out.println("Latency time(micros): " + (endNanoSeconds - startNanoseconds) / 1000);
//        }
          System.out.println(s);
          byte[] b = "OK\n".getBytes();
          this.output.write("OK\n".getBytes());
          this.output.flush();
          long endNanoSeconds = System.nanoTime();
          System.out.println("Latency time(micros): " + (endNanoSeconds - startNanoseconds) / 1000);

    }

    private boolean calculateCRC(String message) {
        boolean retValue = false;
        int crcDigits = 4; // 4 hex digits
        int size = message.length();
        if (size > crcDigits){
            String hexCrc = message.substring(size - crcDigits);
            String messageToCalculate = message.substring(0, size - crcDigits);
            String crcHex = calculateCrCForString(messageToCalculate);
            if (crcHex.length() < crcDigits){
                crcHex = "0" + crcHex;
            }
            retValue = hexCrc.equalsIgnoreCase(crcHex);
        }
        return retValue;
    }

    public String calculateCrCForString(String messageToCalculate) {
        long crc = this.selectCalculator().calculateCRCForStringMessage(messageToCalculate);
        return this.fillHexCodeToFourDigitIfNecessary(crc);
    }
    private CRCCalculator selectCalculator() {
        String crc = ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.CRC);
        return (crc.equalsIgnoreCase("kermit"))? Crc16CcittKermit.getNewInstance(): CRC16CCITT.getNewInstance();
    }

    private String fillHexCodeToFourDigitIfNecessary(long crc) {
        String hexCode = Long.toHexString(crc);
        int len = hexCode.length();
        int zeros = 4 -len;
        if (zeros > 0){
            for (int i = 0; i < zeros; i ++){
                hexCode = "0" + hexCode;
            }
        }
        return hexCode;
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
            for (int i = 0; i < data.length; i++) {
                output.write(data[i]);
                output.flush();
            }
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
    public List<String> readTestScenaryAndProduceDataForTest(String scenary){
        List<String> list = new ArrayList<>();
        String prefix = "serialInputs/scenario";
        String fileName = prefix + scenary + ".txt";
        String realFileName = this.getClass().getClassLoader().getResource(fileName).getFile();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(realFileName));
                String line;
                while ((line = reader.readLine()) != null) {
                    line += this.calculateCrCForString(line) + '\n';
                    list.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;


    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ConfigurationHolder.createSingleInstanceByConfigUri(args[0]);

        SerialCommunicatorLocalClientForTest sc = new SerialCommunicatorLocalClientForTest();
        Thread runner = new Thread(sc);
        runner.start();

        System.out.println("********* EMBEDDED SSL BROKER STARTED *****************");
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        String input = "WRITE  'STOP' TO STOP SERVER ...";
        System.out.println(input);
        while ((input = reader.readLine()).length() != 0) {
            if (input.equalsIgnoreCase("s")) {
                runner.suspend();
                System.out.println("Service interrupted");
            }else{
                runner.resume();
            }
        }




    }

    public double convertStringDataToNumericValue(String s) {
        if (s.length() > 0){
            this.numericValue.set(Double.parseDouble(s));
        }
        return this.numericValue.get();
    }

    public DoubleProperty getNumericValue() {
        return numericValue;
    }

    @Override
    public void run() {




        
        this.connect();
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        String input = "run";

        String clearCommand = "^C" + this.calculateCrCForString("^C") + '\n';
        this.writeData(clearCommand.getBytes());

        String multiConfig = "^V07S31012FV08S13020FV09S130410V0AS130629V0BA11070AV0CP110A0C";
        String multiValues = "^v0700005436v0800005437v0900005438v0A00005439v0B00005440v0C00005441";

        multiConfig += this.calculateCrCForString(multiConfig) + '\n';
        multiValues += this.calculateCrCForString(multiValues) + '\n';
        this.writeData(multiConfig.getBytes());
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.writeData(multiValues.getBytes());





        List<String> lines = this.readTestScenaryAndProduceDataForTest("1");
        for (String line: lines){
            this.writeData(line.getBytes());
        }
        this.waitFor(5000);
        this.writeData(clearCommand.getBytes());

        this.waitFor(5000);
        lines = this.readTestScenaryAndProduceDataForTest("2");
        for (String line: lines){
            this.writeData(line.getBytes());
        }
        this.waitFor(5000);
        this.writeData(clearCommand.getBytes());

        lines = this.readTestScenaryAndProduceDataForTest("3");
        for (String line: lines){
            this.writeData(line.getBytes());
        }
        this.waitFor(5000);
        this.writeData(clearCommand.getBytes());

        String configurationVariable1 = "^V01R310109";
        configurationVariable1 += this.calculateCrCForString(configurationVariable1) + '\n';
        this.writeData(configurationVariable1.getBytes());

        String configurationVariable2 = "^V02R310309";
        configurationVariable2 += this.calculateCrCForString(configurationVariable2) + '\n';
        this.writeData(configurationVariable2.getBytes());

        String configurationVariable3 = "^V03R310509";
        configurationVariable3 += this.calculateCrCForString(configurationVariable3) + '\n';
        this.writeData(configurationVariable3.getBytes());

        String configurationVariable4 = "^V04R310709";
        configurationVariable4 += this.calculateCrCForString(configurationVariable4) + '\n';
        this.writeData(configurationVariable4.getBytes());

        String configurationVariable5 = "^V05R310909";
        configurationVariable5 += this.calculateCrCForString(configurationVariable5) + '\n';
        this.writeData(configurationVariable5.getBytes());

        String configurationVariable6 = "^V06R310B09";
        configurationVariable6 += this.calculateCrCForString(configurationVariable6) + '\n';
        this.writeData(configurationVariable6.getBytes());

        String configurationVariable7 = "^V07R310D09";
        configurationVariable7 += this.calculateCrCForString(configurationVariable7) + '\n';
        this.writeData(configurationVariable7.getBytes());

        String configurationVariable8 = "^V08R310F09";
        configurationVariable8 += this.calculateCrCForString(configurationVariable8) + '\n';
        this.writeData(configurationVariable8.getBytes());


        String valueVariableTemplate = "^v";


        this.waitFor(5000);

        int counter = 1;

        // TODO: REMOVE BEFORE START REAL TEST
        //System.exit(0);
        while (true) {
            this.waitFor(2000);
            //int variableId = counter % 10 + 1;
            String stringValue = Integer.toHexString(counter++);
            int zeros = 8 - stringValue.length();
            for (int index = 1; index <= 8; index ++) {
                String sIndex = Integer.toString(index);
                if (sIndex.length() == 1) {
                    sIndex = "0" + sIndex;
                }

                String valueToTransmit = valueVariableTemplate + sIndex;
                for (int i = 0; i < zeros; i++) {
                    valueToTransmit += "0";
                }
                valueToTransmit += stringValue;
                valueToTransmit += this.calculateCrCForString(valueToTransmit);
                valueToTransmit += '\n';
                //System.out.print("Value tx: " + valueToTransmit);
                this.writeData(valueToTransmit.getBytes());
                this.waitFor(2);
            }

        }
    }

    public void waitFor(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


