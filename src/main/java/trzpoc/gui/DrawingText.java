

package trzpoc.gui;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.TouchEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import trzpoc.comunication.SerialDataManager;
import trzpoc.structure.Cell;
import trzpoc.structure.StructureVisitor;
import trzpoc.structure.serial.SerialDataFacade;
import trzpoc.utils.ConfigurationHolder;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;


public class DrawingText extends Application {

    private Group root;
    private BlockingQueue<String> serialBuffer;
    private SerialDataFacade facade;
    private SerialDataManager serialDataManager;

    private Multimap<Integer, javafx.scene.Node> rows = ArrayListMultimap.create();

    private final String DEFAULT_RESOURCE_FILE_NAME = "application.properties";
    private StructureVisitor visitor;
    private Canvas canvasForGrid;

    private MyRunnable myRunnable;



    @Override public void start(Stage stage) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
        this.readProperties();
        this.visitor = new StructureVisitor(this);
        this.facade = SerialDataFacade.createNewInstance();
        this.serialBuffer = new LinkedBlockingQueue<>();
        this.root = new Group();
        Scene scene = new Scene(root, 800d, 480d, Color.WHITE);
        this.drawGridOnCanvas();

        stage.setScene(scene);
        stage.setTitle("TRZ bar for instruments");
        this.addCombinationKeyAcceleratorToExit(stage);
        this.addCombinationKeyAcceleratorToClerScreen(stage);
        stage.show();


        this.serialDataManager = SerialDataManager.createNewInstanceBySerialBuffer(serialBuffer);
        this.serialDataManager.setMain(this);
        Thread serialThread = new Thread(serialTask);
        serialThread.setDaemon(false);
        serialThread.start();

        this.myRunnable = new MyRunnable(this.serialBuffer);

    }

    public void drawGridOnCanvas() {
        this.canvasForGrid = this.getSingleTonCanvasForGrid();
        this.root.getChildren().add(this.canvasForGrid);
        this.canvasForGrid.toFront();
    }

    private Canvas getSingleTonCanvasForGrid() {
        if (this.canvasForGrid == null){
            this.canvasForGrid = new Canvas(800d,480d);
            GraphicDesigner.createNewInstance().drawGridForGraphicHelp(this.canvasForGrid);
            this.addTouchEventToExit(this.canvasForGrid);
        }
        return this.canvasForGrid;


    }

    private void addCombinationKeyAcceleratorToExit(Stage primaryStage) {
        primaryStage.getScene().getAccelerators().put(
                KeyCombination.keyCombination("CTRL+C"),
                new Runnable() {
                    public void run() {
                        serialDataManager.disconnectFromSerialPort();
                        Platform.exit();
                    }
                }
        );
    }
    private void addCombinationKeyAcceleratorToClerScreen(Stage primaryStage) {
        primaryStage.getScene().getAccelerators().put(
                KeyCombination.keyCombination("CTRL+W"),
                new Runnable() {
                    public void run() {
                        root.getChildren().clear();
                        drawGridOnCanvas();
                    }
                }
        );
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
                if (touches == 2 && (secondTouch - firstTouch) > 0 && (secondTouch - firstTouch) <= 1000) {
                    serialDataManager.disconnectFromSerialPort();
                    Platform.exit();
                }
            }
        });
    }
    private void writeTextOnScene(String value) throws UnsupportedEncodingException {
        //System.out.println("DEBUG INFO - 3 --> call writeTextOnScene:" + System.currentTimeMillis());
        Cell variable = this.facade.onSerialDataInput(value.getBytes());
        if(variable != null){
            //System.out.println("DEBUG INFO - 4 --> call accept visitor:" + System.currentTimeMillis());
            variable.accept(this.visitor);
        }
    }

    private void readProperties() throws FileNotFoundException {
        String resourceFile = (!this.getParameters().getRaw().isEmpty())? this.getParameters().getRaw().get(0): DEFAULT_RESOURCE_FILE_NAME;
        ConfigurationHolder.createSingleInstanceByConfigUri(resourceFile);
    }


    public void runAndWaitMyRunnable() throws InterruptedException, ExecutionException {
        FutureTask future = new FutureTask(this.myRunnable, null);
        Platform.runLater(future);
        future.get();

    }
    class MyRunnable implements Runnable{

        private final BlockingQueue<String> serialBuffer;

        public MyRunnable(BlockingQueue<String> serialBuffer){
            this.serialBuffer = serialBuffer;
        }
        @Override
        public void run() {
            try {
                while (!this.serialBuffer.isEmpty()) {
                    writeTextOnScene(this.serialBuffer.take());
                }
                //TODO: ONLY FOR DEBUG - DELETE WHEN DONE
                System.out.println("BlockingQueue serialBuffer content after take:" + serialBuffer.size());
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    private Task<Void> serialTask = new Task<Void>() {
        @Override
        public Void call() throws Exception {

            serialDataManager.connectToSerialPort();
            /* TODO: EXPORT THIS BLOCK TO A METHOD, TO INVOKE ONLY FOR TEST REASONS
            BlockingQueue<String> list = new LinkedBlockingQueue<>();
            SerialDataEmulator sde = SerialDataEmulator.getNewInstanceBySerialBufferAndWaitingTime(list, 0);

            BlockingQueue<String> totalList = new LinkedBlockingQueue<>();

            sde.runScenario("serialInputs/variable-fragment.txt");

            System.out.println("Starting fill totalList");
            for (int i = 0; i < 20; i ++) {
                totalList.addAll(list);
            }
            System.out.println("End fill totalList: " + totalList.size());
            while(totalList.size() > 0) {
                serialBuffer.add(totalList.take());
            }
            */




            //parkinglist.addAll(list);

/* TODO: EXPERIMENTS

            for (int i = 0; i < 1000; i ++) {
                for(int k = 0; k < list.size(); k ++){
                    serialBuffer.add(list.poll());
                }
                list.addAll(parkinglist);
                Thread.sleep(100);
            }
*/
            /*
            //sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-rightAlignNumbers4-no-crc.txt");

            sde.runScenario("serialInputs/clean-row-before-cleaner-test.txt");
            sde.runScenario("serialInputs/clean-row-after-cleaner-test.txt");
            sde.runScenario("serialInputs/real-examples-prova3-fragment1-1-no-crc.txt");
            sde.runScenario("serialInputs/real-examples-prova3-fragment1-2-no-crc.txt");
            sde.runScenario("serialInputs/real-examples-prova3-fragment1-3-no-crc.txt");
            sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-no-crc.txt");
            sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-bars-no-crc.txt");
            sde.runScenario("serialInputs/barAndVariable-fragment.txt");
            sde.runScenario("serialInputs/real-examples-prova2-no-crc.txt");
            sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-rightAlign-no-crc.txt");
            sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-rightAlignVariables-no-crc.txt");
            Thread.sleep(1000);
            sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-rightAlignVariables1-no-crc.txt");
            Thread.sleep(1000);
            sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-rightAlignVariables2-no-crc.txt");
            Thread.sleep(1000);
            sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-rightAlignVariables3-no-crc.txt");
            Thread.sleep(1000);
            sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-rightAlignNumbers1-no-crc.txt");
            Thread.sleep(1000);
            sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-rightAlignNumbers2-no-crc.txt");
            Thread.sleep(1000);
            sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-rightAlignNumbers3-no-crc.txt");
            Thread.sleep(1000);
            sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-rightAlignNumbers4-no-crc.txt");
            sde.runScenario("serialInputs/test-bars-no-crc.txt");
            */
            while (true) {

            }

        }
    };


    public static void main(String[] args) {
        launch(args);
    }

    public Group getRoot() {
        return root;
    }

    public Multimap<Integer, javafx.scene.Node> getRows() {
        return rows;
    }
}
