package trzpoc.crc;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 12/01/17
 * Time: 10.07
 */
public interface CRCCalculator {
    public int calculateCRCForStringMessage(String message);
    public int calculateCRCforByteArrayMessage(byte[] message);
}