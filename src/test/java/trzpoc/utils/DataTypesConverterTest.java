package trzpoc.utils;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 14/04/17
 * Time: 12.39
 */
public class DataTypesConverterTest {
    private DataTypesConverter sut;

    @DataProvider
    private Object[][] dataToTestBytesToInt(){
        return new Object[][]{
                {new byte[]{0x33, 0x34, 0x35, 0x36, 0x37, 0x38}, 345678}, // HEX values
                {new byte[]{57}, 9}, // ASCII VALUES (09)
                {new byte[]{56}, 8}, // ASCII VALUES (08)
                {new byte[]{55}, 7}, // ASCII VALUES (07)
                {new byte[]{54}, 6}, // ASCII VALUES (06)
                {new byte[]{53}, 5}, // ASCII VALUES (05)
                {new byte[]{52}, 4}, // ASCII VALUES (04)
                {new byte[]{51}, 3}, // ASCII VALUES (03)
                {new byte[]{50}, 2}, // ASCII VALUES (02)
                {new byte[]{49}, 1}, // ASCII VALUES (01)
                {new byte[]{48,49}, 1}, // ASCII VALUES (01)
                {new byte[]{49, 0x39}, 19} // MIXED ASCII/HEX VALUES (19)
        };
    }

    @BeforeMethod
    public void setUp() throws Exception {
        this.sut = DataTypesConverter.getNewInstance();
    }
    @Test
    public void testNewInstance(){
        assertNotNull(DataTypesConverter.getNewInstance());
    }
    @Test
    public void testLongToBytes() throws Exception {
        long testValue = 1000l;
        byte[] expectedValue = ByteBuffer.allocate(8).putLong(testValue).array();
        byte[] actualValue = this.sut.longToBytes(testValue);
        assertEquals(actualValue, expectedValue);
    }
    @Test
    public void testBytesToLong() throws Exception {
        long expectedValue = 10003L;
        byte[] testValue = ByteBuffer.allocate(8).putLong(expectedValue).array();
        assertEquals(this.sut.bytesToLong(testValue), expectedValue);
    }

    @Test
    public void testIntToBytes() throws Exception {
        int testValue = 123456;
        byte[] expectedValue = ByteBuffer.allocate(4).putInt(testValue).array();
        assertEquals(this.sut.intToBytes(testValue), expectedValue);
    }

    @Test(dataProvider = "dataToTestBytesToInt")
    public void testBytesToInt(byte[] testValue, int expectedValue) throws Exception {
        assertEquals(this.sut.bytesToInt(testValue), expectedValue);
    }
    @Test
    public void testByteToChar() throws Exception {
        char expectedValue = 'C';
        byte testValue = 0x43;
        assertEquals(this.sut.byteToChar(testValue), expectedValue);
    }
    @Test
    public void testBytesToString() throws Exception {
        String expectedValue = "12";
        byte[] testValue = {0x31, 0x32};
        assertEquals(this.sut.bytesToString(testValue), expectedValue);
    }

    @Test
    public void testNotAsciiBytesToInt() throws Exception {
        int expectedValue = 2345;
        byte[] testValue = this.sut.intToBytes(expectedValue);
        assertEquals(this.sut.notAsciiBytesToInt(testValue), expectedValue);
    }



}