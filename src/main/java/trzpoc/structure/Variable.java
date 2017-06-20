package trzpoc.structure;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;

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
        return Long.toString(this.value, 10);
    }

    public int getWidth() {

        return TextMetricCalculator.getInstance().calculateWidth(this.printFormattedValue(), this.getFont());
    }

    public String printFormattedValue() {
        String retValue = null;

        String initialValue = Long.toString(this.value, 10);
        boolean minus = (initialValue.startsWith("-"))?true: false;
        if (minus){
            initialValue = initialValue.replace("-", "");
        }
        if (initialValue.length() > this.integerLenght) {
            retValue = this.formatValueString(initialValue);
        }else if (initialValue.length() <= this.integerLenght){
            retValue = this.formatValueStringWithLessThanDigits(initialValue);
        }
        return (minus)? "-" + retValue: retValue;
    }

    private String formatValueStringWithLessThanDigits(String initialValue) {
        if(this.decimalLenght > 0){
        initialValue += ".";
            for (int i = 0; i < this.decimalLenght; i ++){
                initialValue += "0";
            }
        }
        return initialValue;
    }

    private String formatValueString(String initialValue) {
        if (this.integerLenght > 0 && this.decimalLenght >= 0) {
            String integerPart = initialValue.substring(0, initialValue.length() - this.decimalLenght);
            String decimalPart = initialValue.substring(initialValue.length() - this.decimalLenght);
            initialValue = integerPart + "." + decimalPart;
        }else if (this.integerLenght == 0){
            initialValue = "." + initialValue;
        }
        return initialValue;
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
