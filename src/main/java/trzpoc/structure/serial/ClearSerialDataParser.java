package trzpoc.structure.serial;

import trzpoc.structure.Cell;
import trzpoc.structure.Clear;

import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 17/11/17
 * Time: 13.49
 */
public class ClearSerialDataParser implements SerialDataReader{

    public static ClearSerialDataParser getNewInstance(){
        return new ClearSerialDataParser();
    }
    private ClearSerialDataParser(){

    }
    @Override
    public Cell readByteArray(byte[] data) throws UnsupportedEncodingException {
        return new Clear();
    }
}
