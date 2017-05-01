package trzpoc.structure;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.junit.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 26/03/17
 * Time: 12.17
 */
public class DataDisplayManagerTest {

    private DataDisplayManager sut;

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
    private void addRowsToSut(int expectedValue) {
        for(int i = 0; i < expectedValue; i ++){
            this.sut.getOrCreateARow(i);
        }
    }


}