package trzpoc.gui;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 05/05/17
 * Time: 16.03
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.stage.Stage;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortException;
import socketserverfx.FXMLSocketController;
import trzpoc.comunication.jssc.JavaFxJssc;
import trzpoc.structure.serial.SerialDataFacade;
import trzpoc.utils.DataTypesConverter;
import trzpoc.utils.SerialDataMock;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static jssc.SerialPort.MASK_RXCHAR;

public class MainForSerialData extends Application{

    private GraphicDesigner graphicDesigner;
    protected Group root;
    protected Stage primaryStage;
    private String debug;
    private Canvas canvas;
    private Canvas canvasForGrid;
    private SerialDataFacade serialDataFacade;

    private FXMLSocketController socketController;

    private SerialPort serialPort = null;



    private String readDebugValue(){
        final String resourceFileName = "application.properties";
        String retValue = "PRODUCTION"; // default value
        try {
            Properties properties = new Properties();
            InputStream s = this.getClass().getClassLoader().getResourceAsStream(resourceFileName);
            properties.load(s);
            s.close();
            retValue = properties.getProperty("DEBUG");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retValue;
    }


    @Override
    public void start(Stage primaryStage) {
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
        this.addMouseEventToStart(this.canvas);
        this.primaryStage.show();
        this.graphicDesigner = GraphicDesigner.createNewInstanceByGroupAndCanvasAndDebugParam(root, this.canvas, this.debug);
        this.graphicDesigner.setCanvasForGrid(this.canvasForGrid);
        this.serialDataFacade = SerialDataFacade.createNewInstance();


        this.socketController = new FXMLSocketController();
        this.socketController.initialize(null, null);
        this.socketController.activateConnection();

        this.addListenerForSocketDataChanged();
        this.addListenerForDataChanged();
        /*
        boolean isConnected = this.connectToSerialPort("/dev/ttyACM0");
        if (isConnected){
            System.out.println("Connected to /dev/ttyACM0");
        }
        */


    }
    private void addListenerForDataChanged(){
        this.serialDataFacade.getIsDataChanged().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (newValue){
                            graphicDesigner.drawOnCanvas(serialDataFacade.getDisplayManager());
                        }

                    }
                });
            }
        });
    }
    private void addListenerForSocketDataChanged(){
        this.socketController.getReceivedMsgData().addListener(new ListChangeListener<Byte>() {
            @Override
            public void onChanged(Change<? extends Byte> c) {
                Byte[] data = new Byte[socketController.getReceivedMsgData().size()];

                socketController.getReceivedMsgData().toArray(data);
                socketController.getReceivedMsgData().clear();
                byte[] inputData = new byte[data.length];
                for (int i = 0; i < data.length; i ++){
                    inputData[i] = data[i].byteValue();
                }
                try {
                    serialDataFacade.onSerialDataInput(inputData);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

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
                SerialDataMock.getNewInstanceBySerialDataFacade(serialDataFacade).readData();
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
                                SerialDataMock.getNewInstanceBySerialDataFacade(serialDataFacade).readData();
                            }
                        });

                        me.consume();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else if (me.getButton() == MouseButton.SECONDARY) {
                    Platform.runLater(() ->{
                        SerialDataMock.getNewInstanceBySerialDataFacade(serialDataFacade).simulateSerialReception("2");
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
/*
    public Canvas getCanvas() {
        return canvas;
    }

    public Group getRoot() {
        return root;
    }

    public String getDebug() {
        return debug;
    }

*/
    public SerialDataFacade getSerialDataFacade(){
        return this.serialDataFacade;
    }

    public boolean connectToSerialPort(String port){

        System.out.println("connectToSerialPort");

        boolean success = false;
        this.serialPort = new SerialPort(port);
        try {
            serialPort.openPort();
            serialPort.setParams(
                    SerialPort.BAUDRATE_115200,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.setEventsMask(MASK_RXCHAR);
            serialPort.addEventListener((SerialPortEvent serialPortEvent) -> {
                if(serialPortEvent.isRXCHAR()){
                    try {

                        byte[] b = serialPort.readBytes();

                        DataTypesConverter converter = DataTypesConverter.getNewInstance();
                        // TODO: CRC control implementation and send response


                        
                        String st = converter.bytesToString(b);
                        String result = (st.indexOf('\r') > 0)? st.substring(0, st.indexOf('\r')): st;
                        Platform.runLater(() ->{
                            System.out.println(result);
                            SerialDataMock.getNewInstanceBySerialDataFacade(serialDataFacade).simulateSerialReception(result);
                        });

                    } catch (SerialPortException ex) {
                        Logger.getLogger(JavaFxJssc.class.getName())
                                .log(Level.SEVERE, null, ex);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }
            });
            success = true;
        } catch (SerialPortException ex) {
            //Logger.getLogger(JavaFxJssc.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("SerialPortException: " + ex.toString());
        }


        return success;
    }

    public void disconnectFromSerialPort(){

        System.out.println("disconnectFromSerialPort()");
        if(this.serialPort != null){
            try {
                this.serialPort.removeEventListener();

                if(this.serialPort.isOpened()){
                    this.serialPort.closePort();
                }

                this.serialPort = null;
            } catch (SerialPortException ex) {
                Logger.getLogger(JavaFxJssc.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void stop() throws Exception {
        this.disconnectFromSerialPort();
        this.socketController.disconnect();
        super.stop();
    }



    public static void main(String[] args) {
        Application.launch(args);
    }
}