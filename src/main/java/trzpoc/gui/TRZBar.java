package trzpoc.gui;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class TRZBar extends Rectangle {
    private Line zeroMark;
    private double minValue;
    private double maxValue;
    private double value;
    private final double RESOLUTION = 0.1D;

    private double zeroPosInPixel;
    private double positiveStepSizeinPixel;
    private double negativeStepSizeinPixel;

    public TRZBar(Group root){
        super();
        this.zeroMark = new Line();
        root.getChildren().add(this.zeroMark);


    }

    public void calculateBarParams() {
        double negativeRange = 0 - this.minValue;
        double positiveRange = this.maxValue - 0;
        positiveStepSizeinPixel = (this.getWidth() - this.zeroPosInPixel) / (positiveRange / RESOLUTION);
        negativeStepSizeinPixel = (this.zeroPosInPixel) / (negativeRange / RESOLUTION);
    }

    public void setupZeroBar(double i) {
        if (this.zeroMark == null){
            this.zeroMark = new Line();
        }

        double yPos = this.getY();
        this.zeroPosInPixel = calculateZeroPosInPixel(i);
        this.zeroMark.setStartX(this.zeroPosInPixel);
        this.zeroMark.setEndX(this.zeroPosInPixel);
        this.zeroMark.setStartY(yPos);
        this.zeroMark.setEndY(yPos + this.getHeight());
        this.zeroMark.setStroke(Color.RED);
        this.zeroMark.setStrokeWidth(3);
    }

    private double calculateZeroPosInPixel(double i) {
        double width = this.getWidth();
        double percentage = width * (1 - i/100);
        return width - percentage;
    }

    public void setZeroMark(Line zeroMark) {
        this.zeroMark = zeroMark;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public void setValue(double value) {
        this.value = value;
        this.drawValue(value);
    }

    private void drawValue(double value) {
        double sizeInPixel = 0D;

        if (value < 0){
            sizeInPixel = ((this.value * -1)/RESOLUTION) * this.negativeStepSizeinPixel;
            this.setX(this.zeroPosInPixel - sizeInPixel);
            this.setWidth(sizeInPixel);
            this.setFill(Color.GREEN);


        }else{
            sizeInPixel = (this.value/RESOLUTION) * this.positiveStepSizeinPixel;
            this.setX(this.zeroPosInPixel);
            this.setWidth(sizeInPixel);
            this.setFill(Color.GREEN);
        }
    }
}
