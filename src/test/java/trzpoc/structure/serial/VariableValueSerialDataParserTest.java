package trzpoc.structure.serial;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import trzpoc.structure.Variable;
import trzpoc.utils.SerialDataMock;

import static org.testng.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 25/04/17
 * Time: 15.39
 */
public class VariableValueSerialDataParserTest {

    private VariableValueSerialDataParser sut;

    @BeforeMethod
    public void setUp() throws Exception {
        //ConfigurationHolder.createSingleInstanceByConfigUri("application.properties");
        this.sut = VariableValueSerialDataParser.getInstance();
    }
    @DataProvider
    private Object[][] dataForTest(){
        return new Object[][]{
                {"1", 100L},
                {"34", 10000L},
                {"8", 10L},
                {"18", -10L},
                {"4", -120L}
        };
    }

    @Test
    public void testNewInstance(){
        assertNotNull(this.sut);
    }

    @Test(dataProvider = "dataForTest")
    public void testReadByteArray(String id, long value) throws Exception {
        SerialDataMock sdm = new SerialDataMock();
        byte[] rawData = sdm.prepareDataToTransmitAVariable(id, value);
        Variable expectedValue = Variable.getInstance().setAConfiguration(false).setValue(Long.toString(value));
        expectedValue.setId(Integer.valueOf(id));
        Variable actualValue = (Variable) this.sut.readByteArray(rawData);
        assertEquals(actualValue, expectedValue);
        assertFalse(actualValue.isAConfiguration());
    }

}