package trzpoc.structure.serial;

import trzpoc.structure.Cell;
import trzpoc.structure.Variable;
import trzpoc.utils.DataTypesConverter;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 10/04/17
 * Time: 10.57
 */
public class VariableValueSerialDataParser implements SerialDataReader{
    private final int idVariablePos = 2;
    private final int idVariableLenght = 2;

    private final int valuePos = 4;
    private final int valueLenght = 8;

    private final int crcPos = 12;
    private final int crcLenght = 4;

    private final int eolPos = 16;
    private final int eolLenght = 1;

    private DataTypesConverter converter;

    private VariableValueSerialDataParser(){
        this.converter = DataTypesConverter.getNewInstance();

    }

    @Override
    public Cell readByteArray(byte[] data) throws UnsupportedEncodingException {

        byte[] varId = Arrays.copyOfRange(data, idVariablePos, idVariablePos + idVariableLenght);
        int id = this.converter.bytesToInt(varId);

        byte [] varValue = Arrays.copyOfRange(data, valuePos, valuePos + valueLenght);
        long value = this.converter.bytesToLong(varValue);

        byte[] crcValue = Arrays.copyOfRange(data, crcPos, crcPos + crcLenght);
        int crc = this.converter.notAsciiBytesToInt(crcValue);

        return Variable.getInstance().setAConfiguration(false).setId(id).setValue(Long.toString(value));
    }

    public static VariableValueSerialDataParser getInstance() {
        return new VariableValueSerialDataParser();
    }
}
