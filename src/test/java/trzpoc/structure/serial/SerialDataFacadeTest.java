package trzpoc.structure.serial;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import trzpoc.structure.*;
import trzpoc.utils.ConfigurationHolder;
import trzpoc.utils.FontAndColorSelector;

import java.io.UnsupportedEncodingException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 04/05/17
 * Time: 15.57
 */
public class SerialDataFacadeTest {
    private SerialDataFacade sut;
    private FontAndColorSelector fontAndColorSelector;

    @DataProvider
    private Object[][] testDataForConfigurationVariable(){
        return new Object[][]{
                {"^V07A310509", Variable.getInstanceByFontAndColor(fontAndColorSelector.selectFont('A'), fontAndColorSelector.selectColor('A'))
                        .setIntegerLenght(3).setDecimalLenght(1).setId(7).setxPos(9).setyPos(5)},
                {"^V03R220101", Variable.getInstanceByFontAndColor(fontAndColorSelector.selectFont('R'), fontAndColorSelector.selectColor('R'))
                        .setIntegerLenght(2).setDecimalLenght(2).setId(3).setxPos(1).setyPos(1)},
                {"^V07A310A0B", Variable.getInstanceByFontAndColor(fontAndColorSelector.selectFont('A'), fontAndColorSelector.selectColor('A'))
                        .setIntegerLenght(3).setDecimalLenght(1).setId(7).setxPos(11).setyPos(10)},
                {"^V0CA310A0B", Variable.getInstanceByFontAndColor(fontAndColorSelector.selectFont('A'), fontAndColorSelector.selectColor('A'))
                        .setIntegerLenght(3).setDecimalLenght(1).setId(12).setxPos(11).setyPos(10)}
        };
    }
    @DataProvider
    private Object[][] testDataForVariable(){
        return new Object[][]{
                {"^v0C0000000A", Variable.getInstance().setAConfiguration(false).setId(12).setValue("10")},
                {"^v0C0000000B", Variable.getInstance().setAConfiguration(false).setId(12).setValue("11")},
                {"^v0C0000000C", Variable.getInstance().setAConfiguration(false).setId(12).setValue("12")},
                {"^v0C0000000D", Variable.getInstance().setAConfiguration(false).setId(12).setValue("13")},
                {"^v0C0000000E", Variable.getInstance().setAConfiguration(false).setId(12).setValue("14")},
                {"^v0C0000000F", Variable.getInstance().setAConfiguration(false).setId(12).setValue("15")}
        };
    }
    @DataProvider
    private Object[][] testDataForText(){
        return new Object[][]{
                {"^tA0509TestoProva1xxxx\n", Text.getNewInstanceByFontAndColor(fontAndColorSelector.selectFont('A'), fontAndColorSelector.selectColor('A'))
                        .setxPos(9).setyPos(5).setValue("TestoProva1")},
                {"^tA0A0BTestoProva2xxxx\n", Text.getNewInstanceByFontAndColor(fontAndColorSelector.selectFont('A'), fontAndColorSelector.selectColor('A'))
                        .setxPos(11).setyPos(10).setValue("TestoProva2")},
                {"^tR0F0CQuesto essere un testo di prova lungoxxxx\n", Text.getNewInstanceByFontAndColor(fontAndColorSelector.selectFont('R'), fontAndColorSelector.selectColor('R'))
                        .setxPos(12).setyPos(15).setValue("Questo essere un testo di prova lungo")},
        };
    }
    @DataProvider
    private Object[][] testDataForNumber(){
        return new Object[][]{
                {"^nP20001C00000001", Variable.getInstance().setIntegerLenght(2).
                        setDecimalLenght(0).setyPos(0).setxPos(28).setId(434).setValue("1")},    // ID = 434
                {"^nQ410611FFFFFFD8", Variable.getInstance().setIntegerLenght(4).
                        setDecimalLenght(1).setyPos(6).setxPos(17).setId(293).setValue("-40")},  // ID = 293
                {"^nR41062500000005", Variable.getInstance().setIntegerLenght(4).
                        setDecimalLenght(1).setyPos(6).setxPos(37).setId(983).setValue("5")},  // ID = 983
                {"^nS410B11FFFFFFD8", Variable.getInstance().setIntegerLenght(4).
                        setDecimalLenght(1).setyPos(11).setxPos(17).setId(423).setValue("-40")},  // ID = 423
                {"^nS410B250000000A", Variable.getInstance().setIntegerLenght(4).
                        setDecimalLenght(1).setyPos(11).setxPos(37).setId(1213).setValue("10")},  // ID = 1213
        };
    }

    @BeforeTest
    private void setup(){
        String configurationFile = this.getClass().getClassLoader().getResource("application.properties").getFile();
        ConfigurationHolder.createSingleInstanceByConfigUri(configurationFile);
        this.sut = SerialDataFacade.createNewInstance();
        this.fontAndColorSelector = FontAndColorSelector.getNewInstance();
    }
    @Test
    public void testCreateNewInstance() throws Exception {
        assertNotNull(this.sut);
    }
    @Test(dataProvider = "testDataForConfigurationVariable")
    public void testOnSerialDataParserForConfiguration(String dataToParse, Cell expectedValue) throws UnsupportedEncodingException {
        Cell actualValue = this.sut.onSerialDataParser(dataToParse.getBytes());
        assertEquals(actualValue, expectedValue);
    }
    @Test(dataProvider = "testDataForVariable")
    public void testOnSerialDataParserForVariables(String dataToParse, Cell expectedValue) throws UnsupportedEncodingException {
        Cell actualValue = this.sut.onSerialDataParser(dataToParse.getBytes());
        String actualVariableValue = actualValue.getValue();
        String expectedVariableValue = expectedValue.getValue();
        assertEquals(actualValue, expectedValue);
        assertEquals(actualVariableValue, expectedVariableValue);
    }
    @Test(dataProvider = "testDataForText")
    public void testOnSerialDataParserForText(String dataToParse, Cell expectedValue) throws UnsupportedEncodingException {
        Cell actualValue = this.sut.onSerialDataParser(dataToParse.getBytes());
        String actualVariableValue = actualValue.getValue();
        String expectedVariableValue = expectedValue.getValue();
        assertEquals(actualValue, expectedValue);
        assertEquals(actualVariableValue, expectedVariableValue);
    }
    @Test(dataProvider = "testDataForNumber")
    public void testOnSerialDataParserForNumber(String dataToParse, Cell expectedValue) throws UnsupportedEncodingException {
        Cell actualValue = this.sut.onSerialDataParser(dataToParse.getBytes());
        String actualVariableValue = actualValue.getValue();
        String expectedVariableValue = expectedValue.getValue();
        assertEquals(actualValue, expectedValue);
        assertEquals(actualVariableValue, expectedVariableValue);
    }


/*
    ^,V,0,C,2,2,0,0
    ^,V,1,C,2,2,0,1
    ^,V,2,C,2,2,0,2
    ^,V,3,C,2,2,0,3
    ^,V,4,C,2,2,0,4
    ^,V,5,C,2,2,0,5
*/
    private DataDisplayManager createMatrixForTestOneRow() {
        DataDisplayManager retValue = DataDisplayManager.getNewInstance();
        retValue.prepareDisplayMap(20);
        int rowIndex = 0;
        Variable v = null;
        for (int column = 0; column < 6; column ++){


            VariableConfiguratorSerialDataParser dp = VariableConfiguratorSerialDataParser.getNewInstance();
            v = dp.createVariable('C');
            v.setxPos(column).setyPos(rowIndex).setId(column);
            retValue.addOrUpdateCellInMatrix(v);
        }
        
        return retValue;


    }

    private boolean verifyEquality(DataDisplayManager actualValue, DataDisplayManager expectedValue) {
        boolean retValue = actualValue.getNumberOfRows() == expectedValue.getNumberOfRows();
        if (retValue) {
            for (int i = 0; i < actualValue.getNumberOfRows(); i++) {
                CellsRow one = actualValue.getOrCreateARow(i);
                CellsRow otherOne = expectedValue.getOrCreateARow(i);
                retValue &= this.compareRows(one, otherOne);
            }
        }
        return retValue;
    }

    /*

        ^,V,0,C,2,2,0,1
        ^,V,1,C,2,2,1,1
        ^,V,2,C,2,2,2,1
        ^,V,3,C,2,2,3,1
        ^,V,4,C,2,2,4,1
        ^,V,5,C,2,2,5,1
    */
    private DataDisplayManager createMatrixForTest(){
        DataDisplayManager retValue = DataDisplayManager.getNewInstance();
        retValue.prepareDisplayMap(20);
        for (int y = 0; y < 6; y ++){
            retValue.addOrUpdateCellInMatrix(this.createCellForTest(y, 1));
        }
        return retValue;

    }

    private Cell createCellForTest(int row, int column){
        VariableConfiguratorSerialDataParser dp = VariableConfiguratorSerialDataParser.getNewInstance();
        Variable v = dp.createVariable('C');
        v.setxPos(column).setyPos(row).setId(row);
        return v;

    }

    private boolean compareRows(CellsRow one, CellsRow otherOne) {
        boolean retValue = one.equals(otherOne);
        if (retValue){
            Cell oneCell = null;
            Cell otherCell = null;
            for (int column = 0; column < one.getCellsCount(); column ++){
                oneCell = one.getCellByColumnIndex(column);
                otherCell = otherOne.getCellByColumnIndex(column);
                retValue &= oneCell.equals(otherCell);
            }
        }
        return retValue;
    }
}