package trzpoc.structure;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import trzpoc.utils.FontAndColorSelector;

import static org.testng.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 05/06/17
 * Time: 9.50
 */
public class TextTest {
    private Text sut;
    private Font testFont = FontAndColorSelector.getNewInstance().getBigFont();
    private Color testColor = Color.BLACK;
    private final String TEST_STRING = "TEST_STRING";

    @BeforeMethod
    public void init(){
        this.sut = Text.getNewInstanceByFontAndColor(testFont, testColor);
        this.sut.setValue(TEST_STRING);
    }

    @Test
    public void testGetNewInstance() throws Exception {
        assertNotNull(this.sut);
    }
    @Test
    public void testGetNewInstanceByFontAndColor() throws Exception {
        assertEquals(testFont, this.sut.getFont());
        assertEquals(testColor, this.sut.getColor());
    }

    @Test
    public void testGetValue() throws Exception {
        assertEquals(this.sut.getValue(), TEST_STRING);

    }

    @Test
    public void testUpdateData() throws Exception {
        String aNewTest = "A new Test";
        Text textForUpdate = Text.getNewInstanceByFontAndColor(testFont, testColor);
        textForUpdate.setValue(aNewTest);
        this.sut.setValue(aNewTest);
        this.sut.updateData(textForUpdate);
        assertEquals(textForUpdate, sut);
    }
    @Test
    public void testEquals() throws Exception {
        String aNewTest = "A new Test";
        Text textForUpdate = Text.getNewInstanceByFontAndColor(testFont, testColor);
        textForUpdate.setValue(aNewTest);
        this.sut.setValue(aNewTest);
        assertTrue(this.sut.equals(textForUpdate));
    }
    @Test
    public void testNotEquals() throws Exception {
        String aNewTest = "A new Test";
        Text textForUpdate = Text.getNewInstanceByFontAndColor(testFont, testColor);
        textForUpdate.setValue(aNewTest);
        this.sut.setValue(aNewTest).setyPos(10).setyPos(10);
        assertFalse(this.sut.equals(textForUpdate));
    }
    @Test
    public void testIsChanged() throws Exception {
        String aNewTest = "A new Test";
        this.sut.setValue(aNewTest);
        assertTrue(this.sut.isChanged());
        this.sut.setValue(aNewTest);
        assertFalse(this.sut.isChanged());
    }
    @Test
    public void testGetUpperLeftCoordinatesOfContainerRectangle(){
        TextMetricCalculator calculator = TextMetricCalculator.getInstance();

        int x = 10, y = 10, pixelScreenYPos = 250;
        int expectedValueForX = x * calculator.calculateWidth("W", this.testFont);
        int expectedValueForY = pixelScreenYPos - calculator.calculateHeight("W", this.testFont);

        this.sut.setPixelScreenYPos(250);


        int actualValueForX = this.sut.setxPos(x).setyPos(y).getPixelScreenXPos();
        int actualValueForY = this.sut.getPixelScreenYPosUpper();
        assertEquals(actualValueForX, expectedValueForX);
        assertEquals(actualValueForY, expectedValueForY);
    }
    @Test
    public void testGetWidthAndHeightOfContainerRectangle(){
        TextMetricCalculator calculator = TextMetricCalculator.getInstance();
        int expectedWidth = calculator.calculateWidth(this.sut.getValue(), this.testFont);
        int expectedHeight = calculator.calculateHeight(this.sut.getValue(), this.testFont);
        int actualWidth = this.sut.getWidth();
        int actualHeight = this.sut.getHeight();
        assertEquals(actualWidth, expectedWidth);
        assertEquals(actualHeight, expectedHeight);
    }




}