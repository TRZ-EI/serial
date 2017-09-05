package trzpoc.gui;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 02/09/17
 * Time: 15.01
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import trzpoc.structure.serial.SerialDataFacade;
import trzpoc.utils.ConfigurationHolder;
import trzpoc.utils.FontAndColorSelector;

/**
 * https://stackoverflow.com/a/44056730/230513
 */
public class MainForSerialData extends Application {

    private static final String DEFAULT_RESOURCE_FILE_NAME = "application.properties";
    private ConfigurationHolder configurationHolder;
    private FontAndColorSelector fontAndColorSelector;
    private SerialDataFacade serialDataFacade;
    private GraphicDesigner graphicDesigner;
    private CanvasTask task;

    private static final int W = 800;
    private static final int H = 480;
    private String resourceFile;







    @Override
    public void start(Stage stage) {
        this.readProperties();
        this.fontAndColorSelector = FontAndColorSelector.getNewInstance();
        this.serialDataFacade = SerialDataFacade.createNewInstance();
        this.graphicDesigner = GraphicDesigner.createNewInstanceByDebugParam(this.configurationHolder.getProperties().getProperty(ConfigurationHolder.DEBUG));

        stage.setTitle("TRZ Serial display");
        StackPane root = new StackPane();
        Canvas canvas = new Canvas(W, H);
        Canvas canvasForGrid = new Canvas(W, H);
        this.addMouseEventToExit(canvasForGrid);
        canvasForGrid.toFront();
        root.getChildren().add(canvasForGrid);

        this.graphicDesigner.setDataDisplayManager(this.serialDataFacade.getDisplayManager());
        this.graphicDesigner.drawGrid(canvasForGrid.getGraphicsContext2D());
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        this.addCombinationKeyAcceleratorToExit(stage);
        stage.show();
        this.task = new CanvasTask(this.graphicDesigner, this.serialDataFacade);
        this.task.valueProperty().addListener((ObservableValue<? extends Canvas> observable, Canvas oldValue, Canvas newValue) -> {
            long start = System.nanoTime();
            root.getChildren().remove(oldValue);
            root.getChildren().add(newValue);
            canvasForGrid.toFront();
            double elapsed = (System.nanoTime() - start) / 1_000d;
            System.out.println("Elapsed time: " + elapsed);
        });
        Thread thread = new Thread(this.task);
        thread.setDaemon(true);
        thread.start();
    }
    private void readProperties() {
        this.resourceFile = (!this.getParameters().getRaw().isEmpty())? this.getParameters().getRaw().get(0): DEFAULT_RESOURCE_FILE_NAME;
        this.configurationHolder = ConfigurationHolder.createSingleInstanceByConfigUri(this.resourceFile);
    }
    private void addMouseEventToExit(final Canvas canvas){
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                if (me.getButton() == MouseButton.PRIMARY){
                    try {
                        task.setRunningflag(false);
                        Platform.exit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
    public static void main(String[] args) {
        launch(args);
    }

}