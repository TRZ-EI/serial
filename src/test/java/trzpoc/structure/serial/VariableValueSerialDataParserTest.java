package trzpoc.structure.serial;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import trzpoc.structure.Variable;

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
                {"^v0A00000001", Variable.getInstance().setAConfiguration(false).setId(10).setValue("1")},
                {"^v0200000001", Variable.getInstance().setAConfiguration(false).setId(2).setValue("1")},
                {"^v0A00000002", Variable.getInstance().setAConfiguration(false).setId(10).setValue("2")},
                {"^v0200000002", Variable.getInstance().setAConfiguration(false).setId(2).setValue("2")},
                {"^v0A00000003", Variable.getInstance().setAConfiguration(false).setId(10).setValue("3")},
                {"^v0200000003", Variable.getInstance().setAConfiguration(false).setId(2).setValue("3")},
                {"^v0A00000004", Variable.getInstance().setAConfiguration(false).setId(10).setValue("4")},
                {"^v02FFFFFF79", Variable.getInstance().setAConfiguration(false).setId(2).setValue("-135")}
                /*
                {"v0A00000005"},
                {"v0200000005"},
                {"v0A00000006"},
                {"v0200000006"},
                {"v0A00000007"},
                {"v0200000007"},
                {"v0A00000008"},
                {"v0200000008"},
                {"v0A00000009"}
                */
        };
    }

    @Test
    public void testNewInstance(){
        assertNotNull(this.sut);
    }

    @Test(dataProvider = "dataForTest")
    public void testReadByteArray(String command, Variable expectedValue) throws Exception {
        Variable actualValue = (Variable) this.sut.readByteArray(command.getBytes());
        assertEquals(actualValue, expectedValue);
        assertEquals(actualValue.getValue(), expectedValue.getValue());
    }

}