package trzpoc.structure;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.text.DecimalFormat;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 27/03/17
 * Time: 11.41
 */
public class Variable extends Cell {

    private long value;
    private int integerLenght;
    private int decimalLenght;

    private boolean isAConfiguration = false;

    public static Variable getInstance(){
        Variable retValue = new Variable();
        retValue.setxPos(-1).setyPos(-1);
        return retValue;
    }


    public static Variable getInstanceByFontAndColor(Font font, Color color){
        return new Variable(font, color);

    }

    private Variable() {
        super();
    }
    private Variable(Font font, Color color) {
        super(font, color);
    }

    public String getValue(){
        return Long.toString(this.value);
    }

    public int getWidth() {

        return TextMetricCalculator.getInstance().calculateWidth(this.printFormattedValue(), this.getFont());
    }

    public String printFormattedValue() {
        boolean negative = false;
        long tempValue = this.value;
        if (this.value < 0){
            negative = true;
            tempValue *= -1;
        }
        int divisor = 1;
        for (int i = 0; i < this.decimalLenght; i ++){
            divisor *= 10;
        }
        double calculatedValue = (double)tempValue / divisor;
        String formattedValue = new DecimalFormat(this.createFormat()).format(calculatedValue);
        int integerPlaces = formattedValue.indexOf('.');
        if (integerPlaces >= 0 && integerPlaces < this.integerLenght){
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.integerLenght - integerPlaces; i ++){
                sb.append("0");
            }
            formattedValue = sb.toString() + formattedValue;
        }
        if (negative){
            formattedValue = "-" + formattedValue;
        }
        return formattedValue;
    }
    private String createFormat(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.integerLenght; i++){
            builder.append("#");
        }
        if (this.decimalLenght > 0) {
            builder.append(".");
            for (int i = 0; i < this.decimalLenght; i++) {
                builder.append("0");
            }
        }
        return builder.toString();
    }

    public Variable setValue(String value) {
        if (value != null){
            long tempValue = Long.valueOf(value, 10);
            this.setChanged(this.value != tempValue);
            this.value = tempValue;
        }
        return this;
    }

    public Variable setDecimalLenght(int decimalLenght) {
        this.decimalLenght = decimalLenght;
        return this;
    }
    public Variable setIntegerLenght(int integerLenght) {
        this.integerLenght = integerLenght;
        return this;
    }


    public void updateData(Variable d) {
        this.value = d.value;
        this.decimalLenght = d.decimalLenght;
        this.integerLenght = d.integerLenght;
    }

    public int getIntegerLenght() {
        return integerLenght;
    }

    public int getDecimalLenght() {
        return decimalLenght;
    }

    public boolean isAConfiguration() {
        return isAConfiguration;
    }

    public Variable setAConfiguration(boolean AConfiguration) {
        isAConfiguration = AConfiguration;
        return this;
    }
}
