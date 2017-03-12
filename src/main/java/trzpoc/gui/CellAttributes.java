package trzpoc.gui;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 16/01/17
 * Time: 16.35
 */
public class CellAttributes {

    private Font font;
    private Color color;

    private CellAttributes(Font font, Color color){
        this.color = color;
        this.font = font;

    }
    public static CellAttributes getInstanceByFontAndColor(Font font, Color color){
        return new CellAttributes(font, color);

    }
    public static CellAttributes getInstanceByDefault(){
        return new CellAttributes(new Font("Courier New", Font.PLAIN, 20), Color.BLACK);
    }
    public static CellAttributes getInstanceByColor(Color color){
        return new CellAttributes(new Font("Courier New", Font.PLAIN, 20), color);
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
