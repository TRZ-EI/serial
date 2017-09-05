package trzpoc.gui;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 02/09/17
 * Time: 15.01
 */

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import trzpoc.comunication.SerialDataManager;
import trzpoc.gui.GraphicDesigner;
import trzpoc.structure.CellsRow;
import trzpoc.structure.serial.SerialDataFacade;
import trzpoc.utils.ConfigurationHolder;
import trzpoc.utils.FontAndColorSelector;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * https://stackoverflow.com/a/44056730/230513
 */
public class MainForSerialData extends Application {

    private static final String DEFAULT_RESOURCE_FILE_NAME = "application.properties";
    private ConfigurationHolder configurationHolder;
    private FontAndColorSelector fontAndColorSelector;
    private SerialDataFacade serialDataFacade;
    private GraphicDesigner graphicDesigner;

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
        root.getChildren().add(canvasForGrid);

        this.graphicDesigner.setDataDisplayManager(this.serialDataFacade.getDisplayManager());
        this.graphicDesigner.drawGrid(canvasForGrid.getGraphicsContext2D());

        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        CanvasTask task = new CanvasTask(this.graphicDesigner, this.serialDataFacade);
        task.valueProperty().addListener((ObservableValue<? extends Canvas> observable, Canvas oldValue, Canvas newValue) -> {
            long start = System.nanoTime();
            root.getChildren().remove(oldValue);
            root.getChildren().add(newValue);
            double elapsed = (System.nanoTime() - start) / 1_000d;
            System.out.println("Elapsed time: " + elapsed);
        });
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
    private void readProperties() {
        this.resourceFile = (!this.getParameters().getRaw().isEmpty())? this.getParameters().getRaw().get(0): DEFAULT_RESOURCE_FILE_NAME;
        this.configurationHolder = ConfigurationHolder.createSingleInstanceByConfigUri(this.resourceFile);
    }

    private static class CanvasTask extends Task<Canvas> {

        private final SerialDataFacade serialDataFacade;
        private final GraphicDesigner graphicDesigner;
        private boolean runningflag = true;
        private BlockingQueue<String> serialBuffer;

        public CanvasTask(GraphicDesigner graphicDesigner, SerialDataFacade serialDataFacade){
            this.serialDataFacade = serialDataFacade;
            this.serialBuffer = new LinkedBlockingQueue<String>(2048);
            this.graphicDesigner = graphicDesigner;
        }

        @Override
        protected Canvas call() throws Exception {
            SerialDataManager sdm = SerialDataManager.createNewInstanceByQueue(this.serialBuffer);
            sdm.connectToSerialPort();

            Canvas canvas = null;
            while (this.runningflag) {
                while(!this.serialBuffer.isEmpty()){
                    String message = this.serialBuffer.take();
                    canvas = new Canvas(W, H);
                    canvas.setCache(false);
                    canvas.setCacheHint(CacheHint.QUALITY);

                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    this.serialDataFacade.onSerialDataInput(message.getBytes());
                    this.graphicDesigner.drawOnCanvas(serialDataFacade.getDisplayManager(), gc);
                    updateValue(canvas);
                }
            }
            sdm.disconnectFromSerialPort();
            return canvas;
        }
        public void setRunningflag(boolean runningflag) {
            this.runningflag = runningflag;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}