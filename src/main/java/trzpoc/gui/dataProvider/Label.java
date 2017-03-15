package trzpoc.gui.dataProvider;


import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 02/02/17
 * Time: 10.31
 */
public class Label extends CellContentBean {

    private String value;


    public static Label getLabelWithFontAndColor(Font font, Color color){
        return new Label(font, color);
    }
    public static Label getCompleteLabel(int x, int y, Font font, Color color){
        return new Label(x, y, font, color);
    }

    public void setValue(String value) {
        this.value = value;
        Text text = new Text(value);
        text.setFont(this.getFont());
        this.setBound(text.getLayoutBounds());
        this.updateYpos();
    }


    private Label(int x, int y, Font font, Color color){
        super(x, y, font, color);


    }
    public String getValue() {
        return value;
    }
    private Label(Font font, Color color) {
        super(font, color);
    }

}
