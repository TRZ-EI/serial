package trzpoc.structure.serial;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import trzpoc.structure.Variable;
import trzpoc.utils.SerialDataMock;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 10/04/17
 * Time: 11.41
 */
public class VariableConfiguratorSerialDataParserTest {

    private VariableConfiguratorSerialDataParser sut;
     /*
    private final char NERO_PICCOLO = 'P';     //0x31
    private final char ROSSO_PICCOLO = 'Q';
    private final char VERDE_PICCOLO = 'R';
    private final char BLU_PICCOLO = 'S';


    private final char NERO_GRANDE = '9';  //0x39
    private final char ROSSO_GRANDE = 'A';
    private final char VERDE_GRANDE = 'P';
    private final char BLU_GRANDE = 'C';

     */


    @DataProvider
    private Object[][] prepareDataForTest(){
        return new Object[][]{
                {12, 'P', 2, 4, 10, 1}, //NERO PICCOLO
                {12, 'Q', 2, 4, 10, 1}, //ROSSO PICCOLO
                {12, 'R', 2, 4, 10, 1}, //VERDE PICCOLO
                {12, 'S', 2, 4, 10, 1}, //BLU PICCOLO

                {12, '9', 2, 4, 10, 1}, //NERO GRANDE
                {12, 'A', 2, 4, 10, 1}, //ROSSO GRANDE
                {12, 'P', 2, 4, 10, 1}, //VERDE PICCOLO
                {12, 'C', 2, 4, 10, 1}, //BLU GRANDE

                {1, 'A', 2, 4, 10, 1}, //ROSSO GRANDE
                {2, 'A', 2, 4, 10, 1}, //ROSSO GRANDE
                {3, 'A', 2, 4, 10, 1}, //ROSSO GRANDE
                {4, 'A', 2, 4, 10, 1}, //ROSSO GRANDE
                {5, 'A', 2, 4, 10, 1}, //ROSSO GRANDE
                {6, 'A', 2, 4, 10, 1}, //ROSSO GRANDE
                {7, 'A', 2, 4, 10, 1}, //ROSSO GRANDE
                {8, 'A', 2, 4, 10, 1}, //ROSSO GRANDE
                {9, 'A', 2, 4, 10, 1}, //ROSSO GRANDE
                {10, 'A', 2, 4, 10, 1}, //ROSSO GRANDE
                {99, 'A', 2, 4, 10, 1}, //ROSSO GRANDE

                {99, 'A', 0, 0, 10, 99}, //ROSSO GRANDE
                {99, 'A', 1, 1, 1, 89}, //ROSSO GRANDE
                {99, 'A', 2, 2, 2, 79}, //ROSSO GRANDE
                {99, 'A', 3, 3, 99, 12}, //ROSSO GRANDE
                {99, 'A', 4, 4, 30, 9}, //ROSSO GRANDE
                {99, 'A', 5, 5, 15, 69}, //ROSSO GRANDE
                {99, 'A', 6, 7, 3, 59}, //ROSSO GRANDE
                {99, 'A', 7, 7, 7, 49}, //ROSSO GRANDE
                {99, 'A', 8, 8, 77, 39}, //ROSSO GRANDE
                {99, 'A', 9, 9, 88, 29} //ROSSO GRANDE

                
        };
    }

    @BeforeMethod
    public void setUp() throws Exception {
        this.sut = VariableConfiguratorSerialDataParser.getNewInstance();
    }
    @Test
    public void testGetNewInstance() throws Exception {
        assertNotNull(VariableConfiguratorSerialDataParser.getNewInstance());
    }
    @Test(dataProvider = "prepareDataForTest")
    public void testReadByteArray(int varNumber, char fontColor, int chars, int decimals, int row, int column) throws Exception {
        SerialDataMock sdm = new SerialDataMock();
        byte[] rawData = sdm.prepareDataToConfigureAVariable(String.valueOf(varNumber), fontColor, String.valueOf(chars).charAt(0), String.valueOf(decimals).charAt(0),String.valueOf(row), String.valueOf(column));
        Variable expectedValue = this.sut.createVariable(fontColor);

        expectedValue.setId(varNumber).setIntegerLenght(chars).setDecimalLenght(decimals).setxPos(row).setyPos(column);
        Variable actualValue = (Variable) this.sut.readByteArray(rawData);
        assertTrue(actualValue.equals(expectedValue));
    }
    /*
    private final char NERO_PICCOLO = 'P';     //0x31
    private final char ROSSO_PICCOLO = 'Q';
    private final char VERDE_PICCOLO = 'R';
    private final char BLU_PICCOLO = 'S';


    private final char NERO_GRANDE = '9';  //0x39
    private final char ROSSO_GRANDE = 'A';
    private final char VERDE_GRANDE = 'P';
    private final char BLU_GRANDE = 'C';

     */


}