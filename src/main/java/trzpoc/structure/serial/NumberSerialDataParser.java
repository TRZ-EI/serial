package trzpoc.structure.serial;

import trzpoc.structure.Cell;
import trzpoc.structure.Variable;
import trzpoc.utils.DataTypesConverter;
import trzpoc.utils.FontAndColorSelector;
import trzpoc.utils.IdGeneratorByPosition;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 05/11/17
 * Time: 16.14
 */
// 01234567890123456
// ^nP20001C00000001
public class NumberSerialDataParser implements SerialDataReader {

    private DataTypesConverter converter;

    private final int fontColorPos = 2;
    private final int fontColorLenght = 1;

    private final int integerPartPos = 3;
    private final int integerPartLenght = 1;

    private final int decimalPartPos = 4;
    private final int decimalPartLenght = 1;

    private final int rowPos = 5;
    private final int rowLenght = 2;

    private final int colPos = 7;
    private final int colLenght = 2;

    private final int valuePos = 9;
    private final int valuelenght = 8;

    public NumberSerialDataParser(){
        this.converter = DataTypesConverter.getNewInstance();
    }


    @Override
    public Cell readByteArray(byte[] data) throws UnsupportedEncodingException {

        byte fontColor = Arrays.copyOfRange(data, fontColorPos, fontColorPos + fontColorLenght)[0];
        char fc = this.converter.byteToChar(fontColor);

        byte[] intPos = Arrays.copyOfRange(data, integerPartPos, integerPartPos + integerPartLenght);
        int integerDigits = this.converter.bytesToInt(intPos);

        byte[] dec = Arrays.copyOfRange(data, decimalPartPos, decimalPartPos + decimalPartLenght);
        int decimalDigits = this.converter.bytesToInt(dec);

        byte[] r = Arrays.copyOfRange(data, rowPos, rowPos + rowLenght);
        int row = this.converter.bytesToInt(r);

        byte[] c = Arrays.copyOfRange(data, colPos, colPos + colLenght);
        int column = this.converter.bytesToInt(c);

        byte[] valueInBytes = Arrays.copyOfRange(data, valuePos, valuePos + valuelenght);
        long value  = this.converter.bytesToLong(valueInBytes);

        Variable v = Variable.getInstance();
        v.setIntegerLenght(integerDigits).setDecimalLenght(decimalDigits).setxPos(column).setyPos(row).setValue(Long.toString(value));
        v.setFont(FontAndColorSelector.getNewInstance().selectFont(fc)).setColor(FontAndColorSelector.getNewInstance().selectColor(fc));
        v.setId(IdGeneratorByPosition.getNewInstanceByXAndY(column, row).invoke());






        return v;
    }
}
