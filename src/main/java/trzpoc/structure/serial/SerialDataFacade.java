package trzpoc.structure.serial;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import trzpoc.structure.Cell;
import trzpoc.structure.CellsRow;
import trzpoc.structure.DataDisplayManager;
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


    private DataDisplayManager displayManager;
    private DataTypesConverter dataTypesConverter;

    public static SerialDataFacade createNewInstance(){
        return new SerialDataFacade();
    }
    private SerialDataFacade(){
        //this.displayManager = DataDisplayManager.getNewInstance().prepareDisplayMap(20);
        this.dataTypesConverter = DataTypesConverter.getNewInstance();
    }

    public CellsRow onSerialDataInput(byte[] data) throws UnsupportedEncodingException {
        this.isDataChanged.set(false);
        Cell dataParsed = onSerialDataParser(data);


        return this.displayManager.addOrUpdateCellInMatrix(dataParsed);
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
            this.isDataChanged.set(true);
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
