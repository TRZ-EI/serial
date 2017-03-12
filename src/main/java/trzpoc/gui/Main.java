package trzpoc.gui;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 14/02/17
 * Time: 11.06
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.TouchEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Date;


public class Main extends Application {


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Graphics Text");

        Group root = new Group();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        //this.setVisualBounds(primaryStage);
        this.addCombinationKeyAcceleratorToExit(primaryStage);



        Canvas canvas = new Canvas(800, 480);


        // TO DO: CREATE A PRIVATE METHOD
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(canvas);
        scrollPane.setPrefSize(800, 480);
        scrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        scrollPane.setFitToHeight(false);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

                                   


        //root.getChildren().add(canvas);
        root.getChildren().add(scrollPane);
        this.addTouchEventToExit(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.RED);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(4);
        Font theFont = Font.font("Arial", FontWeight.BOLD, 72);
        gc.setFont(theFont);
        gc.fillText("01234567890123456789", 0, 60);
        gc.fillText("wwwwwwwwwwwwwwwwwwww", 0, 120);
        gc.fillText("WWWWWWWWWWWWWWWWWWWW", 0, 180);
        //gc.strokeText("Hello, World!", 60, 60);

        Font smallFont = Font.font("Arial", FontWeight.BOLD, 36);
        gc.setFill(Color.BLUE);
        gc.setFont(smallFont);
        gc.fillText("Testo piccolo", 0, 260);

        primaryStage.show();
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

    private void setVisualBounds(Stage primaryStage) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        //set Stage boundaries to visible bounds of the main screen
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());

        //stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}