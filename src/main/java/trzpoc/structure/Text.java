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
        this.calculateId();
    }

    private void calculateId() {
        this.setId(0);
        // TODO: define better id creation
    }

    public static Text getNewInstanceByFontAndColor(Font font, Color color){
        return new Text(font, color);
    }
    private Text(Font font, Color color) {
        super(font, color);
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
}
