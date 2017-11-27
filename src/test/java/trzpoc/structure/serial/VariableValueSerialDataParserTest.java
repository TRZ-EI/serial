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
                {"^v0A00000001","0000.1",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(10).setValue("1")},
                {"^v0200000001","0000.1",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(2).setValue("1")},
                {"^v0A00000002","0000.2",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(10).setValue("2")},
                {"^v0200000002","0000.2",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(2).setValue("2")},
                {"^v0A00000003", "0000.3",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(10).setValue("3")},
                {"^v0200000003", "0000.3",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(2).setValue("3")},
                {"^v0A00000004", "0000.4",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(10).setValue("4")},
                {"^v02FFFFFF79", "-0013.5",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(2).setValue("-135")},
                {"^v05FFFFFFD80597","-0004.0",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(5).setValue("-40")},
                {"^v06FFFFFFF453CF","-0001.2",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(6).setValue("-12")},
                {"^v0500000003C0A3","0000.3",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(5).setValue("3")},
                {"^v060000000453CF","0000.4",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(6).setValue("4")},
                {"^v0500000005A3FF","0000.5",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(5).setValue("5")},
                {"^v060000000A53CF","0001.0",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(6).setValue("10")},
                {"^v05FFFFFFF9E4B2","-0000.7",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(5).setValue("-7")},
                {"^v06FFFFFFFF53CF","-0000.1",
                        Variable.getInstance().setIntegerLenght(4).setDecimalLenght(1).setAConfiguration(false).setId(6).setValue("-1")}
        };
    }

    @Test
    public void testNewInstance(){
        assertNotNull(this.sut);
    }

    @Test(dataProvider = "dataForTest")
    public void testReadByteArray(String command, String expectedFormattedValue, Variable expectedValue) throws Exception {
        Variable actualValue = (Variable) this.sut.readByteArray(command.getBytes());
        actualValue.setIntegerLenght(4).setDecimalLenght(1);
        assertEquals(actualValue, expectedValue);
        assertEquals(actualValue.getValue(), expectedValue.getValue());
        assertEquals(actualValue.prepareFormattedValue(), expectedFormattedValue);
    }

}