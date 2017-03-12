package trzpoc.gui.dataProvider;

import java.awt.Color;
import java.awt.Font;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 02/02/17
 * Time: 10.27
 */
public abstract class CellContentBean {

    private int x;
    private int y;
    private Font font;
    private Color color;

    public CellContentBean(){

    }

    public CellContentBean(int x, int y, Font font, Color color){
        this.x = x;
        this.y = y;
        this.font = font;
        this.color = color;
    }



    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
