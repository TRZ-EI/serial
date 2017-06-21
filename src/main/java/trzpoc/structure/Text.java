package trzpoc.structure;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 27/03/17
 * Time: 15.37
 */
public class Text extends Cell{


    public static Text getNewInstance(){
        return new Text();
    }
    private Text(){
        super();
    }

    public static Text getNewInstanceByFontAndColor(Font font, Color color){
        return new Text(font, color);
    }
    private Text(Font font, Color color) {
        super(font, color);
    }

    @Override
    public Cell setValue(String value){
        super.setValue(value);
        return this;
    }

    @Override
    public String getValue() {
        return super.getValue();
    }

    public Text updateData(Text dataParsed) {
        if (this.equals(dataParsed)) {
            this.setValue(dataParsed.getValue());
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o instanceof Text){
            Text other = (Text) o;
            result = this.getxPos() == other.getxPos();
            result &= this.getyPos() == other.getyPos();
        }
        return result;
    }
}
