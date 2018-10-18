package trzpoc.utils;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import trzpoc.structure.TextMetricCalculator;

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

    private final char NERO_PICCOLO = '1';     
    private final char ROSSO_PICCOLO = '2';
    private final char VERDE_PICCOLO = '3';
    private final char BLU_PICCOLO = '4';
    private final char ROSSO_PICCOLO_GRASSETTO = '6';


    private final char NERO_GRANDE = 'D';
    private final char ROSSO_GRANDE = 'E';
    private final char VERDE_GRANDE = 'F';
    private final char BLU_GRANDE = 'G';

    private FontAndColorSelector sut;
    private Font bigFont;
    private Font smallFont;
    private Font smallBoldFont;


    @DataProvider
    private Object[][] dataToTestForColor(){
        return new Object[][]{
            {NERO_PICCOLO, Color.BLACK},
            {NERO_GRANDE, Color.BLACK},
            {ROSSO_PICCOLO, Color.RED},
            {ROSSO_PICCOLO_GRASSETTO, Color.RED},
            {ROSSO_GRANDE, Color.RED},
            {BLU_PICCOLO, Color.BLUE},
            {BLU_GRANDE, Color.BLUE},
            {VERDE_PICCOLO, Color.web("00ff00")},
            {VERDE_GRANDE, Color.web("00ff00")},
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
            {ROSSO_PICCOLO_GRASSETTO, this.smallBoldFont},
            {BLU_GRANDE, this.bigFont},
            {VERDE_PICCOLO, this.smallFont},
            {VERDE_GRANDE, this.bigFont},
        };
    }
    private void prepareFontsForTest(){
        try {
            this.smallFont = this.loadSmallFont();
            this.smallBoldFont = this.loadSmallAndBoldFont();
            this.bigFont = this.loadBigFont();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadProperties(){
        ConfigurationHolder.createSingleInstanceByConfigUri(this.getClass().getClassLoader().getResource("application.properties").getFile()).getProperties();
    }

    private Font loadBigFont() {
        String font = ConfigurationHolder.getInstance().getProperties().getProperty(FontProperties.BIG_FONT.name());
        String weight = ConfigurationHolder.getInstance().getProperties().getProperty(FontProperties.BIG_FONT_WEIGHT.name());
        String size = ConfigurationHolder.getInstance().getProperties().getProperty(FontProperties.BIG_SIZE.name());
        return Font.font(font, FontWeight.findByName(weight), Integer.parseInt(size));
    }
    private Font loadSmallFont() {
        String font = ConfigurationHolder.getInstance().getProperties().getProperty(FontProperties.SMALL_FONT.name());
        String weight = ConfigurationHolder.getInstance().getProperties().getProperty(FontProperties.SMALL_FONT_WEIGHT.name());
        String size = ConfigurationHolder.getInstance().getProperties().getProperty(FontProperties.SMALL_SIZE.name());
        return Font.font(font, FontWeight.findByName(weight), Integer.parseInt(size));
    }
    private Font loadSmallAndBoldFont(){
        Font baseType = this.loadSmallFont();
        return Font.font(baseType.getName(), FontWeight.BOLD, baseType.getSize());
    }

    @BeforeMethod
    private void createNewInstance(){
        this.loadProperties();
        this.prepareFontsForTest();
        this.sut = FontAndColorSelector.getNewInstance();
    }

    @Test
    public void testGetNewInstance() throws Exception {
        assertNotNull(this.sut);
    }
    @Test
    public void testGetBigFont(){
        double actualValue = this.sut.getBigFont().getSize();
        // See value in properties file <fonts.properties>
        String bigSize = ConfigurationHolder.getInstance().getProperties().getProperty(FontProperties.BIG_SIZE.name());
        double expectedValue = Double.parseDouble(bigSize);
        assertEquals(expectedValue, actualValue);
    }
    @Test
    public void testGetSmallFont(){
        double actualValue = this.sut.getSmallFont().getSize();
        // See value in properties file <fonts.properties>
        String smallSize = ConfigurationHolder.getInstance().getProperties().getProperty(FontProperties.SMALL_SIZE.name());
        double expectedValue = Double.parseDouble(smallSize);
        assertEquals(expectedValue, actualValue);
    }
    @Test
    public void testGetSmallBoldFont(){
        Font actualValue = this.sut.getSmallBoldFont();
        double actualSize = actualValue.getSize();
        // See value in properties file <fonts.properties>
        String smallSize = ConfigurationHolder.getInstance().getProperties().getProperty(FontProperties.SMALL_SIZE.name());
        double expectedSize = Double.parseDouble(smallSize);
        assertEquals(expectedSize, actualSize);
        assertEquals(actualValue.getStyle().toLowerCase(), "bold");
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
    @Test
    public void testGetCOlorForBar(){
        Color actual = this.sut.getColorForBar();

        String  expectedColorInWebNotation = ConfigurationHolder.getInstance().getProperties().getProperty(FontProperties.BAR_COLOR.name());
        Color expected = Color.web(expectedColorInWebNotation);
        assertEquals(actual, expected);

    }
    private enum FontProperties{
        SMALL_FONT,SMALL_FONT_WEIGHT,SMALL_SIZE,BIG_FONT,BIG_FONT_WEIGHT,BIG_SIZE, BAR_COLOR;
    }

}