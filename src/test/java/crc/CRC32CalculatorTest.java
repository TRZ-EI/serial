package crc;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 12/01/17
 * Time: 9.16
 */
public class CRC32CalculatorTest {
    CRC32Calculator sut;

    long expectedValue = 584521497L;

    String message = "5Ev1A4221";

    @org.testng.annotations.Test
    public void testGetInstance() throws Exception {
        this.sut = CRC32Calculator.getInstance();
        CRC32Calculator sameInstance = CRC32Calculator.getInstance();
        assertNotNull(this.sut);
        assertNotNull(sameInstance);
        assertEquals(sameInstance, this.sut);
    }

    @org.testng.annotations.Test
    public void testCalculateCRC() throws Exception {

        assertEquals(CRC32Calculator.getInstance().calculateCRC(message), expectedValue);
    }

}