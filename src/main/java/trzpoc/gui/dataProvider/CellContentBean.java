package trzpoc.gui.dataProvider;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

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
    private Bounds bounds;


    public CellContentBean(){

    }

    public CellContentBean(int x, int y, Font font, Color color){
        this.x = x;
        this.y = y;
        this.font = font;
        this.color = color;
    }
    public CellContentBean(Font font, Color color){
        this.x = 0;
        this.y = 0;
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

    public Bounds getBounds(){
        return this.bounds;

    }
    public void setBound(Bounds bounds){
        this.bounds = bounds;

    }

    public void updateYpos() {
        Double height = this.bounds.getHeight();
        if (this.y - height.intValue() < 0){
            this.y = height.intValue();

        }

    }
}
