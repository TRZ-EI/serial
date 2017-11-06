package trzpoc.structure.serial;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import trzpoc.structure.Cell;
import trzpoc.structure.Text;
import trzpoc.utils.ConfigurationHolder;

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

    @DataProvider
    private Object[][] createDataForTest(){
        return new Object[][]{
                {"^tQ0100This is a text fo test1234"}

        };
    }


    @BeforeTest
    private void setup(){
        ConfigurationHolder.createSingleInstanceByConfigUri(this.getClass().getClassLoader().getResource("application.properties").getFile());
        this.sut = TextSerialDataParser.getNewInstance();
    }
    @Test
    public void testGetNewInstance() throws Exception {
        assertNotNull(this.sut);
    }
    @Test
    public void testReadByteArray() throws Exception {
        Cell expectedValue = Text.getNewInstance().setValue("This is a text fo test").setxPos(0).setyPos(1);
        String expectedText = "^tQ0100This is a text fo test1234";
        Cell actualValue = this.sut.readByteArray(expectedText.getBytes());
        assertEquals(expectedValue, actualValue);
    }
}