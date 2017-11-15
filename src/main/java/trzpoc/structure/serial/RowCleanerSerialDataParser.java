package trzpoc.structure.serial;

import trzpoc.structure.Cell;
import trzpoc.structure.RowCleaner;
import trzpoc.utils.DataTypesConverter;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 13/11/17
 * Time: 20.31
 */
public class RowCleanerSerialDataParser implements SerialDataReader{
    private final int rowPos = 2;
    private final int rowLenght = 2;

    private DataTypesConverter converter;


    public static RowCleanerSerialDataParser getNewInstance() {
        return new RowCleanerSerialDataParser();
    }
    private RowCleanerSerialDataParser(){
        this.converter = DataTypesConverter.getNewInstance();
    }

    @Override
    public Cell readByteArray(byte[] data) throws UnsupportedEncodingException {
        byte[] r = Arrays.copyOfRange(data, rowPos, rowPos + rowLenght);
        int row = this.converter.bytesToInt(r);
        return RowCleaner.getInstanceByRowId(row);
    }
}
