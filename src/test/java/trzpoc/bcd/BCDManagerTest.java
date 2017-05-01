package trzpoc.bcd;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 21/03/17
 * Time: 14.21
 */
public class BCDManagerTest {


    private BCDManager sut;
    
    
    @DataProvider
    private Object[][] createStringDataForDecimalToBCDTest(){
        return new Object[][]{
            {1L,"00000001"},
            {11L,"00010001"},
            {111L,"0000000100010001"},
            {1111L,"0001000100010001"},
            {11111L,"000000010001000100010001"},
            {42,"01000010"},
            {112233L,"000100010010001000110011"},
            {12345L,"000000010010001101000101"}
        };
    }
    @DataProvider
    private Object[][] createDataForBCDToDecimalTest(){
        return new Object[][]{
                {1L,new byte[]{1}},
                {11L,new byte[]{17}},
                {111L, new byte[]{1,17}},
                {1111L,new byte[]{17,17}},
                {11111L,new byte[]{1,17,17}},
                {42,new byte[]{66}},
                {112233L,new byte[]{17,34,51}},
                {12345L,new byte[]{1,35,69}}
        };
    }

    @BeforeMethod
    public void setUp() throws Exception {
        this.sut = BCDManager.getNewInstance();
    }

    @Test(dataProvider = "createStringDataForDecimalToBCDTest")
    public void testDecimalToBCDConvertingResultToString(long value, String expectedValue) throws Exception {
        assertEquals(expectedValue, this.byteArrayToBinaryString(this.sut.DecimalToBCD(value)));
    }
    @Test(dataProvider = "createDataForBCDToDecimalTest")
    public void testDecimalToBCD(long value, byte[] expectedValue) throws Exception {
        assertEquals(expectedValue, this.sut.DecimalToBCD(value));
    }
    @Test(dataProvider = "createDataForBCDToDecimalTest")
    public void testBCDToDecimal(long expectedValue, byte[] value) throws Exception {
        assertEquals(expectedValue, this.sut.BCDToDecimal(value));
    }

    //@Test
    public void testBCDtoString() throws Exception {

    }

    //@Test
    public void testBCDtoString1() throws Exception {

    }
    private String byteArrayToBinaryString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte i : bytes) {
            String byteInBinary = String.format("%8s", Integer.toBinaryString(i)).replace(' ', '0');
            sb.append(byteInBinary);
        }
        return sb.toString();
    }

}