package trzpoc.structure.serial;

import com.opencsv.CSVReader;
import trzpoc.structure.Cell;
import trzpoc.structure.DataDisplayManager;
import trzpoc.structure.Variable;

import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 01/05/17
 * Time: 14.26
 *
 * Interface between data received from a serial port (mocked by a SerialDataMock)
 * and structure to print to RaspBerry screen
 */
public class SerialDataFacade {

    private DataDisplayManager displayManager;

    public static SerialDataFacade createNewInstance(){
        return new SerialDataFacade();
    }
    private SerialDataFacade(){
        this.displayManager = DataDisplayManager.getNewInstance();
    }

    public void onSerialDataInput(byte[] data) throws UnsupportedEncodingException {

        // first step: what type of action?
        char command = this.readCommandFromData(data);
        Cell dataParsed = null;

        // second step: On command received, parse data
        if (command == 'V'){ // Configure variable
            dataParsed = VariableConfiguratorSerialDataParser.getNewInstance().readByteArray(data);

        }else if (command == 'v'){ // Valorize variable
            dataParsed = VariableValueSerialDataParser.getInstance().readByteArray(data);

        }else if (command == 't'){ // Print text
            // TODO

        }else if (command == 'C'){ // Clear display
            // TODO

        }else if (command == 'B'){ // Bar configuration
            // TODO

        }
        this.displayManager.AddOrUpdateCellInMatrix(dataParsed);



    }

    private char readCommandFromData(byte[] data) {
        return ' ';
    }

    public DataDisplayManager fillMatrixWithData(String dataFileName) throws IOException {

        CSVReader reader = new CSVReader(new FileReader(dataFileName));
        String[] line;
        while ((line = reader.readNext()) != null) {
            this.displayManager.AddOrUpdateCellInMatrix(this.fillCell(line));
        }
        return this.displayManager;

    }

    private Cell fillCell(String[] line) {
        Cell toFill = null;
        String command = line[1];
        String id = line[2];
        String fontColor = line[3];
        String integerLenght = line[4];
        String decimalLenght = line[5];
        String row = line[6];
        String column = line[7];

        if (command.equals("V")){
            toFill = VariableConfiguratorSerialDataParser.getNewInstance().createVariable(fontColor.charAt(0));
            toFill.setId(Integer.valueOf(id).intValue());
            toFill.setyPos(Integer.valueOf(row).intValue());
            toFill.setxPos(Integer.valueOf(column).intValue());
            ((Variable)toFill).setIntegerLenght(Integer.valueOf(integerLenght).intValue());
            ((Variable)toFill).setDecimalLenght(Integer.valueOf(decimalLenght).intValue());
        }
        else if (command.equals("v")){
            toFill = Variable.getInstance();
            toFill.setId(Integer.valueOf(id).intValue());
            // TODO: complete with value
        }
        else if(command.equals("t")){
            // TODO: complete with text
        }
        else if(command.equals("C")){
            // TODO: complete with clear display
        }
        else if(command.equals("B")){
            // TODO: complete with bar display
        }
        return toFill;
    }


}
