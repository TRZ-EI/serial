package trzpoc.gui;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 14/02/17
 * Time: 11.06
 */

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.LcdDesign;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import trzpoc.gui.dataProvider.Label;
import trzpoc.gui.dataProvider.SerialReceiverMock;

import java.util.Date;
import java.util.Random;

// Font height: big = 36, small = 20

public class MainWithProgressBar extends Application {



    protected Gauge myGauge;
    protected Gauge timeElapsedGauge;

    private AnimationTimer timer;
    private Random RND = new Random();
    private double value = 0;
    private long lastTimerCall;



    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Graphics Text for TRZ");

        Group root = new Group();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        //this.setVisualBounds(primaryStage);
        this.addCombinationKeyAcceleratorToExit(primaryStage);

        //ProgressBar progressBar = new ProgressBar(0);
        GaugeBuilder builder = GaugeBuilder.create().prefSize(180, 180).skinType(Gauge.SkinType.DIGITAL);

        myGauge = builder.decimals(0).maxValue(10).unit("PSI").build();
        Gauge myGauge1 = builder.decimals(0).maxValue(10).unit("PSI").build();
        Gauge myGauge2 = builder.decimals(0).maxValue(10).unit("PSI").build();
        
        myGauge.setBarColor(Color.rgb(77,208,225));
        myGauge.setBarBackgroundColor(Color.rgb(39,44,50));
        myGauge.setAnimated(true);

        timer = new AnimationTimer() {
            @Override public void handle(long now) {
                if (now > lastTimerCall + 3_000_000_000l) {
                    value = (value <= 10)? value: 0;
                    myGauge.setValue(value ++);
                    lastTimerCall = now;
                }
            }
        };
        timer.start();




        myGauge1.setBarColor(Color.rgb(77,208,225));
        myGauge1.setBarBackgroundColor(Color.rgb(39,44,50));
        myGauge1.setAnimated(true);

        myGauge2.setBarColor(Color.rgb(77,208,225));
        myGauge2.setBarBackgroundColor(Color.rgb(39,44,50));
        myGauge2.setAnimated(true);

        HBox controls = new HBox();
        controls.setSpacing(10);
        controls.setAlignment(Pos.CENTER);
        controls.getChildren().add(myGauge);
        controls.getChildren().add(myGauge1);
        controls.getChildren().add(myGauge2);

        Canvas canvas = new Canvas(800, 300);

        VBox myGroup = new VBox();
        myGroup.setSpacing(5);
        myGroup.setAlignment(Pos.CENTER);
        myGroup.getChildren().addAll(canvas, controls);



        // TO DO: CREATE A PRIVATE METHOD
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(myGroup);  // was canvas
        scrollPane.setPrefSize(800, 480);
        scrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        scrollPane.setFitToHeight(false);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

                                   


        //root.getChildren().add(canvas);
        root.getChildren().add(scrollPane);
        this.addTouchEventToStart(canvas);
        this.addMouseEventToStart(canvas);
        this.calculateRows(canvas);

        /*
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
        */
        primaryStage.show();
    }

    private int calculateRows(Canvas canvas) {
        Text text = new Text("PROVA N 1                  Prova.         172.9 sec");
        //Font font = Font.font("Arial", FontWeight.BOLD, 72);
        Font font = Font.font("Arial", FontWeight.BOLD, 36);
        text.setFont(font);

        Bounds b = text.getLayoutBounds();

        Double h = b.getHeight();
        double rows = canvas.getHeight() / h;
        System.out.println("canvas height: " + Double.valueOf(canvas.getHeight()).intValue());
        System.out.println("text height: " + Double.valueOf(b.getHeight()).intValue());
        System.out.println("text width: " + Double.valueOf(b.getWidth()).intValue());
        System.out.println("rows: " + Double.valueOf(canvas.getHeight()).intValue() / Double.valueOf(b.getHeight()).intValue());
        return Double.valueOf(canvas.getHeight()).intValue() / Double.valueOf(b.getHeight()).intValue();

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
              if (event.getEventSetId() == 1) {
                    SerialReceiverMock sm = SerialReceiverMock.getNewInstance();

                    while(sm.hasMoreMessages()){
                        Label cb = (Label) sm.getReceivedString();
                        GraphicsContext gc = canvas.getGraphicsContext2D();
                        gc.setFill(cb.getColor());
                        gc.setFont(cb.getFont());
                        gc.fillText(cb.getValue(), cb.getX(), cb.getY());

                        //canvas.
                    }
                }
                event.consume();
            }

        });
    }

    private void addMouseEventToStart(final Canvas canvas){
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                System.out.println("HI");
                int y = 0;
                Font font = Font.font("Arial", FontWeight.NORMAL, 20);
                int fontSize = Double.valueOf(font.getSize()).intValue();
                int height = Double.valueOf(canvas.getHeight()).intValue();
                int rows = height / fontSize;
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.setFont(font);
                gc.setFill(Color.RED);
                for (int i = 0; i < rows; i++){
                    gc.fillText("PROVA N 1                  Prova.                       172.9 sec", 0, y+= fontSize);
                }



                /*
                SerialReceiverMock sm = SerialReceiverMock.getNewInstance();
                while(sm.hasMoreMessages()){
                    Label cb = (Label) sm.getReceivedString();
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    gc.setFill(cb.getColor());
                    gc.setFont(cb.getFont());
                    gc.fillText(cb.getValue(), cb.getX(), cb.getY());
                }
                */
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

    private void setVisualBounds(Stage primaryStage) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        //set Stage boundaries to visible bounds of the main screen
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());

        //stage.show();
    }
    private Gauge createTimeElapsedTime(){
        return GaugeBuilder.create()
                .skinType(Gauge.SkinType.LCD)
                .animated(true)
                .title("Tempo")
                .subTitle("Prova N 1")
                .unit("sec")
                .lcdDesign(LcdDesign.BLUE_LIGHTBLUE2)
                .thresholdVisible(true)
                .threshold(25)
                .build();
    }


    public static void main(String[] args) {
        launch(args);
    }
}