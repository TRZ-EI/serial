package trzpoc.structure;

import javafx.scene.paint.Color;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static junit.framework.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 01/11/17
 * Time: 15.09
 */
public class VariableCollectorTest {
    private VariableCollector sut;

    @BeforeMethod
    public void setUp() throws Exception {
        this.sut = VariableCollector.getSingleInstance();
    }

    @Test
    public void testGetSingleInstance() throws Exception {
        assertNotNull(this.sut);
    }

    @Test
    public void testAddOrGetUpdatedInstance() throws Exception {
        Cell c = Variable.getInstance().setAConfiguration(true).setValue("100").setId(1).setxPos(10).setyPos(10).setColor(Color.BLUE);
        Cell expectedValue = this.sut.addOrGetUpdatedInstance(c);
        assertEquals(c, expectedValue);

        Cell value = Variable.getInstance().setAConfiguration(false).setId(1).setValue("200");

        expectedValue = this.sut.addOrGetUpdatedInstance(value);
        assertEquals(value.getId(), expectedValue.getId());
        assertEquals(expectedValue.getValue(), "200");

        c = Variable.getInstance().setAConfiguration(true).setValue("100").setId(1).setxPos(10).setyPos(10).setColor(Color.RED);
        expectedValue = this.sut.addOrGetUpdatedInstance(c);
        assertEquals(expectedValue.getColor(),Color.RED);


    }

    @Test
    public void testFindCellById() throws Exception {
        Cell c = Variable.getInstance().setValue("100").setId(2000).setxPos(10).setyPos(10);
        this.sut.addOrGetUpdatedInstance(c);
        Cell expectedValue = this.sut.findCellById(2000);
        assertEquals(c, expectedValue);
    }
    @Test
    public void testClear() throws Exception {
        Cell c = Variable.getInstance().setValue("100").setId(2000).setxPos(10).setyPos(10);
        this.sut.addOrGetUpdatedInstance(c);
        assertNotNull(this.sut.findCellById(2000));
        this.sut.clear();
        assertNull(this.sut.findCellById(2000));
    }

}