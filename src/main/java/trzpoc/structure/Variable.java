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
    private int id;
    private int integerLenght;
    private int decimalLenght;

    public static Variable getInstance(){
        return new Variable();
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



    @Override
    public String getValue() {
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
        }
        return initialValue;
    }

    public Variable setValue(String value) {
        this.value = Long.valueOf(value, 10);
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

    public int getId() {
        return id;
    }

    public Variable setId(int id) {
        this.id = id;
        return this;
    }
    public boolean equals(Object o) {
        boolean  result = false;
        if (o instanceof Variable){
            Variable other = (Variable) o;
            result = super.equals(other);
            result &= this.id == other.getId();
        }
        return result;
    }
}
