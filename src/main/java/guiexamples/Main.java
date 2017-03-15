package guiexamples;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {
     private GridPane pane;
     private Gauge steps;
     private Gauge    distance;  
     private Gauge    activeCalories;  
     private Gauge    foodCalories;  
     private Gauge    weight;  
     private Gauge    bodyFat;  
  
      @Override public void init() {  
         GaugeBuilder builder = GaugeBuilder.create().skinType(Gauge.SkinType.SLIM);
         steps          = builder.decimals(0).maxValue(10000).unit("STEPS").build();  
         distance       = builder.decimals(2).maxValue(10).unit("KM").build();  
         activeCalories = builder.decimals(0).maxValue(2200).unit("KCAL").build();  
         foodCalories   = builder.decimals(0).maxValue(2200).unit("KCAL").build();  
         weight         = builder.decimals(1).maxValue(85).unit("KG").build();  
         bodyFat        = builder.decimals(1).maxValue(20).unit("%").build();  
  
          VBox stepsBox        = getTopicBox("STEPS", Color.rgb(77,208,225), steps);
         VBox distanceBox     = getTopicBox("DISTANCE", Color.rgb(255,183,77), distance);  
         VBox foodCaloriesBox = getTopicBox("FOOD", Color.rgb(129,199,132), foodCalories);  
         VBox weightBox       = getTopicBox("WEIGHT", Color.rgb(149,117,205), weight);  
         VBox bodyFatBox      = getTopicBox("BODY FAT", Color.rgb(186,104,200), bodyFat);  
         VBox actvCaloriesBox = getTopicBox("ACTIVE CALORIES", Color.rgb(229,115,115),  
                                              activeCalories);  
  
           pane = new GridPane();  
         pane.setPadding(new Insets(20));
         pane.setHgap(10);  
         pane.setVgap(15);  
         pane.setBackground(new Background(new BackgroundFill(Color.rgb(39,44,50), CornerRadii.EMPTY, Insets.EMPTY)));
         pane.add(stepsBox, 0, 0);  
         pane.add(distanceBox, 1, 0);  
         pane.add(actvCaloriesBox, 0, 2);  
         pane.add(foodCaloriesBox, 1, 2);  
         pane.add(weightBox, 0, 4);  
         pane.add(bodyFatBox, 1, 4);       
}  
  
 @Override public void start(Stage stage) {
         Scene scene = new Scene(pane);
  
         steps.setValue(5201);  
         distance.setValue(3.12);  
         activeCalories.setValue(347);  
         foodCalories.setValue(1500);  
         weight.setValue(78.7);  
         bodyFat.setValue(14.2);  
  
         stage.setTitle("Medusa Dashboard");  
         stage.setScene(scene);  
         stage.show();  
     }  
  
@Override public void stop() { System.exit(0); }  
  
private VBox getTopicBox(final String TEXT, final Color COLOR, final Gauge GAUGE) {  
         Rectangle bar = new Rectangle(200, 3);
         bar.setArcWidth(6);  
         bar.setArcHeight(6);  
         bar.setFill(COLOR);  
  
         Label label = new Label(TEXT);
         label.setTextFill(COLOR);  
         label.setAlignment(Pos.CENTER);
         label.setPadding(new Insets(0, 0, 10, 0));  
  
         GAUGE.setBarColor(COLOR);  
         GAUGE.setBarBackgroundColor(Color.rgb(39,44,50));
         GAUGE.setAnimated(true);  
  
         VBox vBox = new VBox(bar, label, GAUGE);  
         vBox.setSpacing(3);  
         vBox.setAlignment(Pos.CENTER);  
         return vBox;  
     }  
  
      public static void main(String[] args) { launch(args); }   
}  