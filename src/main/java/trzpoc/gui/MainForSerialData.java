package trzpoc.gui;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 05/05/17
 * Time: 16.03
 */

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.stage.Stage;
import trzpoc.comunication.SerialCommunicator;
import trzpoc.crc.CRC16CCITT;
import trzpoc.crc.CRCCalculator;
import trzpoc.crc.Crc16CcittKermit;
import trzpoc.structure.CellsRow;
import trzpoc.structure.serial.MultipleCommandSplitter;
import trzpoc.structure.serial.SerialDataFacade;
import trzpoc.utils.ConfigurationHolder;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TooManyListenersException;


public class MainForSerialData extends Application{

    private GraphicDesigner graphicDesigner;
    protected Group root;
    protected Stage primaryStage;
    private String debug;
    private Canvas canvas;
    private Canvas canvasForGrid;
    private SerialDataFacade serialDataFacade;
    private MultipleCommandSplitter multipleCommandSplitter;

    private SerialCommunicator serialCommunicator;
    private final int NEW_LINE_ASCII = 10;

    private final String DEFAULT_RESOURCE_FILE_NAME = "application.properties";
    private final String DEFAULT_RESOURCE_KEY = "DEBUG";

    private SerialPort serialPort = null;

    private String resourceFile = null;



    private String readDebugValue() throws FileNotFoundException {
        Properties properties = new Properties();
        String retValue = "PRODUCTION"; // default value
        this.resourceFile = (!this.getParameters().getRaw().isEmpty())? this.getParameters().getRaw().get(0): DEFAULT_RESOURCE_FILE_NAME;
        try {
            ConfigurationHolder.createSingleInstanceByConfigUri(this.resourceFile);
            if (ConfigurationHolder.getInstance() != null) {
                retValue = ConfigurationHolder.getInstance().getProperties().getProperty(DEFAULT_RESOURCE_KEY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retValue;
    }

    private InputStream getInputStream(String resourceFile) throws FileNotFoundException {
        InputStream s;
        if (resourceFile != null && resourceFile.length() > 0){
            File aFile = new File(resourceFile);
            s = new FileInputStream(aFile);
        }else{
            s = this.getClass().getClassLoader().getResourceAsStream(DEFAULT_RESOURCE_FILE_NAME);
        }
        return s;
    }


    @Override
    public void start(Stage primaryStage) throws IOException, NoSuchPortException, PortInUseException {
        this.debug = this.readDebugValue();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("JavaFX Graphics Text for TRZ");

        this.root = new Group();
        Scene scene = new Scene(root);
        this.primaryStage.setScene(scene);
        this.addCombinationKeyAcceleratorToExit(primaryStage);
        this.canvas = new Canvas(800, 480);
        this.canvasForGrid = new Canvas(800, 480);



        // TODO: REMOVED SCROLLBAR TO MANAGE BARS
        /*
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(canvas);
        scrollPane.setPrefSize(800, 480);
        scrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        scrollPane.setFitToHeight(false);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        */
        root.getChildren().add(this.canvas);
        root.getChildren().add(this.canvasForGrid);
        this.canvas.toFront();
        this.addTouchEventToStart(this.canvas);
        this.addTouchEventToExit(this.canvasForGrid);
        this.addTouchEventToExit(this.canvas);
        this.addMouseEventToStart(this.canvas);
        this.primaryStage.show();
        this.graphicDesigner = GraphicDesigner.createNewInstanceByGroupAndCanvasAndDebugParam(root, this.canvas, this.debug);
        this.graphicDesigner.setCanvasForGrid(this.canvasForGrid);
        this.serialDataFacade = SerialDataFacade.createNewInstance();
        this.multipleCommandSplitter = MultipleCommandSplitter.getNewInstance();

        this.addListenerForDataChanged();
        boolean isConnected = this.connectToSerialPort();
        if (isConnected){
            System.out.println("Connected to " + this.serialPort.getName());
        }


    }
    private void addListenerForDataChanged(){
        this.serialDataFacade.getIsDataChanged().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (newValue){
                            graphicDesigner.clearScreen(serialDataFacade.getDisplayManager());
                        }

                    }
                });
            }
        });
    }
    private void addTouchEventToExit(Canvas canvas) {
        canvas.setOnTouchPressed(new EventHandler<TouchEvent>() {
            private int touches;
            private long firstTouch = 0;
            private long secondTouch = 0;
            public void handle(TouchEvent event) {
                firstTouch = (firstTouch == 0)? new Date().getTime(): firstTouch;
                secondTouch = (firstTouch > 0)? new Date().getTime(): secondTouch;

                touches += (touches < 2 && event.getEventSetId() == 1)? 1: 0;
                System.out.println(event.getEventSetId());
                System.out.println(firstTouch);
                System.out.println(secondTouch);
                if (touches == 2 && (secondTouch - firstTouch) > 0 && (secondTouch - firstTouch) <= 1000) {
                    Platform.exit();
                }
            }
        });


    }
    private void addTouchEventToStart(final Canvas canvas) {
        canvas.setOnTouchPressed(new EventHandler<TouchEvent>() {
            public void handle(TouchEvent event) {
                // TODO: start process on screen touch
                //SerialDataMock.getNewInstanceBySerialDataFacade(serialDataFacade).readData();
                event.consume();
            }

        });
    }
    private void addMouseEventToStart(final Canvas canvas){
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {
                if (me.getButton() == MouseButton.PRIMARY){
                    try {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                // TODO: start process on mouse action
                            }
                        });

                        me.consume();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else if (me.getButton() == MouseButton.SECONDARY) {
                    Platform.runLater(() ->{
                        // TODO: start process on mouse action
                    });
                    me.consume();
                }

            }
        });
    }
    private void addCombinationKeyAcceleratorToExit(Stage primaryStage) {
        primaryStage.getScene().getAccelerators().put(
                KeyCombination.keyCombination("CTRL+C"),
                new Runnable() {
                    public void run() {
                        Platform.exit();
                    }
                }
        );
    }
    public SerialDataFacade getSerialDataFacade(){
        return this.serialDataFacade;
    }

    public boolean connectToSerialPort() throws IOException, NoSuchPortException, PortInUseException {
        /*
        if (this.resourceFile != null){
            this.serialCommunicator = new SerialCommunicator(this.resourceFile);
        }else{
        }
        */
        this.serialCommunicator = new SerialCommunicator();
        this.serialPort = this.serialCommunicator.connectToSerialPort();
        boolean success = false;
        
        StringBuilder message = new StringBuilder();
        try {
            serialPort.addEventListener((SerialPortEvent serialPortEvent) -> {
                if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                    try {
                        int value = serialPort.getInputStream().available();
                        byte[] data = new byte[value];
                        int read = serialPort.getInputStream().read(data);

                        for (byte b: data){
                            message.append((char)b);
                        }
                        if (message.toString().indexOf(NEW_LINE_ASCII) > 0) {
                            final String[] messagesSent = message.toString().trim().split("\n");
                            message.setLength(0);
                            // ONLY ONE MESSAGE
                            boolean isValid = this.calculateCRC(messagesSent);
                            if (isValid) {
                                this.serialPort.getOutputStream().write(new byte[]{'O', 'K', '\n'});
                                this.serialPort.getOutputStream().flush();
                            }
                            else{
                                this.serialPort.getOutputStream().write(new byte[]{'K', 'O', '\n'});
                                this.serialPort.getOutputStream().flush();

                            }
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (messagesSent.length > 0 && isValid) {
                                            for (String tempValue: messagesSent){
                                                //tempValue += '\n';
                                                // TO MANAGE MULTIPLE COMMANDS IN A SINGLE ROW
                                                List<String> commands = multipleCommandSplitter.splitMultipleCommand(tempValue);
                                                for(String command: commands) {
                                                    command += '\n';
                                                    CellsRow aRow = serialDataFacade.onSerialDataInput(command.getBytes());
                                                    if (aRow != null) {
                                                        graphicDesigner.setDataDisplayManager(serialDataFacade.getDisplayManager());
                                                        graphicDesigner.drawASingleRowOnCanvas(aRow);
                                                    }
                                                }
                                            }
                                        }
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        String error = message.toString();
                        System.out.println("Failed to read data. (" + e.toString() + ")");
                    }
                }
            });
            serialPort.notifyOnDataAvailable(true);
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
        return success;
        }
    private boolean calculateCRC(String[] messages) {
        int score = 0;
        int crcDigits = 4; // 4 hex digits
        for (String message: messages){
            int size = message.length();
            if (size > crcDigits){
                String hexCrc = message.substring(size - crcDigits);
                String messageToCalculate = message.substring(0, size - crcDigits);
                CRCCalculator calculator = this.selectCalculator();
                int crc = calculator.calculateCRCForStringMessage(messageToCalculate);
                String crcHex = Integer.toHexString(crc);
                if (crcHex.length() < crcDigits){
                    crcHex = "0" + crcHex;
                }
                score += (hexCrc.equalsIgnoreCase(crcHex))? 1: 0;
            }
        }
        return (score == messages.length);
    }

    private CRCCalculator selectCalculator() {
        String crc = ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.CRC);
        return (crc.equalsIgnoreCase("kermit"))? Crc16CcittKermit.getNewInstance(): CRC16CCITT.getNewInstance();
    }

    public void disconnectFromSerialPort(){
        if(this.serialPort != null){
            System.out.println("disconnectFromSerialPort()");
            this.serialPort.removeEventListener();
            try {
                this.serialPort.getOutputStream().close();
                this.serialPort.getInputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.serialPort.close();
        }
    }
    @Override
    public void stop() throws Exception {
        this.disconnectFromSerialPort();
        super.stop();
    }


    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        //String[] myArgs = {"./application.properties"};
        //Application.launch(myArgs);
        Application.launch(args);
    }
}