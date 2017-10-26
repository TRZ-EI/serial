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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import trzpoc.comunication.SerialCommunicator;
import trzpoc.structure.Variable;
import trzpoc.structure.serial.SerialDataFacade;
import trzpoc.utils.ConfigurationHolder;
import trzpoc.utils.FontAndColorSelector;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class DrawingText extends Application {
    private Group root;
    //private Text text;
    private final String[] VALUES = {"^V07A310509f465\n", "^v0700000064d0b0\n", "^v07000000c8e076\n", "^v070000012c0156\n", "^v0700000190b73a\n", "^v07000001f4e93f\n", "^v07000002582a0f\n", "^v07000002bc56b9\n", "^v070000032005a0\n", "^v0700000384aaef\n", "^v07000003e81380\n", "^v070000044c4000\n", "^v07000004b08e8f\n", "^v0700000514a2d7\n", "^v0700000578c9fd\n", "^v07000005dc798f\n", "^v070000064044f6\n", "^v07000006a4f538\n", "^v07000007083e0a\n", "^v070000076c7f32\n", "^v07000007d07d79\n", "^v070000083486e4\n", "^v0700000898a8a3\n", "^v07000008fc5dbc\n", "^v07000009600ea5\n", "^v07000009c4bf6b\n\n", "^v0700000a28b406\n", "^v0700000a8cb053\n", "^v0700000af0f775\n", "^v0700000b54b54d\n", "^v0700000bb8e3e9\n", "^v0700000c1c64ab\n", "^v0700000c80b4a5\n"};
    private final int VALUES_INDEX = VALUES.length;
    private final String DEFAULT_RESOURCE_FILE_NAME = "application.properties";

    private BlockingQueue<String> serialBuffer;

    private int index;

    private SerialDataFacade facade;
    //private GraphicDesigner designer;

    @Override public void start(Stage stage) throws FileNotFoundException, UnsupportedEncodingException, InterruptedException {
        this.facade = SerialDataFacade.createNewInstance();
        this.serialBuffer = new LinkedBlockingQueue<>();
        this.readProperties();
        this.root = new Group();

        this.index = 0;

        Scene scene = new Scene(root, 800d, 480d, Color.WHITE);
        Canvas canvasForGrid = new Canvas(800d, 480d);
        GraphicDesigner.createNewInstance().drawGridForGraphicHelp(canvasForGrid);
        canvasForGrid.toFront();
        root.getChildren().add(canvasForGrid);

        //this.addMouseEventToChangeText(scene);
        stage.setScene(scene);
        stage.setTitle("TRZ bar for instruments");
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
    private void addMouseEventToChangeText(final Scene scene){

        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {
                if (me.getButton() == MouseButton.PRIMARY) {
                    if (index < VALUES_INDEX) {
                        String value = VALUES[index++];
                        try {
                            writeTextOnScene(value);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    private void writeTextOnScene(String value) throws UnsupportedEncodingException {
        // TODO: create a single interface for every type of data input and commands(Variable, Text, Bar, Clear)
        Variable variable = (Variable) this.facade.onSerialDataParser(value.getBytes());
        String id = String.valueOf(variable.getId());
        Text myText = (Text)root.getScene().lookup("#" + id);
        if (myText == null){
            myText = new Text();
            myText.setX(variable.getPixelScreenXPos());
            myText.setY(variable.getPixelScreenYPos());
            myText.setId(id);
            myText.setFill(Color.RED);
            myText.setFont(FontAndColorSelector.getNewInstance().getBigFont());
            //myText.textProperty().bind(task.messageProperty());
            root.getChildren().add(myText);
        }
        myText.setText(variable.printFormattedValue());
    }

    private void readProperties() throws FileNotFoundException {
        Properties properties = new Properties();
        String retValue = "PRODUCTION"; // default value
        String resourceFile = (!this.getParameters().getRaw().isEmpty())? this.getParameters().getRaw().get(0): DEFAULT_RESOURCE_FILE_NAME;
        ConfigurationHolder.createSingleInstanceByConfigUri(resourceFile);
    }
    private Task<Void> task = new Task<Void>() {
        @Override
        public Void call() throws Exception {

            while (true) {
                    if (!serialBuffer.isEmpty()){
                        String message = serialBuffer.poll();
                        System.out.println(message);
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

            SerialCommunicator sc = new SerialCommunicator();
            int counter = 0;
            boolean config = false;

            while (true) {
                String valueVariableTemplate = "^v07";
                String valueVariableTemplate1 = "^v08";
                Thread.sleep(50);
                String stringValue = Integer.toHexString(counter ++);
                String valueToTransmit = getPartiallyFormattedCommand(valueVariableTemplate, stringValue);
                valueToTransmit += sc.calculateCrCForString(valueToTransmit);
                valueToTransmit += '\n';
                serialBuffer.add(valueToTransmit);
                if (counter > 500){
                    if (!config) {
                        serialBuffer.add("^V08A310A09f465\n");
                        config = true;
                    }
                    valueToTransmit = this.getPartiallyFormattedCommand(valueVariableTemplate1, stringValue);
                    valueToTransmit += sc.calculateCrCForString(valueToTransmit);
                    valueToTransmit += '\n';
                    serialBuffer.add(valueToTransmit);
                }
                //writeTextOnScene(valueToTransmit);
            }
        }

        private String getPartiallyFormattedCommand(String valueVariableTemplate, String stringValue) {
            int zeros = 8 - stringValue.length();

            String valueToTransmit = valueVariableTemplate;
            for (int i = 0; i < zeros; i ++){
                valueToTransmit += "0";
            }
            valueToTransmit += stringValue;
            return valueToTransmit;
        }
    };


    public static void main(String[] args) {
        launch(args);
    }
}
