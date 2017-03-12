package trzpoc.gui.dataProvider;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 02/02/17
 * Time: 10.31
 */
public class Label extends CellContentBean {

    private String value;


    public Label(int x, int y, Font font, Color color){
        super(x, y, font, color);
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
