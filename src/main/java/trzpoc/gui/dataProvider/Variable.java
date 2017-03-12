package trzpoc.gui.dataProvider;

import java.awt.Color;
import java.awt.Font;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 02/02/17
 * Time: 10.31
 */
public class Variable extends CellContentBean {

    private String name;
    private String value;


    public Variable(int x, int y, Font font, Color color){
        super(x, y, font, color);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
