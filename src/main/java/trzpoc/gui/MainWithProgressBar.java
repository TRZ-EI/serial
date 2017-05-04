package trzpoc.gui;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 14/02/17
 * Time: 11.06
 */

import eu.hansolo.medusa.*;
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
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import trzpoc.gui.dataProvider.Label;
import trzpoc.gui.dataProvider.SerialReceiverMock;
import trzpoc.utils.Chronometer;
import trzpoc.utils.RandomGeneratorInRange;

import java.util.Date;

// Font height: big = 36, small = 20

public class MainWithProgressBar extends Application {



    private Gauge pressorSensor1Gauge;
    private Gauge pressorSensor2Gauge;
    private Gauge pressorSensor3Gauge;
    protected Gauge timeElapsedGauge;

    protected Gauge pressure1Gauge, pressure2Gauge;

    private AnimationTimer timer;
    private long lastTimerCall;
    private Chronometer chronometer;


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Graphics Text for TRZ");

        Group root = new Group();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        //this.setVisualBounds(primaryStage);
        this.addCombinationKeyAcceleratorToExit(primaryStage);

        //ProgressBar progressBar = new ProgressBar(0);
        pressorSensor1Gauge = this.createTestExecutionGauge();
        pressorSensor2Gauge = this.createTestExecutionGauge();
        pressorSensor3Gauge = this.createTestExecutionGauge();


        timeElapsedGauge = this.createTimeElapsedGauge();
        //timeElapsedGauge.setValue(Double.valueOf(172.9d));
        pressure1Gauge = this.createPressureGauge(1);
        pressure2Gauge = this.createPressureGauge(2);


        chronometer = Chronometer.getInstance();
        chronometer.start();
        timer = new AnimationTimer() {
            
            // 20.532.358.351.793
            @Override public void handle(long now) {

                timeElapsedGauge.setValue(chronometer.getActualTimeInSeconds());

                RandomGeneratorInRange pressureSensor1 = RandomGeneratorInRange.getInstanceByRange(-4d, 1d);
                RandomGeneratorInRange pressureSensor2 = RandomGeneratorInRange.getInstanceByRange(-4d, 1d);
                RandomGeneratorInRange pressureSensor3 = RandomGeneratorInRange.getInstanceByRange(-4d, 1d);
                RandomGeneratorInRange pressureSensor4 = RandomGeneratorInRange.getInstanceByRange(0d, 10d);
                RandomGeneratorInRange pressureSensor5 = RandomGeneratorInRange.getInstanceByRange(0d, 10d);

                if (now > lastTimerCall + 5000_000_000l) {
                    pressure1Gauge.setValue(pressureSensor4.getRandomValue());
                    pressure2Gauge.setValue(pressureSensor5.getRandomValue());
                    pressorSensor1Gauge.setValue(pressureSensor1.getRandomValue());
                    pressorSensor2Gauge.setValue(pressureSensor2.getRandomValue());
                    pressorSensor3Gauge.setValue(pressureSensor3.getRandomValue());



                    lastTimerCall = now;

/*
                    value = (value < 1d)? value: -4d;
                    pressorSensor1Gauge.setValue(value += 0.1);

*/
                }
            }
        };
        timer.start();



        /*
        pressorSensor2Gauge.setBarColor(Color.rgb(77,208,225));
        pressorSensor2Gauge.setBarBackgroundColor(Color.rgb(39,44,50));
        pressorSensor2Gauge.setAnimated(true);

        pressorSensor3Gauge.setBarColor(Color.rgb(77,208,225));
        pressorSensor3Gauge.setBarBackgroundColor(Color.rgb(39,44,50));
        pressorSensor3Gauge.setAnimated(true);
        */
        HBox controls = new HBox();
        controls.setSpacing(10);
        controls.setAlignment(Pos.CENTER);
        controls.getChildren().add(pressorSensor1Gauge);
        controls.getChildren().add(pressorSensor2Gauge);
        controls.getChildren().add(pressorSensor3Gauge);

        HBox timeControls = new HBox();
        timeControls.setSpacing(10);
        timeControls.setAlignment(Pos.CENTER);
        timeControls.getChildren().addAll(pressure1Gauge, pressure2Gauge, timeElapsedGauge);


        Canvas canvas = new Canvas(800, 300);

        VBox myGroup = new VBox();
        myGroup.setSpacing(5);
        myGroup.setAlignment(Pos.CENTER);
        myGroup.getChildren().addAll(timeControls /*, canvas*/, controls);



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
                    gc.fillText(cb.printFormattedValue(), cb.getX(), cb.getY());
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
    private Gauge createTimeElapsedGauge(){
        return GaugeBuilder.create().prefSize(150, 75)
                .skinType(Gauge.SkinType.LCD)
                .animated(true)
                .title("Prova N 1").maxMeasuredValueVisible(false)
                .minMeasuredValueVisible(false)
                .oldValueVisible(false)
                .unit("sec").decimals(1)
                .lcdDesign(LcdDesign.YOCTOPUCE)
                .thresholdVisible(false)
                .averageVisible(false)
                .threshold(25)
                .build();
    }
    private Gauge createPressureGauge(int index){
        return GaugeBuilder.create().prefSize(180, 180)
                .skinType(Gauge.SkinType.DASHBOARD)
                .animated(true)
                .title("Pressione P" + index)
                .unit("Bar")
                .maxValue(10).decimals(3)
                .barColor(Color.CRIMSON)
                .valueColor(Color.BLUE)
                .titleColor(Color.BLUE)
                .unitColor(Color.BLUE)
                .thresholdVisible(false)
                .threshold(35)
                .shadowsEnabled(true)
                .gradientBarEnabled(true)
                .gradientBarStops(new Stop(0.00, Color.BLUE),
                        new Stop(0.25, Color.CYAN),
                        new Stop(0.50, Color.LIME),
                        new Stop(0.75, Color.YELLOW),
                        new Stop(1.00, Color.RED))
                .build();

    }
    private Gauge createTestExecutionGauge(){
        return GaugeBuilder.create()
                .minValue(-4)
                .maxValue(1)
                .tickLabelDecimals(1)
                .autoScale(true)
                .animated(true)
                .startAngle(0)
                .angleRange(300)
                .threshold(0.5)
                .thresholdVisible(true)
                .majorTickMarkType(TickMarkType.TRAPEZOID)
                .mediumTickMarkType(TickMarkType.TRAPEZOID)
                .tickLabelColor(Color.BLUE)
                .tickMarkColor(Color.BLUE)
                .titleColor(Color.BLUE)
                .subTitleColor(Color.BLUE)
                .unitColor(Color.BLUE)
                .zeroColor(Color.LIGHTSKYBLUE)
                .lcdVisible(true)
                .lcdDesign(LcdDesign.GRAY_PURPLE)

                .title("DPI")

                .unit("mBar")
                .interactive(false)
                .needleSize(Gauge.NeedleSize.THIN)
                .tickLabelLocation(TickLabelLocation.OUTSIDE)
                .scaleDirection(Gauge.ScaleDirection.CLOCKWISE)
                .sectionsVisible(true)
                .sections(new Section(-4, -2, Color.RED),
                        new Section(-2, 0, Color.YELLOW),
                        new Section(0, 1, Color.LIME))
                .areasVisible(true)
                .highlightAreas(true)
                .areas(new Section(-4, -2, Color.rgb(200, 0, 0, 0.1), Color.rgb(255, 0, 0)))
                .markersVisible(true)
                //.markersInterActive(true)
                .markers(new Marker(0.5, Color.LIME))
                .needleColor(Color.CYAN)
                .build();

    }


    public static void main(String[] args) {
        launch(args);
    }
}