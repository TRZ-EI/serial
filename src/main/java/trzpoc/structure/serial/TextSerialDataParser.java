package trzpoc.structure.serial;

import trzpoc.structure.Cell;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 10/04/17
 * Time: 10.57
 */
public class TextSerialDataParser implements SerialDataReader{

    // TO NOTE: text is variable in lenght. So, start at the end of the array
    // to find eol, crc. Start at the begin of the array to find font+color,
    // row pos, col pos. Text string is between these boundaries....

    private final int fontColorPos = 2;
    private final int fontColorLenght = 1;


    private final int rowPos = 3;
    private final int rowLenght = 2;

    private final int colPos = 5;
    private final int colLenght = 2;




    private final int crcLenght = 4;

    private final int eolLenght = 1;

    public Cell readByteArray(byte[] data) {
        return null;
    }
}
