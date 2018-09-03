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

    private double zeroPosInPixel;
    private double positiveStepSizeinPixel;
    private double negativeStepSizeinPixel;

    private Color color;

    private final double RESOLUTION = 0.1D;

    public TRZBar(){
        super();
    }

    public void calculateBarParams() {
        double negativeRange = 0 - this.minValue;
        double positiveRange = this.maxValue - 0;
        positiveStepSizeinPixel = (this.getWidth() - this.zeroPosInPixel) / (positiveRange / RESOLUTION);
        negativeStepSizeinPixel = (this.zeroPosInPixel) / (negativeRange / RESOLUTION);
    }

    public void setupZeroBar(double i, Group root) {
        if (this.zeroMark == null){
            this.zeroMark = new Line();
        }

        double yPos = this.getY();
        this.zeroPosInPixel = calculateZeroPosInPixel(i);
        this.zeroMark.setStartX(this.zeroPosInPixel);
        this.zeroMark.setEndX(this.zeroPosInPixel);
        this.zeroMark.setStartY(yPos);
        this.zeroMark.setEndY(yPos + this.getHeight());
        this.zeroMark.setStroke(Color.BLUE);
        this.zeroMark.setStrokeWidth(3);
        root.getChildren().add(this.zeroMark);

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

        Color actualColor = this.selectColorBasedOnValue(value);

        if (value < 0){
            sizeInPixel = ((this.value * -1)/RESOLUTION) * this.negativeStepSizeinPixel;
            this.setX(this.zeroPosInPixel - sizeInPixel);
            this.setWidth(sizeInPixel);
        }else{
            sizeInPixel = (this.value/RESOLUTION) * this.positiveStepSizeinPixel;
            this.setX(this.zeroPosInPixel);
            this.setWidth(sizeInPixel);
        }
        this.setFill(actualColor);
    }

    private Color selectColorBasedOnValue(double value) {
        return (value < this.minValue || value > this.maxValue)? Color.RED: this.color;

    }

    public void setColor(Color color) {
        this.color = color;
    }
}
