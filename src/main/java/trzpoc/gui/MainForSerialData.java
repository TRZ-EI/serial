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
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import trzpoc.gui.dataProvider.Label;
import trzpoc.gui.dataProvider.SerialReceiverMock;
import trzpoc.structure.Cell;
import trzpoc.structure.CellsRow;
import trzpoc.structure.DataDisplayManager;
import trzpoc.structure.Variable;
import trzpoc.structure.serial.SerialDataFacade;

import java.io.IOException;
import java.util.Date;

// Font height: big = 36, small = 20

public class MainForSerialData extends Application {


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Graphics Text for TRZ");

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
        this.addTouchEventToStart(canvas);
        this.addMouseEventToStart(canvas);
        this.calculateRows(canvas);
        primaryStage.show();
    }
    private int calculateTextWidthByFont(String text, Font font){
        Text t = new Text(text);
        t.setFont(font);
        return Double.valueOf(t.getLayoutBounds().getWidth()).intValue();
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
                SerialDataFacade sd = SerialDataFacade.createNewInstance();
                try {
                    String realFileName = this.getClass().getClassLoader().getResource("inputExamples.csv").getFile();
                    DataDisplayManager dm = sd.fillMatrixWithData(realFileName);
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    int maxHeight = 0;

                    for (int row = 0; row < dm.getNumberOfRows(); row ++){
                        CellsRow cellsRow = dm.getOrCreateARow(row);
                        maxHeight += cellsRow.getMaxHeight();
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
                            gc.fillText(textToFill, c.getxPos() + maxWidth, c.getyPos() + maxHeight);
                            maxWidth += c.getWidth();
                        }
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }


/*
                System.out.println("HI");
                int y = 0;
                Font font = Font.font("Arial", FontWeight.NORMAL, 20);
                int fontSize = Double.valueOf(font.getSize()).intValue();
                int height = Double.valueOf(canvas.getHeight()).intValue();
                int rows = height / fontSize;
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.setFont(font);
                gc.setFill(Color.RED);
                String label = "PROVA N 1                  Prova.                       ";
                gc.setFill(Color.BLACK);
                gc.fillText(label, 0, y=fontSize);
                // Variable #1
                int xPos = calculateTextWidthByFont(label, font);
                gc.fillText("172.9 sec", xPos, y=fontSize);

                Font small = Font.font("Arial", FontWeight.NORMAL, 16);
                gc.setFont(small);
                gc.fillText("Press. P1 ", 0, fontSize * 3);
                gc.setFill(Color.BLUE);
                // Variable #1
                xPos = calculateTextWidthByFont("Press. P1 ", small);
                gc.fillText("VAR #2 ", xPos, fontSize * 3);
                gc.setFill(Color.BLACK);
                xPos += calculateTextWidthByFont("VAR #2 ", small);
                gc.fillText("BAR", xPos, fontSize * 3);
*/




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
        launch(args);
    }
}