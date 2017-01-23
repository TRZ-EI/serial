package crc;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 12/01/17
 * Time: 16.51
 */
public class CRC16CalculatorTest {

    private CRC16Calculator sut;

    @org.testng.annotations.Test
    public void testGetInstance() throws Exception {
        this.sut = CRC16Calculator.getInstance();
        CRC16Calculator sameInstance = CRC16Calculator.getInstance();
        assertNotNull(this.sut);
        assertNotNull(sameInstance);
        assertEquals(sameInstance, this.sut);
    }
    @org.testng.annotations.Test
    public void testCalculateCRC() throws Exception {
        String message = "This is a message";
        long expectedValue = 33659L;
        assertEquals(CRC16Calculator.getInstance().calculateCRC(message), expectedValue);
    }

}