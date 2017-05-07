package trzpoc.utils;

import trzpoc.crc.CRC16CCITT;
import trzpoc.structure.serial.VariableConfiguratorSerialDataParser;

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

    public byte[] prepareDataToConfigureAVariable(String varNumber, char font_colore1, char num_caratteri1, char decimali1, String riga1, String colonna1){
        

        List<Byte> buffer = new ArrayList<Byte>();
        buffer.add(Byte.valueOf("5E", 16));
        buffer.add(new Byte((byte)'V'));
        /*varNumber*/


        varNumber = this.prependWithAsciiZeroIfNecessary(varNumber);
        for (int i = 0; i < varNumber.length(); i ++) {
            buffer.add(Byte.valueOf(this.convertADigitCharToHex(varNumber.charAt(i))));
        }
        /*font_colore*/ buffer.add(new Byte((byte)font_colore1));
        /*num_caratteri1*/buffer.add(this.convertADigitCharToHex(num_caratteri1));
        /*decimali1*/ buffer.add(this.convertADigitCharToHex(decimali1));
        /*riga1*/
        riga1 = this.prependWithAsciiZeroIfNecessary(riga1);
        for (int i = 0; i < riga1.length(); i ++) {
            buffer.add(Byte.valueOf(this.convertADigitCharToHex(riga1.charAt(i))));
        }
        /*colonna1*/
        colonna1 = this.prependWithAsciiZeroIfNecessary(colonna1);
        for (int i = 0; i < colonna1.length(); i ++) {
            buffer.add(Byte.valueOf(this.convertADigitCharToHex(colonna1.charAt(i))));
        }
        return this.addCRCValueAndEndOfRow(buffer);


    }

    private String prependWithAsciiZeroIfNecessary(String value) {
        return (value.length() == 1)? "0" + value: value;
    }

    public byte[] prepareDataToTransmitAVariable(String variableId, long variableValue){
        List<Byte> buffer = new ArrayList<Byte>();
        buffer.add(Byte.valueOf("5E", 16));
        buffer.add(Byte.valueOf((byte)'v'));


        /*variableId*/
        variableId = (variableId.length() == 1)? "0" + variableId: variableId;
        for (int i = 0; i < variableId.length(); i ++) {
            buffer.add(Byte.valueOf(this.convertADigitCharToHex(variableId.charAt(i))));
        }


        /*variableValue*/
        byte[] convertedValue = this.convertLongToByteArray(variableValue);
        for (int i = 0; i < convertedValue.length; i ++) {
            buffer.add(Byte.valueOf(convertedValue[i]));
        }
        return this.addCRCValueAndEndOfRow(buffer);
    }
    public byte[] prepareDataToTransmitAText(char fontColor, String row, String column, String text){
        List<Byte> buffer = new ArrayList<Byte>();
        buffer.add(Byte.valueOf("5E", 16));
        buffer.add(Byte.valueOf((byte)'t'));

        /*fontColor*/ buffer.add(new Byte((byte)fontColor));

        /*row*/
        row = this.prependWithAsciiZeroIfNecessary(row);
        for (int i = 0; i < row.length(); i ++) {
            buffer.add(Byte.valueOf(this.convertADigitCharToHex(row.charAt(i))));
        }
        /*colum*/
        column = this.prependWithAsciiZeroIfNecessary(column);
        for (int i = 0; i < column.length(); i ++) {
            buffer.add(Byte.valueOf(this.convertADigitCharToHex(column.charAt(i))));
        }
        this.addTextToByteBuffer(buffer, text);
        return this.addCRCValueAndEndOfRow(buffer);
    }

    private void addTextToByteBuffer(List<Byte> buffer, String text) {
        char[] charArray = text.toCharArray();
        for (int i = 0; i < charArray.length; i ++){
            buffer.add(this.convertADigitCharToHex(charArray[i]));
        }
    }

    private byte[] addCRCValueAndEndOfRow(List<Byte> buffer) {
        byte[] crc = this.calculateCRC(this.prepareByteArray(buffer));
        for (int i = 0; i < crc.length; i ++){
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
        byte[] bytes = ByteBuffer.allocate(Integer.BYTES).putInt(crc).array();
        for (byte b : bytes) {
            System.out.format("0x%x ", b);
        }
        return bytes;
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
        for (int i = 0; i < buffer.size(); i ++){
            retValue[i] = a[i].byteValue();
        }
        return retValue;
    }
    private byte convertADigitToAscii(char digit){
       return (byte)digit;
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
        byte[] configureVariableInfo = sdm.prepareDataToConfigureAVariable("12", 'C', '2', '4',"10", "01");

        VariableConfiguratorSerialDataParser dp = VariableConfiguratorSerialDataParser.getNewInstance();
        dp.readByteArray(configureVariableInfo);



        byte[] setVariableInfo = sdm.prepareDataToTransmitAVariable("12",1000L);

        byte[] varValue = Arrays.copyOfRange(setVariableInfo, 4, 12);
        long converted = sdm.bytesToLong(varValue);


        System.out.println("");


    }

}
