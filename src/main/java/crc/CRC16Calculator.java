package crc;

import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;

import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 12/01/17
 * Time: 10.10
 */
public class CRC16Calculator implements CRCCalculator {
    private static CRC16Calculator instance;

    private CRC16Calculator(){

    }
    public static CRC16Calculator getInstance(){
        if (instance == null){
            instance = new CRC16Calculator();
        }
        return instance;
    }

    public long calculateCRC(String message) throws NoSuchAlgorithmException {
        return this.calculateCRC16(message);
    }

    private long calculateCRC16(String message) throws NoSuchAlgorithmException {
        // CRC-16/ARC
        AbstractChecksum checksum = JacksumAPI.getChecksumInstance("crc-16");
        checksum.update(message.getBytes());
        return checksum.getValue();
    }
}
