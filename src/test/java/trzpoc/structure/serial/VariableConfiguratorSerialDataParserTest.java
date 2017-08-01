package trzpoc.structure.serial;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import trzpoc.structure.Variable;
import trzpoc.utils.DataTypesConverter;
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
                // WARNING: HEX VALUES
                {"C", 'P', '2', '4', "A", "1"}, //NERO PICCOLO
                {"C", 'Q', '2', '4', "A", "1"}, //ROSSO PICCOLO
                {"C", 'R', '2', '4', "A", "1"}, //VERDE PICCOLO
                {"C", 'S', '2', '4', "A", "1"}, //BLU PICCOLO

                {"C", '9', '2', '4', "A", "1"}, //NERO GRANDE
                {"C", 'A', '2', '4', "A", "1"}, //ROSSO GRANDE
                {"C", 'P', '2', '4', "A", "1"}, //VERDE PICCOLO
                {"C", 'C', '2', '4', "A", "1"}, //BLU GRANDE

                {"1", 'A', '2', '4', "A", "1"}, //ROSSO GRANDE
                {"2", 'A', '2', '4', "A", "1"}, //ROSSO GRANDE
                {"3", 'A', '2', '4', "A", "1"}, //ROSSO GRANDE
                {"4", 'A', '2', '4', "A", "1"}, //ROSSO GRANDE
                {"5", 'A', '2', '4', "A", "1"}, //ROSSO GRANDE
                {"6", 'A', '2', '4', "A", "1"}, //ROSSO GRANDE
                {"7", 'A', '2', '4', "A", "1"}, //ROSSO GRANDE
                {"8", 'A', '2', '4', "A", "1"}, //ROSSO GRANDE
                {"9", 'A', '2', '4', "A", "1"}, //ROSSO GRANDE
                {"A", 'A', '2', '4', "A", "1"}, //ROSSO GRANDE
                {"63", 'A', '2', '4', "A", "1"}, //ROSSO GRANDE
                {"63", 'A', '0', '0', "A", "63"}, //ROSSO GRANDE
                {"63", 'A', '1', '1', "1", "59"}, //ROSSO GRANDE
                {"63", 'A', '2', '2', "2", "4F"}, //ROSSO GRANDE
                {"63", 'A', '3', '3', "99", "C"}, //ROSSO GRANDE
                {"63", 'A', '4', '4', "30", "9"}, //ROSSO GRANDE
                {"63", 'A', '5', '5', "15", "45"}, //ROSSO GRANDE
                {"63", 'A', '6', '7', "3", "3B"}, //ROSSO GRANDE
                {"63", 'A', '7', '7', "7", "31"}, //ROSSO GRANDE
                {"63", 'A', '8', '8', "77", "27"}, //ROSSO GRANDE
                {"63", 'A', '9', '9', "88", "1D"} //ROSSO GRANDE

                
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
    public void testReadByteArray(String varNumber, char fontColor, char chars, char decimals, String row, String column) throws Exception {
        SerialDataMock sdm = new SerialDataMock();
        DataTypesConverter converter = DataTypesConverter.getNewInstance();

        byte[] rawData = sdm.prepareDataToConfigureAVariable(varNumber, fontColor, chars, decimals ,row, column);
        Variable expectedValue = this.sut.createVariable(fontColor);
        int digits = converter.bytesToInt(new byte[]{(byte)chars});
        int decs = converter.bytesToInt(new byte[]{(byte)decimals});
        int col = converter.bytesToInt(column.getBytes());
        int r = converter.bytesToInt(row.getBytes());
        int id = converter.bytesToInt(varNumber.getBytes());
        expectedValue.setAConfiguration(true).setIntegerLenght(digits).setDecimalLenght(decs).setxPos(col).setyPos(r).setId(id);
        Variable actualValue = (Variable) this.sut.readByteArray(rawData);
        assertTrue(actualValue.equals(expectedValue));
        assertTrue(actualValue.isAConfiguration());
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