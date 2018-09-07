package trzpoc.gui;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TRZBar extends Rectangle {
    private Line zeroMark;
    private double minValue;
    private double maxValue;
    private double value;

    private double zeroPosInPixel;
    private double positiveStepSizeinPixel;
    private double negativeStepSizeinPixel;
    private double initialWidth;

    private Color color;

    private double RESOLUTION = 0.1D;

    private static Logger logger = Logger.getLogger(TRZBar.class);

    public TRZBar(){
        super();
    }

    public TRZBar(double resolution){
        super();
        this.RESOLUTION = resolution;
    }

    public void calculateBarParams() {
        double negativeRange = 0 - this.minValue;
        double positiveRange = this.maxValue - 0;
        positiveStepSizeinPixel = round((this.getWidth() - this.zeroPosInPixel) / (positiveRange / RESOLUTION));
        negativeStepSizeinPixel = round((this.zeroPosInPixel) / (negativeRange / RESOLUTION));
        logger.debug("negativeRange: " + negativeRange);
        logger.debug("positiveRange: " + positiveRange);
        logger.debug("positiveStepSizeinPixel: " + positiveStepSizeinPixel + " px");
        logger.debug("negativeStepSizeinPixel: " + negativeStepSizeinPixel + " px");
        logger.debug("------------------------------------------------------------------------------------------------");
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
        double retValue = 0;
        this.initialWidth = this.getWidth();
        double percentage = this.round(this.initialWidth * (1 - i/100));
        retValue = this.initialWidth - percentage;
        logger.debug("defined width in pixel: " + this.initialWidth);
        logger.debug("percentage calc: " + this.initialWidth + " * (1 - " + i + "/100) --> " + percentage);
        logger.debug("zero position calc: " + this.initialWidth + " - " + percentage + " --> " + retValue + " px");
        return retValue;
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
        double roundedValue = this.round(value);
        Color actualColor = this.selectColorBasedOnValue(value);
        logger.debug("actual value: " + roundedValue + "; min value: " + this.minValue + "; max value: " + this.maxValue);
        logger.debug("Selected color based on value: " + this.printColorName(actualColor));
        if (value < 0){
            logger.debug("negative step size: " + this.negativeStepSizeinPixel);
            sizeInPixel = this.round(((this.value * -1)/RESOLUTION) * this.negativeStepSizeinPixel);
            logger.debug("Negative total range in pixel: 0 to "  + this.zeroPosInPixel);
            logger.debug("Rectangle size in pixel calc = (" + roundedValue + " * -1)/" + RESOLUTION + " * " + this.negativeStepSizeinPixel + " --> " + sizeInPixel);
            logger.debug("Rectangle x pos calc: " + this.zeroPosInPixel + " - " + sizeInPixel);
            logger.debug("Rectangle size in pixel: " + sizeInPixel + "; x pos: " + (this.zeroPosInPixel - sizeInPixel));
            this.setX(this.zeroPosInPixel - sizeInPixel);
            this.setWidth(sizeInPixel);
        }else{
            logger.debug("positive step size: " + this.positiveStepSizeinPixel);
            sizeInPixel = this.round((this.value/RESOLUTION) * this.positiveStepSizeinPixel);
            logger.debug("Positive total range in pixel: " + this.zeroPosInPixel + " to " + this.initialWidth);
            logger.debug("Rectangle size in pixel calc = (" + roundedValue +")/" + RESOLUTION + " * " + this.positiveStepSizeinPixel + " --> " + sizeInPixel);
            logger.debug("Rectangle size in pixel: " + sizeInPixel + "; x pos: " + this.zeroPosInPixel);
            this.setX(this.zeroPosInPixel);
            this.setWidth(sizeInPixel);
        }
        this.setFill(actualColor);
        logger.debug("************************************************************************************************");
    }

    private double round(double rawValue) {
        BigDecimal bd = new BigDecimal(rawValue);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    private Color selectColorBasedOnValue(double value) {
        double rounded = this.round(value);
        return (rounded < this.minValue || rounded > this.maxValue)? Color.RED: this.color;

    }
    private String printColorName(Color actualColor) {
        return (actualColor == Color.GREEN)? "GREEN": "RED";
   }

    public void setColor(Color color) {
        this.color = color;
    }
}
