package trzpoc.gui;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 05/05/17
 * Time: 16.03
 */

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.Section;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import trzpoc.gui.dataProvider.Label;
import trzpoc.gui.dataProvider.SerialReceiverMock;
import trzpoc.gui.hansolo.skins.TRZLinearSkin;
import trzpoc.structure.*;
import trzpoc.structure.serial.SerialDataFacade;
import trzpoc.utils.FontAndColorSelector;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

public class MainForSerialData extends Application {

    protected Group root;
    protected Stage primaryStage;
    private String debug;

    private String readDebugValue(){
        final String resourceFileName = "application.properties";
        String retValue = "PRODUCTION"; // default value
        try {
            Properties properties = new Properties();
            InputStream s = this.getClass().getClassLoader().getResourceAsStream(resourceFileName);
            properties.load(s);
            s.close();
            retValue = properties.getProperty("DEBUG");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retValue;
    }


    @Override
    public void start(Stage primaryStage) {
        this.debug = this.readDebugValue();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("JavaFX Graphics Text for TRZ");

        this.root = new Group();
        Scene scene = new Scene(root);
        this.primaryStage.setScene(scene);
        this.addCombinationKeyAcceleratorToExit(primaryStage);
        Canvas canvas = new Canvas(800, 480);


        // TODO: REMOVED SCROLLBAR TO MANAGE BARS
        /*
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(canvas);
        scrollPane.setPrefSize(800, 480);
        scrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        scrollPane.setFitToHeight(false);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        */
        root.getChildren().add(canvas);
        this.addTouchEventToStart(canvas);
        this.addMouseEventToStart(canvas);
        this.primaryStage.show();
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
                SerialDataFacade sd = SerialDataFacade.createNewInstance();
                try {
                    String realFileName = this.getClass().getClassLoader().getResource("inputExamples.csv").getFile();
                    DataDisplayManager dm = sd.fillMatrixWithData(realFileName);

                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    int width = 0;
                    for (int row = 0; row < dm.getNumberOfRows(); row ++){
                        CellsRow cellsRow = dm.getOrCreateARow(row);
                        // DRAW ROWS
                        this.drawHorizontalRows(cellsRow, gc);
                        boolean isAlreaadyPlotted = false;
                        for(int cellIndex = 0; cellIndex < cellsRow.getCellsCount(); cellIndex ++){
                            Cell c = cellsRow.getCellByColumnIndex(cellIndex);
                            // DRAW VERTICAL DIVS FOR ROW
                            if (!isAlreaadyPlotted){
                                this.drawVerticalDivsForRow(cellsRow, c, gc);
                                isAlreaadyPlotted = true;
                            }
                            gc.setFont(c.getFont());
                            gc.setFill(c.getColor());
                            String textToFill = null;
                            if (c instanceof Variable){
                                textToFill = ((Variable)c).printFormattedValue();
                            }else if (c instanceof Text){
                                textToFill = c.getValue();
                            }else if (c instanceof Bar){
                                this.configureBar(c, cellsRow.getPixelScreenYPos());
                            }
                            if (textToFill != null && textToFill.length() > 0) {
                                FontAndColorSelector fcs = FontAndColorSelector.getNewInstance();
                                width = fcs.getWidthForFont(c.getFont(), "W");
                                gc.fillText(textToFill, c.getxPos() * width, cellsRow.getPixelScreenYPos());
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                me.consume();
            }

            private void configureBar(Cell c, int pixelScreenYPos) {
                // TODO: EXPERIMENTS WITH BARS
                double prefHeight = 200d;
                double prefWidth = 700d;

                Bar cellBar = (Bar)c;
                Gauge bar = this.createHorizontalBar(cellBar.getMinValue(), cellBar.getMaxValue());
                bar.setPrefSize(prefWidth, prefHeight);
                bar.setLayoutX(30);
                bar.setLayoutY(pixelScreenYPos - prefHeight / 2);
                root.getChildren().add(bar);
            }
             private Gauge createHorizontalBar(long minValue, long maxValue) {
                long delta = maxValue - minValue;
                Gauge gauge = GaugeBuilder.create()
                        .minValue(minValue)
                        .maxValue(maxValue)
                        .skinType(Gauge.SkinType.LINEAR)
                        .orientation(Orientation.HORIZONTAL)
                        .sectionsVisible(true)
                        .valueVisible(false)
                        .foregroundBaseColor(Color.BLUE)
                        .barColor(Color.GREEN)
                        .sections(new Section(minValue, 0 , Color.GREEN),
                                new Section(0, maxValue, Color.BLUE)
                        )
                        .build();
                gauge.setSkin(new TRZLinearSkin(gauge));
                return gauge;


            }
            private void drawHorizontalRows(CellsRow cellsRow, GraphicsContext gc) {
                if (debug.equalsIgnoreCase("debug")) {
                    gc.setStroke(Color.RED);
                    gc.setLineWidth(0.1d);
                    gc.strokeLine(0, cellsRow.getPixelScreenYPos(), 800, cellsRow.getPixelScreenYPos());
                }
            }
            private void drawVerticalDivsForRow(CellsRow cellsRow, Cell c, GraphicsContext gc) {
                if (debug.equalsIgnoreCase("debug")){
                    int maxHeight = 800;
                    Font f = c.getFont();
                    int width = FontAndColorSelector.getNewInstance().getWidthForFont(f, "W");
                    int x1 = width;
                    int y1 = cellsRow.getPixelScreenYPos() - cellsRow.getMaxHeight();
                    int y2 = cellsRow.getPixelScreenYPos();
                    gc.setStroke(Color.BLUE);
                    gc.setLineWidth(0.1d);
                    while( x1 <= maxHeight){
                        gc.strokeLine(x1, y1, x1, y2);
                        x1 += width;
                    }
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
    public static void main(String[] args) {
        Application.launch(args);
    }
}