package trzpoc.structure.serial;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import trzpoc.structure.Bar;
import trzpoc.utils.SerialDataMock;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 28/05/17
 * Time: 16.25
 */
public class BarSerialDataParserTest {

    private BarSerialDataParser sut;

    @BeforeTest
    private void setup(){
        this.sut = BarSerialDataParser.getNewInstance();
    }

    @Test
    public void testGetNewInstance() throws Exception {
        assertNotNull(this.sut);
    }

    @Test
    public void testReadByteArray() throws Exception {
        SerialDataMock s = new SerialDataMock();
        byte[] data = s.prepareDataToTransmitABar("10", 0, 100, "20");
        Bar actualValue = (Bar) this.sut.readByteArray(data);
        assertNotNull(actualValue);
        assertEquals(actualValue.getId(), 10);
        assertEquals(actualValue.getMinValue(), 0);
        assertEquals(actualValue.getMaxValue(), 100);
        assertEquals(actualValue.getyPos(), 20);


    }

}