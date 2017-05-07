package trzpoc.structure;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.junit.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 26/03/17
 * Time: 12.17
 */
public class DataDisplayManagerTest {

    private DataDisplayManager sut;
    private Font font;
    private Color color;

    @BeforeClass
    public void setupForTest(){
        this.font = Font.font("Arial", FontWeight.BOLD, 30);
        this.color = Color.BLUE;
    }


    @BeforeMethod
    public void setUp() throws Exception {
        this.sut = DataDisplayManager.getNewInstance();

    }

    @Test
    public void testGetNewInstance() throws Exception {
        assertNotNull(this.sut);
    }

    @Test
    public void testGetOrCreateARow() throws Exception {
        CellsRow expectedValue = CellsRow.getEmptyInstance().setyPos(10);
        assertEquals(expectedValue, this.sut.getOrCreateARow(10));
    }

    @Test
    public void testGetNumberOfRows() throws Exception {
        int expectedValue = 10;
        this.addRowsToSut(expectedValue);
        assertEquals(expectedValue, this.sut.getNumberOfRows());
    }
    @Test
    public void testPrepareDisplayMap() throws Exception {
        int expectedValue = 50;
        assertEquals(expectedValue, this.sut.prepareDisplayMap(expectedValue).getNumberOfRows());
    }
    @Test
    public void testUpdateCellInMatrix() throws Exception {
        this.sut.setRows(this.createMatrixForTest());
        // Create a cell existing, with a new value
        int rowIndex = 0;
        int columnIndex = 0;
        long newValue = 180438L;
        Cell existingCell = Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(2).setDecimalLenght(2).setValue(Long.toString(180438)).setxPos(columnIndex).setyPos(rowIndex).setId(0);
        this.sut.addOrUpdateCellInMatrix(existingCell);
        Cell expectedValue = this.sut.getOrCreateARow(rowIndex).getCellByColumnIndex(columnIndex);
        assertEquals(expectedValue, existingCell);
        assertTrue(newValue == Long.valueOf(expectedValue.getValue()));


    }
    @Test
    public void testAddCellInMatrix() throws Exception {
        this.sut.setRows(this.createMatrixForTest());

        // Create a new Cell, not existing in matrix
        int rowIndex = 0;
        int columnIndex = 25;
        Cell newCell = Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(2).setDecimalLenght(2).setValue(Long.toString(180438)).setxPos(columnIndex).setyPos(rowIndex).setId(0);
        this.sut.addOrUpdateCellInMatrix(newCell);
        Cell expectedValue = this.sut.getOrCreateARow(rowIndex).addOrUpdateACell(newCell);
        assertEquals(expectedValue, newCell);
    }
    private void addRowsToSut(int expectedValue) {
        for(int i = 0; i < expectedValue; i ++){
            this.sut.getOrCreateARow(i);
        }
    }
    private ArrayList<CellsRow> createMatrixForTest(){
        ArrayList<CellsRow> retValue = new ArrayList<CellsRow>();
        // matrix made by 10 x 10 cells
        for (int y = 0; y < 10; y ++){
            retValue.add(this.createDataForTest(y));
        }
        return retValue;
    }

    private CellsRow createDataForTest(int rowIndex) {
        CellsRow retValue = CellsRow.getEmptyInstance().setyPos(rowIndex);
        int baseId = rowIndex * 10;
        int baseValue = 1000;
        for (int x = 0; x < 10; x ++){
            retValue.addOrUpdateACell(Variable.getInstanceByFontAndColor(font, color).setIntegerLenght(2).setDecimalLenght(2).setValue(Long.toString(baseValue + x)).setxPos(x).setyPos(rowIndex).setId(x + baseId));
        }
        return retValue;
    }



}