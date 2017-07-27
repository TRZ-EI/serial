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
import trzpoc.comunication.SerialCommunicator;
import trzpoc.structure.serial.SerialDataFacade;
import trzpoc.utils.SerialDataMock;

import java.io.*;
import java.util.Date;
import java.util.Properties;


public class MainForSerialDataToTestCanvas extends Application{

    private GraphicDesigner graphicDesigner;
    protected Group root;
    protected Stage primaryStage;
    private String debug;
    private Canvas canvas;
    private Canvas canvasForGrid;
    private SerialDataFacade serialDataFacade;

    private SerialCommunicator serialCommunicator;
    private final int NEW_LINE_ASCII = 10;

    private final String DEFAULT_RESOURCE_FILE_NAME = "application.properties";
    private final String DEFAULT_RESOURCE_KEY = "DEBUG";

    private SerialPort serialPort = null;



    private String readDebugValue() throws FileNotFoundException {
        Properties properties = new Properties();
        String retValue = "PRODUCTION"; // default value
        String resourceFile = (!this.getParameters().getRaw().isEmpty())? this.getParameters().getRaw().get(0): null;
        try {
            InputStream s = this.getInputStream(resourceFile);
            properties.load(s);
            s.close();
            retValue = properties.getProperty(DEFAULT_RESOURCE_KEY);
        } catch (IOException e) {
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
        this.addMouseEventToStart(this.canvas);
        this.primaryStage.show();
        this.graphicDesigner = GraphicDesigner.createNewInstanceByGroupAndCanvasAndDebugParam(root, this.canvas, this.debug);
        this.graphicDesigner.setCanvasForGrid(this.canvasForGrid);
        this.serialDataFacade = SerialDataFacade.createNewInstance();



        //this.addListenerForSocketDataChanged();
        this.addListenerForDataChanged();


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
                                SerialDataMock.getNewInstanceBySerialDataFacade(serialDataFacade).simulateSerialReception();
                                canvas.toFront();

                                // TODO: start process on mouse action
                            }
                        });

                        me.consume();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else if (me.getButton() == MouseButton.SECONDARY) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                SerialDataMock.getNewInstanceBySerialDataFacade(serialDataFacade).sendRandomValuesForVariable();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }


                        }
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

    @Override
    public void stop() throws Exception {
        super.stop();
    }


    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        //String[] myArgs = {"./application.properties"};
        //Application.launch(myArgs);
        Application.launch(args);
    }
}