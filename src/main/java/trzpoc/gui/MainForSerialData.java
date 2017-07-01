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
import trzpoc.structure.serial.SerialDataFacade;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
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

    private SerialCommunicator serialCommunicator;
    private final int NEW_LINE_ASCII = 10;


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
        this.addMouseEventToStart(this.canvas);
        this.primaryStage.show();
        this.graphicDesigner = GraphicDesigner.createNewInstanceByGroupAndCanvasAndDebugParam(root, this.canvas, this.debug);
        this.graphicDesigner.setCanvasForGrid(this.canvasForGrid);
        this.serialDataFacade = SerialDataFacade.createNewInstance();



        //this.addListenerForSocketDataChanged();
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
                            graphicDesigner.drawOnCanvas(serialDataFacade.getDisplayManager());
                        }

                    }
                });
            }
        });
    }
    /*
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
    */

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
        this.serialCommunicator = new SerialCommunicator();
        this.serialPort = this.serialCommunicator.connectToSerialPort();


        boolean success = false;
        
        StringBuilder message = new StringBuilder();
        try {
            serialPort.addEventListener((SerialPortEvent serialPortEvent) -> {
                if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                    try {
                        Thread.sleep(400);
                        int value = serialPort.getInputStream().available();
                        byte[] data = new byte[value];
                        int read = serialPort.getInputStream().read(data);
                        for (byte b: data){
                            message.append((char)b);
                        }

                        
                        String toSplit = message.toString();
                        String[] messages = toSplit.split("\n");
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    message.setLength(0);
                                    for(String s: messages){
                                        s = s.trim();
                                        if (s.length() > 0 && s.indexOf('^') == 0){
                                            s += '\n';
                                            serialDataFacade.onSerialDataInput(s.getBytes());
                                        }
                                    }
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        // TODO: CRC control implementation and send response
                    } catch (Exception e) {
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






/*

                if(serialPortEvent.isRXCHAR()){

                    boolean receivingMessage = false;
                    try {
                        //Thread.sleep(30);
                        byte[] buffer = serialPort.readBytes();
                        for (byte b: buffer) {
                            if (b == '^') {
                                receivingMessage = true;

                                message.setLength(0);
                                message.append((char)b);
                            }
                            else if (receivingMessage == true) {
                                if (b == '\r') {
                                    receivingMessage = false;
                                    message.append('\n');
                                    String toProcess = message.toString();
                                    Platform.runLater(new Runnable() {
                                        @Override public void run() {
                                            try {
                                                serialDataFacade.onSerialDataInput(toProcess.getBytes());
                                            } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                                else {
                                    message.append((char)b);
                                }
                            }
                        }




                        //DataTypesConverter converter = DataTypesConverter.getNewInstance();



                        
                        //String st = converter.bytesToString(b);
                        //String st = serialPort.readString();
                        //String[] results = st.split("\r\n");


                        //String result = (st.indexOf("\r\n") > 0)? st.substring(0, st.indexOf('\r')): st;
                        /*
                        Platform.runLater(() ->{
                            for (String result: results) {
                                result += '\n';
                                System.out.println(result);
                                try {
                                    serialDataFacade.onSerialDataInput(result.getBytes());
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                //SerialDataMock.getNewInstanceBySerialDataFacade(serialDataFacade).simulateSerialReception(result);
                            }

                        });

                    } catch (SerialPortException ex) {
                        Logger.getLogger(JavaFxJssc.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                }
            });
            success = true;
        } catch (SerialPortException ex) {
            //Logger.getLogger(JavaFxJssc.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("SerialPortException: " + ex.toString());
        }


        return success;
        */

    public void disconnectFromSerialPort(){

        System.out.println("disconnectFromSerialPort()");
        if(this.serialPort != null){
                this.serialCommunicator.disconnect();
        }
    }

    @Override
    public void stop() throws Exception {
        this.disconnectFromSerialPort();
        super.stop();
    }



    public static void main(String[] args) {
        Application.launch(args);
    }
}