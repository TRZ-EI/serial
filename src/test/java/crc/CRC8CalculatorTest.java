package crc;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 12/01/17
 * Time: 10.55
 */
public class CRC8CalculatorTest {
    CRC8Calculator sut;
    String message = "1P42111";
    int expectedValue = 232;


    @org.testng.annotations.Test
    public void testGetInstance() throws Exception {
        this.sut = CRC8Calculator.getInstance();
        CRC8Calculator sameInstance = CRC8Calculator.getInstance();
        assertNotNull(this.sut);
        assertNotNull(sameInstance);
        assertEquals(sameInstance, this.sut);
    }
    @org.testng.annotations.Test
    public void testCalculateCRC() throws Exception {

        assertEquals(CRC8Calculator.getInstance().calculateCRC(message), expectedValue);
    }

}