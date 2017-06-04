package trzpoc.utils;

import com.opencsv.CSVReader;
import trzpoc.crc.CRC16CCITT;
import trzpoc.structure.serial.SerialDataFacade;
import trzpoc.structure.serial.VariableConfiguratorSerialDataParser;

import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 23/03/17
 * Time: 11.38
 */
public class SerialDataMock {


    private final Byte fineStringa = Byte.valueOf("0A", 16).byteValue();
    private final DataTypesConverter dataTypesConverter = DataTypesConverter.getNewInstance();

    private SerialDataFacade serialDataFacade;

    public SerialDataMock(SerialDataFacade serialDataFacade) {
        this.serialDataFacade = serialDataFacade;
    }

    public SerialDataMock() {
    }

    public static SerialDataMock getNewInstanceBySerialDataFacade(SerialDataFacade serialDataFacade) {
        return new SerialDataMock(serialDataFacade);
    }

    public void readData() {
        String realFileName = this.getClass().getClassLoader().getResource("inputExamples.csv").getFile();
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(realFileName));
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (!line[0].startsWith("#")) {
                    byte[] dataReceived = this.simulateSerialInput(line);
                    this.serialDataFacade.onSerialDataInput(dataReceived);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private byte[] simulateSerialInput(String[] line) throws UnsupportedEncodingException {
        String command = line[1];
        byte[] retValue = null;


        if (command.equals("V")) {
            String id = line[2];
            char fontColor = line[3].charAt(0);
            char integerLenght = line[4].charAt(0);
            char decimalLenght = line[5].charAt(0);
            String row = line[6];
            String column = line[7];
            retValue = this.prepareDataToConfigureAVariable(id, fontColor, integerLenght, decimalLenght, row, column);
        } else if (command.equals("v")) {
            String id = line[2];
            String value = line[3];
            retValue = this.prepareDataToTransmitAVariable(id, Long.parseLong(value));
        } else if (command.equals("t")) {
            //TextSerialDataParser sdp = TextSerialDataParser.getNewInstance();
            retValue = this.prepareDataToTransmitAText(line[2].charAt(0), line[3], line[4], line[5]);
        } else if (command.equals("C")) {
            // TODO: complete with clear display
        } else if (command.equals("B")) {
            retValue = this.prepareDataToTransmitABar(line[2], Long.parseLong(line[3]), Long.parseLong(line[4]), line[5]);
        }
        return retValue;
    }


    public byte[] prepareDataToConfigureAVariable(String varNumber, char font_colore1, char num_caratteri1, char decimali1, String riga1, String colonna1) {
        List<Byte> buffer = new ArrayList<Byte>();
        buffer.add(Byte.valueOf("5E", 16));
        buffer.add(new Byte((byte) 'V'));
        /*varNumber*/
        varNumber = this.prependWithAsciiZeroIfNecessary(varNumber);
        for (int i = 0; i < varNumber.length(); i++) {
            buffer.add(Byte.valueOf(this.convertADigitCharToHex(varNumber.charAt(i))));
        }
        /*font_colore*/
        buffer.add(new Byte((byte) font_colore1));
        /*num_caratteri1*/
        buffer.add(this.convertADigitCharToHex(num_caratteri1));
        /*decimali1*/
        buffer.add(this.convertADigitCharToHex(decimali1));
        /*riga1*/
        riga1 = this.prependWithAsciiZeroIfNecessary(riga1);
        for (int i = 0; i < riga1.length(); i++) {
            buffer.add(Byte.valueOf(this.convertADigitCharToHex(riga1.charAt(i))));
        }
        /*colonna1*/
        colonna1 = this.prependWithAsciiZeroIfNecessary(colonna1);
        for (int i = 0; i < colonna1.length(); i++) {
            buffer.add(Byte.valueOf(this.convertADigitCharToHex(colonna1.charAt(i))));
        }
        return this.addCRCValueAndEndOfRow(buffer);


    }

    private String prependWithAsciiZeroIfNecessary(String value) {
        return (value.length() == 1) ? "0" + value : value;
    }

    public byte[] prepareDataToTransmitAVariable(String variableId, long variableValue) {
        List<Byte> buffer = new ArrayList<Byte>();
        buffer.add(Byte.valueOf("5E", 16));
        buffer.add(Byte.valueOf((byte) 'v'));


        /*variableId*/
        variableId = (variableId.length() == 1) ? "0" + variableId : variableId;
        for (int i = 0; i < variableId.length(); i++) {
            buffer.add(Byte.valueOf(this.convertADigitCharToHex(variableId.charAt(i))));
        }


        /*variableValue*/
        byte[] convertedValue = this.convertLongToByteArray(variableValue);
        for (int i = 0; i < convertedValue.length; i++) {
            buffer.add(Byte.valueOf(convertedValue[i]));
        }
        return this.addCRCValueAndEndOfRow(buffer);
    }

    public byte[] prepareDataToTransmitAText(char fontColor, String row, String column, String text) {
        List<Byte> buffer = new ArrayList<Byte>();
        buffer.add(Byte.valueOf("5E", 16));
        buffer.add(Byte.valueOf((byte) 't'));

        /*fontColor*/
        buffer.add(new Byte((byte) fontColor));

        /*row*/
        row = this.prependWithAsciiZeroIfNecessary(row);
        for (int i = 0; i < row.length(); i++) {
            buffer.add(Byte.valueOf(this.convertADigitCharToHex(row.charAt(i))));
        }
        /*colum*/
        column = this.prependWithAsciiZeroIfNecessary(column);
        for (int i = 0; i < column.length(); i++) {
            buffer.add(Byte.valueOf(this.convertADigitCharToHex(column.charAt(i))));
        }
        this.addTextToByteBuffer(buffer, text);
        return this.addCRCValueAndEndOfRow(buffer);
    }

    public byte[] prepareDataToTransmitABar(String variableId, long minValue, long maxValue, String row) {
        List<Byte> buffer = new ArrayList<Byte>();
        buffer.add(Byte.valueOf("5E", 16));
        buffer.add(Byte.valueOf((byte) 'B'));
        /*variableId*/
        variableId = (variableId.length() == 1) ? "0" + variableId : variableId;
        for (int i = 0; i < variableId.length(); i++) {
            buffer.add(Byte.valueOf(this.convertADigitCharToHex(variableId.charAt(i))));
        }
        /* minValue */
        byte[] convertedMinValue = this.convertLongToByteArray(minValue);
        for (int i = 0; i < convertedMinValue.length; i++) {
            buffer.add(Byte.valueOf(convertedMinValue[i]));
        }
        /* maxValue */
        byte[] convertedMaxValue = this.convertLongToByteArray(maxValue);
        for (int i = 0; i < convertedMaxValue.length; i++) {
            buffer.add(Byte.valueOf(convertedMaxValue[i]));
        }
        /* row */
        row = this.prependWithAsciiZeroIfNecessary(row);
        for (int i = 0; i < row.length(); i++) {
            buffer.add(Byte.valueOf(this.convertADigitCharToHex(row.charAt(i))));
        }
        return this.addCRCValueAndEndOfRow(buffer);
    }

    private void addTextToByteBuffer(List<Byte> buffer, String text) {
        char[] charArray = text.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            buffer.add(this.convertADigitCharToHex(charArray[i]));
        }
    }

    private byte[] addCRCValueAndEndOfRow(List<Byte> buffer) {
        byte[] crc = this.calculateCRC(this.prepareByteArray(buffer));
        for (int i = 0; i < crc.length; i++) {
            buffer.add(Byte.valueOf(crc[i]));
        }
        buffer.add(fineStringa);
        return prepareByteArray(buffer);
    }

    private byte[] calculateCRC(byte[] bytes) {
        int crc = CRC16CCITT.getNewInstance().calculateCRCforByteArrayMessage(bytes);
        byte[] convertedCrc = this.convertIntToByteArray(crc);

        return convertedCrc;
    }

    private byte[] convertIntToByteArray(int crc) {
        return ByteBuffer.allocate(Integer.BYTES).putInt(crc).array();
/*
            return new byte[] {
                    (byte)(crc >>> 24),
                    (byte)(crc >>> 16),
                    (byte)(crc >>> 8),
                    (byte)crc};
*/
    }

    private byte[] convertLongToByteArray(long value) {
        return dataTypesConverter.longToBytes(value);
    }

    private byte[] prepareByteArray(List<Byte> buffer) {
        byte[] retValue = new byte[buffer.size()];
        Byte[] a = buffer.toArray(new Byte[0]);
        for (int i = 0; i < buffer.size(); i++) {
            retValue[i] = a[i].byteValue();
        }
        return retValue;
    }

    private byte convertADigitToAscii(char digit) {
        return (byte) digit;
    }


    private byte convertADigitCharToHex(char digit) {
        return this.convertADigitToAscii(digit);
    }

    /*
    public int bytesToInt(byte[] intBytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(intBytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        int retValue = ois.readInt();
        ois.close();
        return retValue;
    }
    */
    public byte[] longToBytes(long x) {
        return dataTypesConverter.longToBytes(x);
    }

    public long bytesToLong(byte[] bytes) {
        return dataTypesConverter.bytesToLong(bytes);
    }

    public byte[] intToBytes(int x) {
        return dataTypesConverter.intToBytes(x);
    }

    public int bytesToInt(byte[] bytes) throws UnsupportedEncodingException {
        return dataTypesConverter.bytesToInt(bytes);
    }

    public static void main(String[] args) throws IOException {
        SerialDataMock sdm = new SerialDataMock();
        byte[] configureVariableInfo = sdm.prepareDataToConfigureAVariable("12", 'C', '2', '4', "10", "01");

        VariableConfiguratorSerialDataParser dp = VariableConfiguratorSerialDataParser.getNewInstance();
        dp.readByteArray(configureVariableInfo);


        byte[] setVariableInfo = sdm.prepareDataToTransmitAVariable("12", 1000L);

        byte[] varValue = Arrays.copyOfRange(setVariableInfo, 4, 12);
        long converted = sdm.bytesToLong(varValue);
    }

}
