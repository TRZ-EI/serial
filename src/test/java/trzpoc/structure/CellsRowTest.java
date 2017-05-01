package trzpoc.structure;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.testng.Assert.assertEquals;

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
            retValue.add(Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(2).setDecimalLenght(2).setValue(baseValue).setxPos(x).setyPos(1));
        }
        return retValue;
    }
}