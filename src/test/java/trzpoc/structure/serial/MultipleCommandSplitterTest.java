package trzpoc.structure.serial;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 17/08/17
 * Time: 14.32
 */
public class MultipleCommandSplitterTest {
    private MultipleCommandSplitter sut;


    @DataProvider(name="multipleCommands")
    private Object[][] createMultipleCommandsAndResponses(){
        return new Object[][]{
                {"^V07S31012FV08S31022FV09S31032FV0AS31042F",
                        new String[] {"^V07S31012F","^V08S31022F", "^V09S31032F", "^V0AS31042F"}
                },
                {"^v0701234567v0801234567v0901234567v0A01234567",
                        new String[] {"^v0701234567", "^v0801234567", "^v0901234567", "^v0A01234567"}
                },
                {"^v0701234567", new String[] {"^v0701234567"}},
                {"^V07S31012F", new String[] {"^V07S31012F"}},
                {"^tS0104PROVA N 1", new String[] {"^tS0104PROVA N 1"}},
                {"^B18-0000002000000010F", new String[] {"^B18-0000002000000010F"}}

        };
    }



    @BeforeMethod
    public void setUp() throws Exception {
        this.sut = MultipleCommandSplitter.getNewInstance();
    }

    @Test(dataProvider = "multipleCommands")
    public void testSplitterForCommands(String multipleCommand, String[] expectedValues){
        List<String> actualValues = this.sut.splitMultipleCommand(multipleCommand);
        assertEquals(actualValues.toArray(), expectedValues);
    }


}