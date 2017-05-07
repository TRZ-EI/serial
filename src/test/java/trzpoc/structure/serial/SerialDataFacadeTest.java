package trzpoc.structure.serial;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import trzpoc.structure.Cell;
import trzpoc.structure.CellsRow;
import trzpoc.structure.DataDisplayManager;
import trzpoc.structure.Variable;

import java.util.ArrayList;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 04/05/17
 * Time: 15.57
 */
public class SerialDataFacadeTest {
    private SerialDataFacade sut;

    @BeforeTest
    private void setup(){
        this.sut = SerialDataFacade.createNewInstance();
    }



    @Test
    public void testCreateNewInstance() throws Exception {
        assertNotNull(this.sut);
    }

    @Test
    public void testFillMatrixWithDataManyRows() throws Exception {
        String testDataFileName = "InputExamplesForTest.csv";
        String realFileName = this.getClass().getClassLoader().getResource(testDataFileName).getFile();

        DataDisplayManager expectedValue = this.createMatrixForTest();
        DataDisplayManager actualValue = this.sut.fillMatrixWithData(realFileName);
        assertTrue(this.verifyEquality(actualValue, expectedValue));

    }
    @Test
    public void testFillMatrixWithDataOneRow() throws Exception {
        this.sut = SerialDataFacade.createNewInstance();
        String testDataFileName = "InputExamplesForTestOneRow.csv";
        String realFileName = this.getClass().getClassLoader().getResource(testDataFileName).getFile();

        DataDisplayManager expectedValue = this.createMatrixForTestOneRow();
        DataDisplayManager actualValue = this.sut.fillMatrixWithData(realFileName);
        assertTrue(this.verifyEquality(actualValue, expectedValue));


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
        ArrayList<CellsRow> rows = new ArrayList<CellsRow>();
        for (int y = 0; y < 6; y ++){
            rows.add(this.createDataForTest(y, 1));
        }
        retValue.setRows(rows);
        return retValue;

    }

    private CellsRow createDataForTest(int rowIndex, int column) {

        VariableConfiguratorSerialDataParser dp = VariableConfiguratorSerialDataParser.getNewInstance();
        Variable v = dp.createVariable('C');
        v.setxPos(column).setyPos(rowIndex).setId(rowIndex);
        CellsRow retValue = CellsRow.getEmptyInstance().setyPos(rowIndex);
        retValue.addOrUpdateACell(v);
        return retValue;
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