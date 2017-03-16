package trzpoc.gui.dataProvider;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 12/03/17
 * Time: 12.33
 */
public class SerialReceiverMock {
    private Queue<CellContentBean> fifoBuffer;
    public static SerialReceiverMock getNewInstance(){
        return new SerialReceiverMock();
    }
    private SerialReceiverMock(){
        this.fifoBuffer = new LinkedList<CellContentBean>();
        this.fillBuffer();
    }
    public CellContentBean getReceivedString(){
        return this.fifoBuffer.remove();
    }
    public void pushReceivingString(CellContentBean message){
        this.fifoBuffer.add(message);
    }
    public boolean hasMoreMessages(){
        return !this.fifoBuffer.isEmpty();
    }
    private void fillBuffer(){
        //Label l = this.getRedLabelWithBigFont(0, 0,"TRZ TEST");
        Label l = this.getRedLabelWithSmallFont(0, 0,"TRZ TEST");
        Double w = l.getBounds().getWidth();
        this.fifoBuffer.add(l);

        this.fifoBuffer.add(this.getBlueLabelWithSmallFont(w.intValue(), l.getY(), "GOO"));
    }



    private Label getRedLabelWithSmallFont(int x, int y, String value) {
        Text text = new Text(value);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        text.getLayoutBounds().getHeight();


        Label label = Label.getLabelWithFontAndColor(Font.font("Arial", FontWeight.BOLD, 36), Color.RED);
        return getLabelFilledWithValues(x, y, value, label);
    }
    private Label getBlueLabelWithBigFont(int x, int y, String value) {
        Label label = Label.getLabelWithFontAndColor(Font.font("Arial", FontWeight.BOLD, 72), Color.BLUE);
        return getLabelFilledWithValues(x, y, value, label);
    }
    private Label getBlueLabelWithSmallFont(int x, int y, String value) {
        Label label = Label.getLabelWithFontAndColor(Font.font("Arial", FontWeight.BOLD, 36), Color.BLUE);
        return getLabelFilledWithValues(x, y, value, label);
    }
    private Label getColoredAndSixedLabel(Font font, Color color) {
        return Label.getLabelWithFontAndColor(font, color);
    }
    private Label getRedLabelWithBigFont(int x, int y, String value) {
        Label label = getColoredAndSixedLabel(Font.font("Arial", FontWeight.BOLD, 72),Color.RED);
        return getLabelFilledWithValues(x, y, value, label);
    }

    private Label getLabelFilledWithValues(int x, int y, String value, Label label) {
        label.setX(x);
        label.setY(y);
        label.setValue(value);
        return label;
    }
    private Bounds getBoundsForText(String message, Font font){
        Text text = new Text(message);
        text.setFont(font);
        return text.getLayoutBounds();


    }




}
