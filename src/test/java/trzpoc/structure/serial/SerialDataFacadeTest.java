package trzpoc.structure.serial;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import trzpoc.structure.CellsRow;
import trzpoc.structure.DataDisplayManager;
import trzpoc.structure.Variable;

import java.util.ArrayList;

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

    @BeforeTest
    private void setup(){
        this.sut = SerialDataFacade.createNewInstance();
    }



    @Test
    public void testCreateNewInstance() throws Exception {
        assertNotNull(this.sut);
    }

    @Test
    public void testFillMatrixWithData() throws Exception {
        String testDataFileName = "InputExamplesForTest.csv";
        String realFileName = this.getClass().getClassLoader().getResource(testDataFileName).getFile();

        DataDisplayManager expectedValue = this.createMatrixForTest();
        DataDisplayManager actualValue = this.sut.fillMatrixWithData(realFileName);
        assertEquals(actualValue, expectedValue);

        // TODO: how to check if two DataDisplayManager are equals

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
            rows.add(this.createDataForTest(y));
        }
        retValue.setRows(rows);
        return retValue;

    }

    private CellsRow createDataForTest(int rowIndex) {

        VariableConfiguratorSerialDataParser dp = VariableConfiguratorSerialDataParser.getNewInstance();
        Variable v = dp.createVariable('C');
        v.setxPos(1).setyPos(rowIndex).setId(rowIndex);
        CellsRow retValue = CellsRow.getEmptyInstance();
        retValue.addOrUpdateACell(v);
        return retValue;
    }



}