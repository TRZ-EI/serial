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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import trzpoc.comunication.SerialDataManager;
import trzpoc.structure.Cell;
import trzpoc.structure.Clear;
import trzpoc.structure.RowCleaner;
import trzpoc.structure.Variable;
import trzpoc.structure.serial.SerialDataFacade;
import trzpoc.utils.ConfigurationHolder;
import trzpoc.utils.SerialDataEmulator;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class DrawingText extends Application {

    private Group root;
    private BlockingQueue<String> serialBuffer;
    private SerialDataFacade facade;
    private SerialDataManager serialDataManager;

    private Multimap<Integer, Text> rows = ArrayListMultimap.create();

    private final String DEFAULT_RESOURCE_FILE_NAME = "application.properties";

    @Override public void start(Stage stage) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
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

    protected void drawGridOnCanvas() {
        Canvas canvasForGrid = new Canvas(800d, 480d);
        GraphicDesigner.createNewInstance().drawGridForGraphicHelp(canvasForGrid);
        canvasForGrid.toFront();
        this.addTouchEventToExit(canvasForGrid);
        root.getChildren().add(canvasForGrid);
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
        // TODO: create a single interface for every type of data input and commands(Configuration, Variable, Text, Number: Bar and Clear are different)
        Cell variable = this.facade.onSerialDataInput(value.getBytes());
        String v = null;
        if (variable instanceof trzpoc.structure.Text){
            v = variable.getValue();
        }else if (variable instanceof Variable){
            v = ((Variable)variable).printFormattedValue();
        }else if (variable instanceof Clear){
            root.getChildren().clear();
            this.rows.clear();
            this.drawGridOnCanvas();
            return;
        }else if (variable instanceof RowCleaner){
            Collection<Text> contents = this.rows.get(variable.getId());
            root.getChildren().removeAll(contents);
            return;
        }
        String id = String.valueOf(variable.getId());
        Text myText = (Text)root.getScene().lookup("#" + id);
        if (myText == null){
            myText = new Text();
            myText.setX(variable.getPixelScreenXPos());
            myText.setY(variable.getPixelScreenYPos());
            myText.setId(id);
            myText.setFill(variable.getColor());
            myText.setFont(variable.getFont());
            root.getChildren().add(myText);
            this.rows.put(variable.getyPos(), myText);
        }

        myText.setText(v);
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

            serialBuffer.add("^V07A310509f465\n");

            //serialDataManager = SerialDataManager.createNewInstanceBySerialBuffer(serialBuffer);
            //serialDataManager.connectToSerialPort();

            SerialDataEmulator sde = SerialDataEmulator.getNewInstanceBySerialBufferAndWaitingTime(serialBuffer, 1000);
            sde.runScenario("serialInputs/clean-row-before-cleaner-test.txt");
            sde.runScenario("serialInputs/clean-row-after-cleaner-test.txt");

            while (true) {

            }
        }
    };


    public static void main(String[] args) {
        launch(args);
    }
}
