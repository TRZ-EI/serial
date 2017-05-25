package trzpoc.utils;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import trzpoc.structure.TextMetricCalculator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

    private Properties p;

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
    @BeforeClass
    private void prepareFontsForTest(){
        try {
            p = new Properties();
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("fonts.properties");
            p.load(is);
            is.close();
            this.smallFont = this.loadSmallFont();
            this.bigFont = this.loadBigFont();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Font loadBigFont() {
        String font = this.p.getProperty(FontProperties.BIG_FONT.name());
        String weight = this.p.getProperty(FontProperties.BIG_FONT_WEIGHT.name());
        String size = this.p.getProperty(FontProperties.BIG_SIZE.name());
        return Font.font(font, FontWeight.findByName(weight), Integer.parseInt(size));
    }
    private Font loadSmallFont() {
        String font = this.p.getProperty(FontProperties.SMALL_FONT.name());
        String weight = this.p.getProperty(FontProperties.SMALL_FONT_WEIGHT.name());
        String size = this.p.getProperty(FontProperties.SMALL_SIZE.name());
        return Font.font(font, FontWeight.findByName(weight), Integer.parseInt(size));
    }

    @BeforeTest
    private void createNewInstance(){
        this.sut = FontAndColorSelector.getNewInstance();
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
    @Test
    public void testGetWidthForSmallFont(){
        int width = TextMetricCalculator.getInstance().calculateWidth("W", this.smallFont);
        assertEquals(width, this.sut.getWidthForSmallFont("W"));
        System.out.println("WIDTH FOR SMALL FONT = " + this.sut.getWidthForSmallFont("W"));
    }
    @Test
    public void testGetWidthForBigFont(){
        int width = TextMetricCalculator.getInstance().calculateWidth("W", this.bigFont);
        assertEquals(width, this.sut.getWidthForBigFont("W"));
        System.out.println("WIDTH FOR BIG FONT = " + this.sut.getWidthForBigFont("W"));
    }
    @Test
    public void testGetHeightForSmallFont(){
        int width = TextMetricCalculator.getInstance().calculateHeight("W", this.smallFont);
        assertEquals(width, this.sut.getHeightForSmallFont("W"));
        System.out.println("HEIGHT FOR SMALL FONT = " + this.sut.getHeightForSmallFont("W"));
    }
    @Test
    public void testGetHeightForBigFont(){
        int width = TextMetricCalculator.getInstance().calculateHeight("W", this.bigFont);
        assertEquals(width, this.sut.getHeightForBigFont("W"));
        System.out.println("HEIGHT FOR BIG FONT = " + this.sut.getHeightForBigFont("W"));
    }
    @Test
    public void testForGetWidthForFont(){
        int expectedValue = TextMetricCalculator.getInstance().calculateWidth("W", this.bigFont);
        assertEquals(expectedValue, this.sut.getWidthForFont(this.bigFont, "W"));

    }
    @Test
    public void testGetHeightForFont(){
        int expectedValue = TextMetricCalculator.getInstance().calculateHeight("W", this.bigFont);
        assertEquals(expectedValue, this.sut.getHeightForFont(this.bigFont,"W"));
    }
    private enum FontProperties{
        SMALL_FONT,SMALL_FONT_WEIGHT,SMALL_SIZE,BIG_FONT,BIG_FONT_WEIGHT,BIG_SIZE;
    }

}