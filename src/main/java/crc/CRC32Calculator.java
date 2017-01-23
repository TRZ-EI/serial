package crc;

import java.util.zip.Checksum;
import java.util.zip.CRC32;


/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 12/01/17
 * Time: 9.12
 */
public class CRC32Calculator implements CRCCalculator {

    private static CRC32Calculator instance;
    private CRC32Calculator(){

    }
    public static CRC32Calculator getInstance(){
        if (instance == null){
            instance = new CRC32Calculator();
        }
        return instance;
    }
    public long calculateCRC(String message) {
        return this.calculateCRC32(message);
    }
    private long calculateCRC32(String message){
        byte bytes[] = message.getBytes();
        Checksum checksum = new CRC32();
        checksum.update(bytes, 0, bytes.length);
        return checksum.getValue();
    }

}
