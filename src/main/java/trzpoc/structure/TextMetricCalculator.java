package trzpoc.structure;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 19/03/17
 * Time: 14.53
 */
public class TextMetricCalculator {


    public static TextMetricCalculator getInstance() {
        return new TextMetricCalculator();
    }


    public int calculateWidth(String text, Font font){
        Text t = getTextByFont(text, font);
        return Double.valueOf(t.getLayoutBounds().getWidth()).intValue();
        //return 0;
    }
    public int calculateHeight(String text, Font font){
        Text t = getTextByFont(text, font);
        return Double.valueOf(t.getLayoutBounds().getHeight()).intValue();
    }
    public int calculateWidthOfSpace(Font font){
        Text t = getTextByFont(" ", font);
        return Double.valueOf(t.getLayoutBounds().getWidth()).intValue();
    }
    private Text getTextByFont(String text, Font font) {
        Text t = new Text(text);
        t.setFont(font);
        return t;
    }

}
