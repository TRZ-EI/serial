package trzpoc.structure.serial;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import trzpoc.utils.ConfigurationHolder;

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

    }

}