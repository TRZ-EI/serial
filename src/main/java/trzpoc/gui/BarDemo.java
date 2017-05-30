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

import eu.hansolo.medusa.*;
import eu.hansolo.medusa.Gauge.SkinType;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import trzpoc.gui.hansolo.skins.TRZLinearSkin;

import java.util.Random;


public class BarDemo extends Application {
    private static final Random RND            = new Random();
    private static final double MIN_CELL_SIZE  = 80;
    private static final double PREF_CELL_SIZE = 120;
    private static final double MAX_CELL_SIZE  = 150;
    private static       int    noOfNodes      = 0;
    private Gauge          gauge21;
    private AnimationTimer timer;
    private long lastTimerCall;

    @Override public void init() {
        // TODO: isolate this



        gauge21 = GaugeBuilder.create()
                              //.skinType(SkinType.LINEAR)
                              //.title("Linear")
// Related to Tick Labels
                .tickLabelDecimals(0)
                .minValue(0)
                .maxValue(1)
                .areasVisible(false)
                // Number of decimals for tick labels
                .onlyFirstAndLastTickLabelVisible(false)                                         // Should only the first and last tick label be visible
                .tickLabelSectionsVisible(false)                                                 // Should sections for tick labels be visible
                // Related to Tick Marks
                .tickMarkSectionsVisible(false)                                                  // Should sections for tick marks be visible
                // Related to Major Tick Marks
                .majorTickMarksVisible(false)                                                     // Should major tick marks be visible
                //.majorTickMarkType(TickMarkType.LINE)                                            // Tick mark type for major tick marks (LINE, DOT, TRIANGLE, TICK_LABEL)
                //.majorTickMarkColor(Color.BLACK)                                                 // Color for major tick marks (overriden by tick mark sections)
                // Related to Medium Tick Marks
                .mediumTickMarksVisible(false)                                                    // Should medium tick marks be visible
                //.mediumTickMarkType(TickMarkType.LINE)                                           // Tick mark type for medium tick marks (LINE, DOT, TRIANGLE)
                //.mediumTickMarkColor(Color.BLACK)                                                // Color for medium tick marks (overriden by tick mark sections)
                // Related to Minor Tick Marks
                .minorTickMarksVisible(false)                                                     // Should minor tick marks be visible
                //.minorTickMarkType(TickMarkType.LINE)                                            // Tick mark type for minor tick marks (LINE, DOT, TRIANGLE)
                //.minorTickMarkColor(Color.BLACK)




                .tickLabelsVisible(false)
                              .tickLabelSectionsVisible(false)
                              .tickMarkSectionsVisible(false)
                              .orientation(Orientation.HORIZONTAL)
                              .sectionsVisible(false)
                              .valueVisible(false)
                              .foregroundBaseColor(Color.BLUE)
                              .barColor(Color.GREEN)
                              .barEffectEnabled(true)
                              .barBorderColor(Color.CHOCOLATE)

                /*
                              .sections(new Section(0, 20, Color.BLUE),
                                        new Section(80, 100, Color.RED),
                                        new Section(50, 80, Color.ORANGE)
                                      )
*/
                              .build();
        gauge21.setSkin(new TRZLinearSkin(gauge21));

        lastTimerCall = System.nanoTime();
        timer = new AnimationTimer() {
            @Override public void handle(long now) {
                if (now > lastTimerCall + 3_000_000_000l) {
                    double value = RND.nextDouble() * gauge21.getRange() + gauge21.getMinValue();
                    gauge21.setValue(value);
                    if (value >= 60d){
                        gauge21.setBarColor(Color.BLUE);
                    }else{
                        gauge21.setBarColor(Color.GREEN);
                    }
                    lastTimerCall = now;
                }
            }
        };




    }

    @Override public void start(Stage stage) {
        stage.setWidth(800d);
        stage.setHeight(480d);
        Group root = new Group();
        gauge21.setPrefSize(700d, 100d);
        gauge21.setLayoutX(10);
        gauge21.setLayoutY(300);
        root.getChildren().add(gauge21);
        
        //GridPane pane = new GridPane();
        //pane.add(gauge21, 6, 1);
        //pane.setHgap(10);
        //pane.setVgap(10);
        //pane.setBackground(new Background(new BackgroundFill(Color.rgb(90, 90, 90), CornerRadii.EMPTY, Insets.EMPTY)));
        stage.setScene(new Scene(root));
        stage.setTitle("TRZ bar for instruments");
        stage.show();
        timer.start();


    }

    @Override public void stop() {
        System.exit(0);
    }



    public static void main(String[] args) {
        launch(args);
    }
}
