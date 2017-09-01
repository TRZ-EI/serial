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
import trzpoc.comunication.SerialDataManager;
import trzpoc.structure.serial.SerialDataFacade;
import trzpoc.utils.ConfigurationHolder;

import java.io.*;
import java.util.Date;
import java.util.Properties;


public class MainForSerialData extends Application{

    private GraphicDesigner graphicDesigner;
    protected Group root;
    protected Stage primaryStage;
    private String debug;
    private Canvas canvas;
    private Canvas canvasForGrid;
    private SerialDataFacade serialDataFacade;
    private SerialDataManager serialDataManager;
    private GraphicDesignerRunnable runnable;



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
        this.serialDataManager = SerialDataManager.createNewInstance();
        this.runnable = GraphicDesignerRunnable.createNewInstanceBySerialDataFacadeAndGraphicDesigner(serialDataFacade, graphicDesigner);

        this.addListenerForDataChanged();
        this.addListenerForDataReceived();
        boolean isConnected = this.connectToSerialPort();
        if (isConnected){
            System.out.println("Connected to serial port");
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
    private void addListenerForDataReceived(){
        this.serialDataManager.getIsDataAvalaible().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue){
                    while(!serialDataManager.getSerialBuffer().isEmpty()){
                        String[] y = serialDataManager.getSerialBuffer().toArray(new String[0]);
                        serialDataManager.getSerialBuffer().clear();
                        System.out.println("MESSAGES READ: " + y.length);
                        runnable.setMessages(y);
                        Platform.runLater(runnable);
                        serialDataManager.setIsDataAvalaible(false);
                    }
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

                        String[] values = {"^V07A310509f465\n","^v0700000064d0b0\n","^v07000000c8e076\n","^v070000012c0156\n","^v0700000190b73a\n","^v07000001f4e93f\n","^v07000002582a0f\n","^v07000002bc56b9\n","^v070000032005a0\n","^v0700000384aaef\n","^v07000003e81380\n","^v070000044c4000\n","^v07000004b08e8f\n","^v0700000514a2d7\n","^v0700000578c9fd\n","^v07000005dc798f\n","^v070000064044f6\n","^v07000006a4f538\n","^v07000007083e0a\n","^v070000076c7f32\n","^v07000007d07d79\n","^v070000083486e4\n","^v0700000898a8a3\n","^v07000008fc5dbc\n","^v07000009600ea5\n","^v07000009c4bf6b\n\n","^v0700000a28b406\n","^v0700000a8cb053\n","^v0700000af0f775\n","^v0700000b54b54d\n","^v0700000bb8e3e9\n","^v0700000c1c64ab\n","^v0700000c80b4a5\n"};
                        runnable.setMessages(values);
                        for (int i = 0; i < 10000; i ++) {
                            Platform.runLater(runnable);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                else if (me.getButton() == MouseButton.SECONDARY) {
                    Platform.runLater(() ->{
                        // TODO: start process on mouse action
                    });
                }
                me.consume();

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
        return this.serialDataManager.connectToSerialPort();
    }


    public void disconnectFromSerialPort(){
        this.serialDataManager.disconnectFromSerialPort();
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