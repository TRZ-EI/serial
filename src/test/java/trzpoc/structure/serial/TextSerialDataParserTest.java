package trzpoc.structure.serial;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import trzpoc.structure.Cell;
import trzpoc.utils.SerialDataMock;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 06/05/17
 * Time: 16.49
 */
public class TextSerialDataParserTest {

    private TextSerialDataParser sut;

    @BeforeTest
    private void setup(){
        this.sut = TextSerialDataParser.getNewInstance();
    }

    @Test
    public void testGetNewInstance() throws Exception {
        assertNotNull(this.sut);
    }

    @Test
    public void testReadByteArray() throws Exception {
        String expectedText = "This is a text fo test";
        SerialDataMock s = new SerialDataMock();
        byte[] data = s.prepareDataToTransmitAText('C', "10", "10", expectedText);
        Cell text = this.sut.readByteArray(data);
        assertEquals(text.getValue(), expectedText);
    }
}