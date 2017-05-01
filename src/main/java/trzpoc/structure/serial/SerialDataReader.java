package trzpoc.structure.serial;

import trzpoc.structure.Cell;

import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 10/04/17
 * Time: 11.24
 */
public interface SerialDataReader {
    public Cell readByteArray(byte[] data) throws UnsupportedEncodingException;

}
