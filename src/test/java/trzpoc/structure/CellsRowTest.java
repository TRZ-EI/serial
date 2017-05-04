package trzpoc.structure;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 19/03/17
 * Time: 16.43
 */
public class CellsRowTest {
    private CellsRow sut;
    private Font font;
    private Color color;
    private List<Cell> referenceData;

    @BeforeClass
    private void prepareEnvironment(){
        this.font = Font.font("Arial", FontWeight.BOLD, 30);
        this.color = Color.BLUE;
        referenceData = this.createDataForTest();
    }
    @BeforeMethod
    public void setUp() throws Exception {
        this.sut = CellsRow.getEmptyInstance();
    }
    @DataProvider
    private Object[][] createDataForUpdateCellsTest(){
        return new Object[][]{
                {"1345", Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(2).setDecimalLenght(2).setValue("1345").setxPos(0).setyPos(1).setId(0)},
                {"120400", Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(4).setDecimalLenght(2).setValue("120400").setxPos(1).setyPos(1).setId(1)},
                {"120401", Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(4).setDecimalLenght(2).setValue("120401").setxPos(2).setyPos(1).setId(2)},
                {"120402", Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(4).setDecimalLenght(2).setValue("120402").setxPos(3).setyPos(1).setId(3)},
                {"120403", Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(4).setDecimalLenght(2).setValue("120403").setxPos(4).setyPos(1).setId(4)},
                {"120404", Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(4).setDecimalLenght(2).setValue("120404").setxPos(5).setyPos(1).setId(5)},
                {"120405", Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(4).setDecimalLenght(2).setValue("120405").setxPos(6).setyPos(1).setId(6)},
                {"120406", Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(4).setDecimalLenght(2).setValue("120406").setxPos(7).setyPos(1).setId(7)},
                {"120407", Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(4).setDecimalLenght(2).setValue("120407").setxPos(8).setyPos(1).setId(8)},
                {"120408", Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(4).setDecimalLenght(2).setValue("120408").setxPos(9).setyPos(1).setId(9)}
        };
    }
    @DataProvider
    private Object[][] createDataForAddCellTest(){
        return new Object[][]{
                {Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(2).setDecimalLenght(2).setValue("1345").setxPos(22).setyPos(1).setId(0)},
                {Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(4).setDecimalLenght(2).setValue("120400").setxPos(23).setyPos(1).setId(1)},
                {Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(4).setDecimalLenght(2).setValue("120401").setxPos(24).setyPos(1).setId(2)},
                {Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(4).setDecimalLenght(2).setValue("120402").setxPos(25).setyPos(1).setId(3)},
                {Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(4).setDecimalLenght(2).setValue("120403").setxPos(26).setyPos(1).setId(4)},
                {Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(4).setDecimalLenght(2).setValue("120404").setxPos(27).setyPos(1).setId(5)},
                {Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(4).setDecimalLenght(2).setValue("120405").setxPos(28).setyPos(1).setId(6)},
                {Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(4).setDecimalLenght(2).setValue("120406").setxPos(29).setyPos(1).setId(7)},
                {Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(4).setDecimalLenght(2).setValue("120407").setxPos(30).setyPos(1).setId(8)},
                {Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(4).setDecimalLenght(2).setValue("120408").setxPos(31).setyPos(1).setId(9)}
        };
    }

    @Test
    public void testGetEmptyInstance() throws Exception {
        assertNotNull(CellsRow.getEmptyInstance());
    }
    @Test(dataProvider = "createDataForUpdateCellsTest")
    public void testUpdateACell(String newValue, Cell existingCell) throws Exception {
        // First: create an existing cell with different data - UPDATE
        // x: 0 to 9
        this.sut.setCells(this.referenceData);
        Cell expectedValue = this.sut.addOrUpdateACell(existingCell);
        assertEquals(expectedValue.getValue(), newValue);
    }
    @Test(dataProvider = "createDataForAddCellTest")
    public void testAddACell(Cell newCell) throws Exception {
        // First: create a new cell not existing in row - ADD
        // New Cell row = yPos = 1, existing row; column = xPos = from 22 to 31, NOT existing cell
        this.sut.setCells(this.referenceData);
        assertFalse(this.sut.getCells().contains(newCell));
        Cell expectedValue = this.sut.addOrUpdateACell(newCell);
        assertEquals(expectedValue, newCell);
    }
    @Test
    public void testGetWidthByCells() throws Exception {
        int expectedValue = this.calculateWidthByCells(referenceData);
        assertEquals(expectedValue, this.sut.setCells(referenceData).getWidthByCells());
    }
    @Test
    public void testGetCellsCount() throws Exception {
        int expectedValue = this.referenceData.size();
        assertEquals(expectedValue, this.sut.setCells(this.referenceData).getCellsCount());
    }
    @Test
    public void testGetAndSetyPos() throws Exception {
        int expectedValue = 10;
        assertEquals(expectedValue, this.sut.setyPos(10).getyPos());
    }
    @Test
    public void testEquals() throws Exception {
        CellsRow expectedValue = CellsRow.getEmptyInstance().setyPos(100);
        assertEquals(expectedValue, this.sut.setyPos(100));
    }
    @Test
    public void testGetMaxHeight(){
        TextMetricCalculator mc = TextMetricCalculator.getInstance();
        String testValue = "1000";
        Font highFont = Font.font("Arial", FontWeight.BOLD, 30);
        int expectedValue = mc.calculateHeight(testValue, highFont);
        List<Cell> dataForThisTest = this.createRowWithDifferentCellHeights(testValue);
        assertEquals(this.sut.setCells(dataForThisTest).getMaxHeight(), expectedValue);
    }
    @Test
    public void testGetCellByColumnIndex() throws Exception {
        int rowIndex = 1;
        int columnIndex = 0;
        Cell expectedValue = (Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(2).setDecimalLenght(2).setValue("1000").setxPos(0).setyPos(rowIndex).setId(0));
        this.sut.setCells(this.referenceData);
        Cell actualValue = this.sut.getCellByColumnIndex(columnIndex);
        assertEquals(actualValue, expectedValue);


    }

    private List<Cell> createRowWithDifferentCellHeights(String value) {
        List<Cell> retValue = new ArrayList<>();
        Font smallFont = Font.font("Arial", FontWeight.BOLD, 10);
        Font mediumFont = Font.font("Arial", FontWeight.BOLD, 20);
        Font highFont = Font.font("Arial", FontWeight.BOLD, 30);

        this.fillListWithCellsUsingAFont(retValue, smallFont, value);
        this.fillListWithCellsUsingAFont(retValue, mediumFont, value);
        this.fillListWithCellsUsingAFont(retValue, highFont, value);
        return retValue;
    }

    private void fillListWithCellsUsingAFont(List<Cell> retValue, Font smallFont, String value) {
        for (int index = 0; index < 10; index ++){
            retValue.add(Variable.getInstanceByFontAndColor(smallFont, Color.BLACK).setValue(value));
        }
    }

    private int calculateWidthByCells(List<Cell> dataForTest) {
        int retValue = 0;
        for (Iterator<Cell> it = dataForTest.iterator(); it.hasNext(); ) {
            retValue += it.next().getWidth();
        }
        return retValue;
    }
    private List<Cell> createDataForTest() {
        List<Cell> retValue = new ArrayList<Cell>();
        String baseValue = "1000";
        for (int x = 0; x < 10; x ++){
            retValue.add(Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(2).setDecimalLenght(2).setValue(baseValue).setxPos(x).setyPos(1).setId(x));
        }
        return retValue;
    }
}