package trzpoc.structure;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 19/03/17
 * Time: 15.29
 */
public abstract class Cell implements CellInterface {

    private int id;
    private Color color;
    private Font font;
    private String value;
    private int xPos;
    private int yPos;
    private int pixelScreenYPos;
    private boolean isChanged;
    

    protected Cell(Font font, Color color) {
        this();
        this.color = color;
        this.font = font;
    }
    protected Cell() {
        this.isChanged = false;
    }


    public Color getColor() {
        return color;
    }

    public Cell setColor(Color color) {
        this.color = color;
        return this;
    }

    public Font getFont() {
        return font;
    }

    public Cell setFont(Font font) {
        this.font = font;
        return this;
    }

    @Override
    public String getValue(){
        return value;
    }

    public Cell setValue(String value) {
        if (value != null) {
            this.isChanged = !value.equals(this.value);
            this.value = value;
        }
        return this;
    }
    public int getPixelScreenXPos(){
        return this.xPos * TextMetricCalculator.getInstance().calculateWidth("W", this.font);
    }
    public int getPixelScreenYPosUpper(){
        return this.pixelScreenYPos - this.getHeight();
    }
    public int getWidth() {

        return TextMetricCalculator.getInstance().calculateWidth(this.getValue(), this.font);
    }
    public int getHeight() {

        return TextMetricCalculator.getInstance().calculateHeight(this.getValue(), this.font);
    }
    public int getxPos() {
        return xPos;
    }
    public Cell setxPos(int xPos) {
        this.xPos = xPos;
        return this;
    }
    public int getyPos() {
        return yPos;
    }

    public Cell setyPos(int yPos) {
        this.yPos = yPos;
        return this;
    }
    public int getId() {
        return id;
    }

    public Cell setId(int id) {
        this.id = id;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o instanceof Cell){
            Cell other = (Cell)o;
            result = this.xPos == other.getxPos();
            result &= this.yPos == other.getyPos();
            result &= this.id == other.getId();
        }
        return result;
    }

    public int getPixelScreenYPos() {
        return pixelScreenYPos;
    }

    public Cell setPixelScreenYPos(int pixelScreenYPos) {
        this.pixelScreenYPos = pixelScreenYPos;
        return this;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }
}
