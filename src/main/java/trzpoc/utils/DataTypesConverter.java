package trzpoc.utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class DataTypesConverter {
    private DataTypesConverter() {
    }

    public byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    public byte[] intToBytes(int x) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(x);
        return buffer.array();
    }
    public byte[] charToByte(char x) {
        ByteBuffer buffer = ByteBuffer.allocate(Character.BYTES);
        buffer.putChar(x);
        return buffer.array();
    }
    public char byteToChar(byte b) {
        return (char)b;
    }

    public String bytesToString(byte[] bytes) throws UnsupportedEncodingException {
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < bytes.length; i ++){
            buffer.append(this.byteToChar(bytes[i]));
        }
        return buffer.toString();
    }

    public int bytesToInt(byte[] bytes) throws UnsupportedEncodingException {
        String toTransform = this.bytesToString(bytes);
        Integer i = Integer.parseInt("1");
        return Integer.parseInt(toTransform, 10);
    }
    public int notAsciiBytesToInt(byte[] bytes) throws UnsupportedEncodingException {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getInt();
    }

    public static DataTypesConverter getNewInstance() {
        return new DataTypesConverter();
    }
}