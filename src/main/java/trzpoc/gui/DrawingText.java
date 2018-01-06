/*
 * Copyright (c) 2016 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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


    @Override public void start(Stage stage) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
        this.visitor = new StructureVisitor(this);
        this.readProperties();
        this.facade = SerialDataFacade.createNewInstance();
        this.serialBuffer = new LinkedBlockingQueue<>();
        this.root = new Group();
        Scene scene = new Scene(root, 800d, 480d, Color.WHITE);
        this.drawGridOnCanvas();

        //this.addMouseEventToChangeText(scene);
        stage.setScene(scene);
        stage.setTitle("TRZ bar for instruments");
        this.addCombinationKeyAcceleratorToExit(stage);
        this.addCombinationKeyAcceleratorToClerScreen(stage);
        stage.show();

        // Task to write on screen
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

        // Task to read from serial (future)
        Thread serialThread = new Thread(serialTask);
        serialThread.setDaemon(true);
        serialThread.start();

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
        Cell variable = this.facade.onSerialDataInput(value.getBytes());
        if(variable != null){
            variable.accept(this.visitor);
        }
    }

    private void readProperties() throws FileNotFoundException {
        String resourceFile = (!this.getParameters().getRaw().isEmpty())? this.getParameters().getRaw().get(0): DEFAULT_RESOURCE_FILE_NAME;
        ConfigurationHolder.createSingleInstanceByConfigUri(resourceFile);
    }

    private Task<Void> task = new Task<Void>() {
        @Override
        public Void call() throws Exception {

            while (true) {
                    if (!serialBuffer.isEmpty()){
                        String message = serialBuffer.poll();
                        Platform.runLater(new Runnable() {
                            @Override public void run() {
                                try {
                                    writeTextOnScene(message);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
            }
        }
    };

    private Task<Void> serialTask = new Task<Void>() {
        @Override
        public Void call() throws Exception {

            serialDataManager = SerialDataManager.createNewInstanceBySerialBuffer(serialBuffer);
            serialDataManager.connectToSerialPort();



            /*
            SerialDataEmulator sde = SerialDataEmulator.getNewInstanceBySerialBufferAndWaitingTime(serialBuffer, 200);
//            sde.runScenario("serialInputs/clean-row-before-cleaner-test.txt");
//            sde.runScenario("serialInputs/clean-row-after-cleaner-test.txt");
//            sde.runScenario("serialInputs/real-examples-prova3-fragment1-1.txt");
//            sde.runScenario("serialInputs/real-examples-prova3-fragment1-2.txt");
//            sde.runScenario("serialInputs/real-examples-prova3-fragment1-3.txt");
//            sde.runScenario("serialInputs/real-examples-prova3-fragment1-4.txt");
//            sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-bars-no-crc.txt");
//            sde.runScenario("serialInputs/barAndVariable-fragment.txt");
//            sde.runScenario("serialInputs/real-examples-prova2.txt");
            sde.runScenario("serialInputs/real-examples-prova3-fragment1-4-rightAlign-no-crc.txt");
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
