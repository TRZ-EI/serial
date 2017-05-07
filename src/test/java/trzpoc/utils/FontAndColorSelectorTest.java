package trzpoc.utils;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 06/05/17
 * Time: 14.37
 *
 *
 *
 */
public class FontAndColorSelectorTest {

    private final char NERO_PICCOLO = 'P';     //0x31
    private final char ROSSO_PICCOLO = 'Q';
    private final char VERDE_PICCOLO = 'R';
    private final char BLU_PICCOLO = 'S';


    private final char NERO_GRANDE = '9';  //0x39
    private final char ROSSO_GRANDE = 'A';
    private final char VERDE_GRANDE = 'G';
    private final char BLU_GRANDE = 'C';

    private FontAndColorSelector sut;
    private Font bigFont;
    private Font smallFont;

    @DataProvider
    private Object[][] dataToTestForColor(){
        return new Object[][]{
            {NERO_PICCOLO, Color.BLACK},
            {NERO_GRANDE, Color.BLACK},
            {ROSSO_PICCOLO, Color.RED},
            {ROSSO_GRANDE, Color.RED},
            {BLU_PICCOLO, Color.BLUE},
            {BLU_GRANDE, Color.BLUE},
            {VERDE_PICCOLO, Color.GREEN},
            {VERDE_GRANDE, Color.GREEN},
        };
    }
    @DataProvider
    private Object[][] dataToTestForFont(){
        return new Object[][]{
            {NERO_PICCOLO, this.smallFont},
            {NERO_GRANDE, this.bigFont},
            {ROSSO_PICCOLO, this.smallFont},
            {ROSSO_GRANDE, this.bigFont},
            {BLU_PICCOLO, this.smallFont},
            {BLU_GRANDE, this.bigFont},
            {VERDE_PICCOLO, this.smallFont},
            {VERDE_GRANDE, this.bigFont},
        };
    }
    @BeforeTest
    private void createNewInstance(){
        this.sut = FontAndColorSelector.getNewInstance();
    }

    @BeforeTest
    private void seutp(){
        bigFont = Font.font("Arial", FontWeight.NORMAL, 20);
        smallFont = Font.font("Arial", FontWeight.NORMAL, 16);

    }

    @Test
    public void testGetNewInstance() throws Exception {
        assertNotNull(this.sut);
    }

    @Test(dataProvider = "dataToTestForFont")
    public void testSelectFont(Character selector, Font expectedValue) throws Exception {
        Font actualValue = this.sut.selectFont(selector);
        assertEquals(actualValue, expectedValue);
    }

    @Test(dataProvider = "dataToTestForColor")
    public void testSelectColor(Character selector, Color expectedValue) throws Exception {
        Color actualValue = this.sut.selectColor(selector);
        assertEquals(actualValue, expectedValue);
    }

}