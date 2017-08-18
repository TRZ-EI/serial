package trzpoc.structure.serial;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import trzpoc.structure.Cell;
import trzpoc.structure.CellsRow;
import trzpoc.structure.DataDisplayManager;
import trzpoc.utils.DataTypesConverter;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

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

    private final BooleanProperty isDataChanged = new SimpleBooleanProperty(false);


    private DataDisplayManager displayManager;
    private DataTypesConverter dataTypesConverter;

    public static SerialDataFacade createNewInstance(){
        return new SerialDataFacade();
    }
    private SerialDataFacade(){
        this.displayManager = DataDisplayManager.getNewInstance().prepareDisplayMap(20);
        this.dataTypesConverter = DataTypesConverter.getNewInstance();
    }

    public CellsRow onSerialDataInput(byte[] data) throws UnsupportedEncodingException {
        //this.isDataChanged.set(false);
        // first step: what type of action?
        char command = this.readCommandFromData(data);
        Cell dataParsed = null;

        // second step: On command received, parse data
        if (command == 'V'){ // Configure variable
            dataParsed = VariableConfiguratorSerialDataParser.getNewInstance().readByteArray(data);
        }else if (command == 'v'){ // Valorize variable
            dataParsed = VariableValueSerialDataParser.getInstance().readByteArray(data);
        }else if (command == 't'){ // Print text
            dataParsed = TextSerialDataParser.getNewInstance().readByteArray(data);
        }else if (command == 'C'){ // Clear display
            // TODO
        }else if (command == 'B'){ // Bar configuration
            dataParsed = BarSerialDataParser.getNewInstance().readByteArray(data);
        }

        //this.isDataChanged.set(true);
        return this.displayManager.addOrUpdateCellInMatrix(dataParsed);
    }
    private char readCommandFromData(byte[] data) {
        final int commandPos = 1;
        final int commandLenght = 1;
        byte byteCommand = Arrays.copyOfRange(data, commandPos, commandPos + commandLenght)[0];
        return this.dataTypesConverter.byteToChar(byteCommand);
    }
/*
    public DataDisplayManager fillMatrixWithData(String dataFileName) throws IOException {

        CSVReader reader = new CSVReader(new FileReader(dataFileName));
        String[] line;
        while ((line = reader.readNext()) != null) {
            if (!line[0].startsWith("#")) {
                this.displayManager.addOrUpdateCellInMatrix(this.simulateSerialInput(line));
                // TODO: REMOVE METHOD simulateSerialInput FOR PRODUCTION
                // WHEN A SERIAL DATA RECEIVER RECEIVES A BYTE[], CALL METHOD onSerialDataInput, which is goal is
                // to parse raw data
            }
        }
        return this.displayManager;

    }
*/
/*
    private Cell simulateSerialInput(String[] line) throws UnsupportedEncodingException {
        Cell toFill = null;
        String command = line[1];
        SerialDataMock serialDataMock = new SerialDataMock();


        if (command.equals("V")){
            String id = line[2];
            char fontColor = line[3].charAt(0);
            char integerLenght = line[4].charAt(0);
            char decimalLenght = line[5].charAt(0);
            String row = line[6];
            String column = line[7];
            byte[] data = serialDataMock.prepareDataToConfigureAVariable(id, fontColor, integerLenght, decimalLenght, row, column);
            toFill = this.onSerialDataInput(data);
        }
        else if (command.equals("v")){
            String id = line[2];
            String value = line[3];
            byte[] data = serialDataMock.prepareDataToTransmitAVariable(id, Long.parseLong(value));
            toFill = this.onSerialDataInput(data);
        }
        else if(command.equals("t")){
            //TextSerialDataParser sdp = TextSerialDataParser.getNewInstance();
            byte[] data = serialDataMock.prepareDataToTransmitAText(line[2].charAt(0), line[3], line[4], line[5]);
            toFill = this.onSerialDataInput(data);
        }
        else if(command.equals("C")){
            // TODO: complete with clear display
        }
        else if(command.equals("B")){
             byte[] data = serialDataMock.prepareDataToTransmitABar(line[2], Long.parseLong(line[3]), Long.parseLong(line[4]), line[5]);
             toFill = this.onSerialDataInput(data);
        }
        return toFill;
    }
*/

    public DataDisplayManager getDisplayManager() {
        return this.displayManager;
    }

    public BooleanProperty getIsDataChanged(){
        return this.isDataChanged;
    }
}
