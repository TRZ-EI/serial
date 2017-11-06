package trzpoc.structure.serial;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import trzpoc.structure.*;
import trzpoc.utils.DataTypesConverter;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Iterator;

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
    private final VariableCollector variableCollector;


    private DataDisplayManager displayManager;
    private DataTypesConverter dataTypesConverter;

    public static SerialDataFacade createNewInstance(){
        return new SerialDataFacade();
    }
    private SerialDataFacade(){
        this.variableCollector = VariableCollector.getSingleInstance();
        //this.displayManager = DataDisplayManager.getNewInstance().prepareDisplayMap(20);
        this.dataTypesConverter = DataTypesConverter.getNewInstance();
    }

    public Cell onSerialDataInput(byte[] data) throws UnsupportedEncodingException {
        Cell retValue = null;
        this.isDataChanged.set(false);
        Cell dataParsed = onSerialDataParser(data);
        if (dataParsed instanceof Clear){
            retValue = dataParsed;
            this.variableCollector.clear();
        }
        if (dataParsed instanceof Clear || dataParsed instanceof Text){
            retValue = dataParsed;

        }else {
            retValue = this.variableCollector.addOrGetUpdatedInstance(dataParsed);
//            CellsRow row = this.displayManager.addOrUpdateCellInMatrix(dataParsed);
//            retValue = row.addOrUpdateACell(dataParsed);
        }
        return retValue;
    }


    public Cell onSerialDataParser(byte[] data) throws UnsupportedEncodingException {
        // first step: what type of action?
        char command = this.readCommandFromData(data);
        Cell retValue = null;

        // second step: On command received, parse data
        if (command == 'V'){ // Configure variable
            retValue = VariableConfiguratorSerialDataParser.getNewInstance().readByteArray(data);
        }else if (command == 'v'){ // Valorize variable
            retValue = VariableValueSerialDataParser.getInstance().readByteArray(data);
        }else if (command == 't'){ // Print text
            retValue = TextSerialDataParser.getNewInstance().readByteArray(data);
        }else if (command == 'C'){ // Clear display
            retValue = new Clear();
        }else if (command == 'B'){ // Bar configuration
            retValue = BarSerialDataParser.getNewInstance().readByteArray(data);
        }
        return retValue;
    }

    private char readCommandFromData(byte[] data) {
        final int commandPos = 1;
        final int commandLenght = 1;
        byte byteCommand = Arrays.copyOfRange(data, commandPos, commandPos + commandLenght)[0];
        return this.dataTypesConverter.byteToChar(byteCommand);
    }


    public DataDisplayManager getDisplayManager() {
        return this.displayManager;
    }

    public BooleanProperty getIsDataChanged(){
        return this.isDataChanged;
    }

    public void addCanvasesToRootNode(Group root) {
        Iterator<CellsRow> iterator = this.displayManager.getRows().iterator();
        while(iterator.hasNext()){
            root.getChildren().add(iterator.next().getCanvas());
        }

    }

}
