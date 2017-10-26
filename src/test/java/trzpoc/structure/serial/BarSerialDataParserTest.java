package trzpoc.structure.serial;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import trzpoc.structure.Bar;
import trzpoc.utils.ConfigurationHolder;
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
    private void prepareFontsForTest(){
        try {
            this.loadProperties();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @BeforeTest
    private void setup(){
        this.sut = BarSerialDataParser.getNewInstance();
    }

    private void loadProperties(){
        ConfigurationHolder.createSingleInstanceByConfigUri(this.getClass().getClassLoader().getResource("application.properties").getFile()).getProperties();
    }




    @Test
    public void testGetNewInstance() throws Exception {
        assertNotNull(this.sut);
    }

    //@Test
    // TODO: REVIEW TEST IMPLEMENTATION
    public void testReadByteArray() throws Exception {
        SerialDataMock s = new SerialDataMock();
        // WARNING: HEX VALUES - A = 10, 14 = 20
        byte[] data = s.prepareDataToTransmitABar("A", 0, 100, "14");
        Bar actualValue = (Bar) this.sut.readByteArray(data);
        assertNotNull(actualValue);
        assertEquals(actualValue.getId(), 10);
        assertEquals(actualValue.getMinValue(), 0);
        assertEquals(actualValue.getMaxValue(), 100);
        assertEquals(actualValue.getyPos(), 20);


    }

}