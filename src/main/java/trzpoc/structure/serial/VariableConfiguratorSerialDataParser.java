package trzpoc.structure.serial;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import trzpoc.structure.Cell;
import trzpoc.structure.Variable;
import trzpoc.utils.DataTypesConverter;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 10/04/17
 * Time: 10.56
 */
public class VariableConfiguratorSerialDataParser implements SerialDataReader {

    // position and lenght for byte[] content (Variable configuration)

    private final int idVariablePos = 2;
    private final int idVariableLenght = 2;

    private final int fontColorPos = 4;
    private final int fontColorLenght = 1;

    private final int integerPartPos = 5;
    private final int integerPartLenght = 1;

    private final int decimalPartPos = 6;
    private final int decimalPartLenght = 1;

    private final int rowPos = 7;
    private final int rowLenght = 2;

    private final int colPos = 9;
    private final int colLenght = 2;

    private final int crcPos = 11;
    private final int crcLenght = 4;

    private final int eolPos = 15;
    private final int eolLenght = 1;


    private final char NERO_PICCOLO = 'P';     //0x31
    private final char ROSSO_PICCOLO = 'Q';
    private final char VERDE_PICCOLO = 'R';
    private final char BLU_PICCOLO = 'S';


    private final char NERO_GRANDE = '9';  //0x39
    private final char ROSSO_GRANDE = 'A';
    private final char VERDE_GRANDE = 'P';
    private final char BLU_GRANDE = 'C';

    private DataTypesConverter converter;

    public static VariableConfiguratorSerialDataParser getNewInstance() {
        return new VariableConfiguratorSerialDataParser();
    }

    public Cell readByteArray(byte[] data) throws UnsupportedEncodingException {
        byte[] varId = Arrays.copyOfRange(data, idVariablePos, idVariablePos + idVariableLenght);

        int id = this.converter.bytesToInt(varId);

        byte fontColor = Arrays.copyOfRange(data, fontColorPos, fontColorPos + fontColorLenght)[0];
        char fc = this.converter.byteToChar(fontColor);

        byte intPos = Arrays.copyOfRange(data, integerPartPos, integerPartPos + integerPartLenght)[0];
        char integerDigits = this.converter.byteToChar(intPos);

        byte dec = Arrays.copyOfRange(data, decimalPartPos, decimalPartPos + decimalPartLenght)[0];
        char decimalDigits = this.converter.byteToChar(dec);

        byte[] r = Arrays.copyOfRange(data, rowPos, rowPos + rowLenght);
        int row = this.converter.bytesToInt(r);

        byte[] c = Arrays.copyOfRange(data, colPos, colPos + colLenght);
        int column = this.converter.bytesToInt(c);

        byte[] crc = Arrays.copyOfRange(data, crcPos, crcPos + crcLenght);
        int crcValue = this.converter.notAsciiBytesToInt(crc);

        Variable retValue = this.createVariable(fc);
        retValue.setId(id).setIntegerLenght(integerDigits).setDecimalLenght(decimalDigits).setxPos(row).setyPos(column);


        return retValue;


    }

    public Variable createVariable(char fc) {
        Font bigFont = Font.font("Arial", FontWeight.NORMAL, 20);
        Font smallFont = Font.font("Arial", FontWeight.NORMAL, 16);

        Variable retValue = null;
        if (fc == NERO_PICCOLO){
            retValue = Variable.getInstanceByFontAndColor(smallFont, Color.BLACK);
        }
        else if (fc == ROSSO_PICCOLO){
            retValue = Variable.getInstanceByFontAndColor(smallFont, Color.RED);
        }
        else if (fc == VERDE_PICCOLO){
            retValue = Variable.getInstanceByFontAndColor(smallFont, Color.GREEN);
        }
        else if (fc == BLU_PICCOLO){
            retValue = Variable.getInstanceByFontAndColor(smallFont, Color.BLUE);
        }
        if (fc == NERO_GRANDE){
            retValue = Variable.getInstanceByFontAndColor(bigFont, Color.BLACK);
        }
        else if (fc == ROSSO_GRANDE){
            retValue = Variable.getInstanceByFontAndColor(bigFont, Color.RED);
        }
        else if (fc == VERDE_GRANDE){
            retValue = Variable.getInstanceByFontAndColor(bigFont, Color.GREEN);
        }
        else if (fc == BLU_GRANDE){
            retValue = Variable.getInstanceByFontAndColor(bigFont, Color.BLUE);
        }
        return retValue;

    }

    private VariableConfiguratorSerialDataParser(){
        this.converter = DataTypesConverter.getNewInstance();

    }



}
