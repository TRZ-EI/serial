package crc;

import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 12/01/17
 * Time: 10.07
 */
public interface CRCCalculator {
    public long calculateCRC(String message) throws NoSuchAlgorithmException;
}
