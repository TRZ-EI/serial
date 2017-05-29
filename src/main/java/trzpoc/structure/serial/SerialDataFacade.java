package trzpoc.structure.serial;

import com.opencsv.CSVReader;
import trzpoc.structure.Cell;
import trzpoc.structure.DataDisplayManager;
import trzpoc.structure.Variable;
import trzpoc.utils.SerialDataMock;

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
        this.displayManager.prepareDisplayMap(20);
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
        this.displayManager.addOrUpdateCellInMatrix(dataParsed);



    }

    private char readCommandFromData(byte[] data) {
        return ' ';
    }

    public DataDisplayManager fillMatrixWithData(String dataFileName) throws IOException {

        CSVReader reader = new CSVReader(new FileReader(dataFileName));
        String[] line;
        while ((line = reader.readNext()) != null) {
            if (!line[0].startsWith("#")) {
                this.displayManager.addOrUpdateCellInMatrix(this.fillCell(line));
            }
        }
        return this.displayManager;

    }

    private Cell fillCell(String[] line) throws UnsupportedEncodingException {
        Cell toFill = null;
        String command = line[1];

        if (command.equals("V")){
            String id = line[2];
            String fontColor = line[3];
            String integerLenght = line[4];
            String decimalLenght = line[5];
            String row = line[6];
            String column = line[7];
            toFill = VariableConfiguratorSerialDataParser.getNewInstance().createVariable(fontColor.charAt(0));
            toFill.setId(Integer.valueOf(id).intValue());
            toFill.setyPos(Integer.valueOf(row).intValue());
            toFill.setxPos(Integer.valueOf(column).intValue());
            ((Variable)toFill).setIntegerLenght(Integer.valueOf(integerLenght).intValue());
            ((Variable)toFill).setDecimalLenght(Integer.valueOf(decimalLenght).intValue());
        }
        else if (command.equals("v")){
            String id = line[2];
            String value = line[3];
            toFill = Variable.getInstance();
            toFill.setId(Integer.valueOf(id).intValue()).setValue(value);
            // TODO: complete with value
        }
        else if(command.equals("t")){
            TextSerialDataParser sdp = TextSerialDataParser.getNewInstance();
            SerialDataMock serialDataMock = new SerialDataMock();

            byte[] data = serialDataMock.prepareDataToTransmitAText(line[2].charAt(0), line[3], line[4], line[5]);
            toFill = sdp.readByteArray(data);
        }
        else if(command.equals("C")){
            // TODO: complete with clear display
        }
        else if(command.equals("B")){
             BarSerialDataParser barSerialDataParser = BarSerialDataParser.getNewInstance();
             SerialDataMock serialDataMock = new SerialDataMock();
             byte[] data = serialDataMock.prepareDataToTransmitABar(line[2], Long.parseLong(line[3]), Long.parseLong(line[4]), line[5]);
             toFill = barSerialDataParser.readByteArray(data);
            // TODO: complete with bar display
        }
        return toFill;
    }


}
