package guiexamples;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 14/03/17
 * Time: 15.18
 */

import eu.hansolo.medusa.*;
import eu.hansolo.medusa.Clock.ClockSkinType;
import eu.hansolo.medusa.Gauge.SkinType;
import eu.hansolo.medusa.events.UpdateEvent;
import eu.hansolo.medusa.events.UpdateEvent.EventType;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.Instant;
import java.util.Locale;
import java.util.Random;



/**
 * User: hansolo
 * Date: 04.01.16
 * Time: 06:31
 */
public class MedusaTest extends Application {
    private static final Random          RND       = new Random();
    private static       int             noOfNodes = 0;
    private FGauge fgauge;
    private Gauge gauge;
    private Clock clock;
    private              long            lastTimerCall;
    private              AnimationTimer  timer;
    private              DoubleProperty  value;
    private              long            epochSeconds;
    private              BooleanProperty toggle;


    @Override public void init() {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("da", "DK"));
        numberFormat.setRoundingMode(RoundingMode.HALF_DOWN);
        numberFormat.setMinimumIntegerDigits(3);
        numberFormat.setMaximumIntegerDigits(3);
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(0);

        value  = new SimpleDoubleProperty(0);
        toggle = new SimpleBooleanProperty(false);

        fgauge = FGaugeBuilder.create()
                .gaugeDesign(GaugeDesign.NONE)
                .build();

        gauge = GaugeBuilder.create()
                .skinType(SkinType.AMP)
                //.prefSize(400, 400)
                .knobPosition(Pos.BOTTOM_LEFT)
                .tickLabelLocation(TickLabelLocation.OUTSIDE)
                .decimals(0)
                .minValue(-20)
                .maxValue(100)
                .animated(true)
                //.checkThreshold(true)
                //.onThresholdExceeded(e -> System.out.println("threshold exceeded"))
                .lcdVisible(true)
                .lcdFont(LcdFont.LCD)
                //.locale(Locale.GERMANY)
                //.numberFormat(numberFormat)
                .title("Very Large Title")
                .unit("\u00B0C")
                .subTitle("Only")
                //.interactive(true)
                //.onButtonPressed(o -> System.out.println("Button pressed"))
                .sections(new Section(-20,  0, Color.rgb(  0,   0, 255), Color.rgb(  0,   0, 255)),
                        new Section(  0, 25, Color.rgb(255, 255,   0), Color.rgb(255, 255,   0)),
                        new Section( 75,100, Color.rgb(255,   0,   0), Color.rgb(255, 255,   0)))
                .sectionsVisible(true)
                .highlightSections(true)
                .autoScale(true)
                .averagingEnabled(true)
                .averagingPeriod(10)
                .averageVisible(true)
                .markers(
                        new Marker( 0, "M1", Color.rgb(  0,   0, 255, 0.95), Marker.MarkerType.DOT),
                        new Marker(25, "M2", Color.rgb(255, 255,   0, 0.95), Marker.MarkerType.STANDARD),
                        new Marker(50, "M3", Color.rgb(255, 255,   0, 0.95), Marker.MarkerType.TRIANGLE)
                )
                .markersVisible(true)
                .ledVisible(true)
                //.ledType(LedType.FLAT)
                .thresholdVisible(true)
                .threshold(60)
                .checkThreshold(true)
                .onThresholdExceeded(e -> gauge.setLedBlinking(true))
                .onThresholdUnderrun(e -> gauge.setLedBlinking(false))
                .build();

        //gauge.setAlert(true);

        // Calling bind() directly sets a value to gauge
        gauge.valueProperty().bind(value);

        gauge.getSections().forEach(section -> section.setOnSectionUpdate(sectionEvent -> gauge.fireUpdateEvent(new UpdateEvent(MedusaTest.this, EventType.REDRAW))));

        //gauge.valueVisibleProperty().bind(toggle);

        epochSeconds = Instant.now().getEpochSecond();

        clock = ClockBuilder.create()
                .skinType(ClockSkinType.LCD)
                .locale(Locale.GERMANY)
                .shadowsEnabled(false)
                //.discreteSeconds(false)
                //.discreteMinutes(false)
                .running(true)
                //.backgroundPaint(Color.web("#1f1e23"))
                //.hourColor(Color.web("#dad9db"))
                .lcdFont(LcdFont.DIGITAL)
                .secondsVisible(true)
                //.minuteColor(Color.web("#dad9db"))
                //.secondColor(Color.web("#d1222b"))
                //.hourTickMarkColor(Color.web("#9f9fa1"))
                //.minuteTickMarkColor(Color.web("#9f9fa1"))
                .build();

        lastTimerCall = System.nanoTime();
        timer = new AnimationTimer() {

            private int counter = 0;
            private boolean changed = false;

            @Override public void handle(long now) {
                if (now > lastTimerCall + 3_000_000_000l) {
                    double v = RND.nextDouble() * gauge.getRange() * 1.1 + gauge.getMinValue();
                    value.set(v);
                    //System.out.println(v);
                    //gauge.setValue(v);

                    //System.out.println("MovingAverage over " + gauge.getAveragingWindow().size() + " values: " + gauge.getAverage() + "  last value = " + v);

                    //toggle.set(!toggle.get());

                    //System.out.println(gauge.isValueVisible());

                    //gauge.setValue(v);

                    //epochSeconds+=20;
                    //clock.setTime(epochSeconds);

                    if ( counter++ >= 1 ) {
                        if ( !changed ) {
                            changed = true;
                            clock.setSecondsVisible(false);
                            System.out.println("*** CHANGED");
                        }
                    }

                    lastTimerCall = now;
                }
            }
        };
    }

    @Override public void start(Stage stage) {
        StackPane pane = new StackPane(gauge);
        pane.setPadding(new Insets(20));
        pane.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        LinearGradient gradient = new LinearGradient(0, 0, 0, pane.getLayoutBounds().getHeight(),
                false, CycleMethod.NO_CYCLE,
                new Stop(0.0, Color.rgb(38, 38, 38)),
                new Stop(1.0, Color.rgb(15, 15, 15)));
        //pane.setBackground(new Background(new BackgroundFill(gradient, CornerRadii.EMPTY, Insets.EMPTY)));
        //pane.setBackground(new Background(new BackgroundFill(Color.rgb(39,44,50), CornerRadii.EMPTY, Insets.EMPTY)));
        //pane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        //pane.setBackground(new Background(new BackgroundFill(Gauge.DARK_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(pane);
        scene.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);

        stage.setTitle("Medusa");
        stage.setScene(scene);
        stage.show();

        //gauge.setValue(50);

        // Calculate number of nodes
        calcNoOfNodes(pane);
        System.out.println(noOfNodes + " Nodes in SceneGraph");

        timer.start();

        //gauge.getSections().get(0).setStart(10);
        //gauge.getSections().get(0).setStop(90);
    }

    @Override public void stop() {
        System.exit(0);
    }



    // ******************** Misc **********************************************
    private static void calcNoOfNodes(Node node) {
        if (node instanceof Parent) {
            if (((Parent) node).getChildrenUnmodifiable().size() != 0) {
                ObservableList<Node> tempChildren = ((Parent) node).getChildrenUnmodifiable();
                noOfNodes += tempChildren.size();
                for (Node n : tempChildren) { calcNoOfNodes(n); }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}