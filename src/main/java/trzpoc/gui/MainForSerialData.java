package trzpoc.gui;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 05/05/17
 * Time: 16.03
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
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import trzpoc.gui.dataProvider.Label;
import trzpoc.gui.dataProvider.SerialReceiverMock;
import trzpoc.structure.Cell;
import trzpoc.structure.CellsRow;
import trzpoc.structure.DataDisplayManager;
import trzpoc.structure.Variable;
import trzpoc.structure.serial.SerialDataFacade;
import trzpoc.utils.FontAndColorSelector;

import java.io.IOException;
import java.util.Date;

// Font height: big = 36, small = 20

public class MainForSerialData extends Application {

    protected Group root;
    protected Stage primaryStage;
    private String debug;

    @Override
    public void start(Stage primaryStage) {
        Parameters params = this.getParameters();
        this.debug = (String)params.getRaw().get(0);



        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("JavaFX Graphics Text for TRZ");

        this.root = new Group();
        Scene scene = new Scene(root);
        this.primaryStage.setScene(scene);
        this.addCombinationKeyAcceleratorToExit(primaryStage);
        Canvas canvas = new Canvas(800, 480);


        // TODO: CREATE A PRIVATE METHOD
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(canvas);
        scrollPane.setPrefSize(800, 480);
        scrollPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        scrollPane.setFitToHeight(false);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

                                   


        root.getChildren().add(scrollPane);
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
                    int maxHeight = 0;
                    int width = 0;
                    for (int row = 0; row < dm.getNumberOfRows(); row ++){
                        CellsRow cellsRow = dm.getOrCreateARow(row);
                        int maxWidth = 0;
                        for(int cellIndex = 0; cellIndex < cellsRow.getCellsCount(); cellIndex ++){
                            Cell c = cellsRow.getCellByColumnIndex(cellIndex);
                            gc.setFont(c.getFont());
                            gc.setFill(c.getColor());
                            String textToFill = null;
                            if (c instanceof Variable){
                                textToFill = ((Variable)c).printFormattedValue();
                            }else{
                                textToFill = c.getValue();
                            }
                            if (textToFill.length() > 0) {
                                FontAndColorSelector fcs = FontAndColorSelector.getNewInstance();
                                width = fcs.getWidthForFont(c.getFont(), "W");
                                gc.fillText(textToFill, c.getxPos() * width, c.getyPos() + maxHeight);
                            }


                            maxWidth += c.getWidth();
                        }
                        maxHeight += cellsRow.getMaxHeight();

                    }
                    if (debug.equalsIgnoreCase("debug")) {
                        int yMax = 480;
                        gc.setStroke(Color.BLUE);
                        gc.setLineWidth(0.1d);
                        int rows = 800 / width;
                        for (int row = 0; row < rows; row++) {
                            gc.strokeLine(row * width, 0, row * width, yMax);
                        }
                    }
/* *********************************************************************
                    Gauge gauge21 = GaugeBuilder.create()
                            .skinType(Gauge.SkinType.LINEAR)
                            .title("Linear")
                            .orientation(Orientation.HORIZONTAL)
                            .sectionsVisible(true)
                            .foregroundBaseColor(Color.GRAY)
                            .barColor(Color.LIGHTSKYBLUE)
                            .sections(new Section(0, 20, Color.BLUE),
                                    new Section(80, 100, Color.RED))
                            .build();
                            VBox controls = new VBox();
                            
                            controls.setSpacing(5);
                            controls.setAlignment(Pos.CENTER);
                            controls.getChildren().addAll(canvas, gauge21);
                            root.getChildren().add(controls);
                            primaryStage.show();



/****** */


                } catch (IOException e) {
                    e.printStackTrace();
                }
                me.consume();
                this.writeRows(canvas);
            }

            private void writeRows(Canvas canvas) {



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
        args = new String[]{"DEBUG"};
        launch(args);
    }
}