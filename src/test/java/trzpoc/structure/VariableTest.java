package trzpoc.structure;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 19/03/17
 * Time: 15.33
 */
public class VariableTest {
    private Variable sut;
    private Font refFont;

    @BeforeMethod
    private void prepareSutForTest(){
        this.refFont = Font.font("Serif", FontWeight.BOLD, 20);
        this.sut = (Variable) Variable.getInstanceByFontAndColor(refFont, Color.BLUE);
    }
    @DataProvider
    private Object[][] prepareDataForTest(){
        return new Object[][]{
                {"123456", 4, 2, "1234.56"},
                {"123456", 2, 4, "12.3456"},
                {"-123456",4, 2, "-1234.56"},
                {"-123456",2, 4, "-12.3456"},
                {"1", 1, 4, "0.0001"},
                {"-1", 1, 4, "-0.0001"},
                {"123456", 7, 2, "0001234.56"},
                {"-123456",7, 1, "-0012345.6"},
                {"5",0, 1, ".5"},
                {"100",3, 0, "100"},
                {"100",3, 1, "010.0"},
                {"1",3, 1, "000.1"}



        };
    }



    @Test
    public void testGetColor() throws Exception {
        assertEquals(this.sut.getColor(),Color.BLUE);

    }

    @Test
    public void testSetColor() throws Exception {
        Color c = Color.AQUA;
        assertEquals(c, this.sut.setColor(c).getColor());
    }

    @Test
    public void testGetFont() throws Exception {
        assertEquals(this.refFont, this.sut.getFont());
    }

    @Test
    public void testSetFont() throws Exception {
        assertEquals(this.refFont, this.sut.setFont(refFont).getFont());
    }

    @Test(dataProvider = "prepareDataForTest")
    public void testSetAndGetValue(String rawValue, int intPart, int decimals, String expectedValue) throws Exception {
        assertEquals(expectedValue, this.sut.setValue(rawValue).setIntegerLenght(intPart).setDecimalLenght(decimals).printFormattedValue());
    }
    @Test
    public void testGetWidth() throws Exception {
        int expectedWidth = this.calculateWidthOfText("100");
        this.sut.setIntegerLenght(3).setDecimalLenght(0);
        assertEquals(expectedWidth, this.sut.setValue("100").getWidth());
    }

    private int calculateWidthOfText(String test) {
        Text t = new Text(test);
        t.setFont(this.refFont);
        return Double.valueOf(t.getLayoutBounds().getWidth()).intValue();
    }

    @Test
    public void testGetHeight() throws Exception {
        int expectedHeight = this.calculateHeightOfText("100");
        this.sut.setIntegerLenght(3).setDecimalLenght(0);

        assertEquals(expectedHeight, this.sut.setValue("100").getHeight());
    }
    private int calculateHeightOfText(String test) {
        Text t = new Text(test);
        t.setFont(this.refFont);
        return Double.valueOf(t.getLayoutBounds().getHeight()).intValue();
    }

    @Test
    public void testSetAndGetxPos() throws Exception {
        int expectedValue = 100;
        assertEquals(expectedValue, this.sut.setxPos(expectedValue).getxPos());
    }
    @Test
    public void testSetAndGetyPos() throws Exception {
        int expectedValue = 100;
        assertEquals(expectedValue, this.sut.setyPos(expectedValue).getyPos());

    }
    @Test
    public void testEquals() throws Exception {
        Variable one = (Variable) Variable.getInstanceByFontAndColor(this.refFont, Color.BLUE).setId(10).setxPos(10).setyPos(10);
        Variable two = (Variable) Variable.getInstanceByFontAndColor(this.refFont, Color.STEELBLUE).setId(10).setxPos(10).setyPos(10);
        assertEquals(one, two);

    }

}