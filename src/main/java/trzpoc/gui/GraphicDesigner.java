package trzpoc.gui;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.Section;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import trzpoc.gui.hansolo.skins.TRZLinearSkin;
import trzpoc.structure.*;
import trzpoc.utils.FontAndColorSelector;

import java.util.*;

public class GraphicDesigner {
    private  String debug;
    private Canvas canvas, canvasForGrid;
    private Group group;
    private DataDisplayManager dataDisplayManager;
    private Map<Integer, Gauge> bars;
    private Queue<Gauge> preFetchedBars = new LinkedList<>();
    private FontAndColorSelector fcs;
    private boolean gridIsDrawn;

    public GraphicDesigner() {
        this.fcs = FontAndColorSelector.getNewInstance();
    }


    public static GraphicDesigner createNewInstanceByGroupAndCanvasAndDebugParam(Group group, Canvas canvas, String debug){
        return new GraphicDesigner(group, canvas, debug);
    }
    public static GraphicDesigner createNewInstance(){
        return new GraphicDesigner();

    }

    // TODO Canvas canvas is to remove (not yet useful)

    private GraphicDesigner(Group group, Canvas canvas, String debug) {
        this.group = group;
        this.canvas = canvas;
        this.debug = debug;
        this.bars = new HashMap<>();
        this.preFetchedBars.add(this.createOrUpdateHorizontalBar(0,0));
        this.preFetchedBars.add(this.createOrUpdateHorizontalBar(0,0));
        this.fcs = FontAndColorSelector.getNewInstance();
        this.gridIsDrawn = false;
    }
    // TODO Delete
    public void drawOnCanvas(DataDisplayManager dm) {
        this.dataDisplayManager = dm;
        this.drawGrid();
        this.drawOnCanvas();
    }
    public void clearScreen(DataDisplayManager dataDisplayManager){
        for (int row = 0; row < dataDisplayManager.getNumberOfRows(); row++) {
            CellsRow cellsRow = dataDisplayManager.getOrCreateARow(row);

            Canvas canvasForRow = cellsRow.getCanvas();
            if (canvasForRow != null) {
                this.clearCanvas(canvasForRow);
            }
        }
    }

    // TODO Delete
    public void drawASingleRowOnCanvas(CellsRow cellsRow) {
        if(!gridIsDrawn) {
            this.drawGrid();
        }
        this.drawRowOnCanvas(cellsRow);
    }
    // TODO Delete
    private void drawGrid(){
        this.canvasForGrid.getGraphicsContext2D().clearRect(0,0, canvasForGrid.getWidth(),canvasForGrid.getHeight());

        for (int row = 0; row < this.dataDisplayManager.getNumberOfRows(); row++) {
            CellsRow cellsRow = this.dataDisplayManager.getOrCreateARow(row);
            // DRAW ROW
            this.drawHorizontalRows(cellsRow);
            // DRAW VERTICAL DIVS FOR ROW
            this.drawVerticalDivsForRow(cellsRow);

        }

    }
    // TODO Delete
    private void drawRowOnCanvas(CellsRow cellsRow) {
        long start = System.nanoTime();
            if (cellsRow.isNecessaryToRedraw()) {

                for (int cellIndex = 0; cellIndex < cellsRow.getCellsCount(); cellIndex++) {
                    Cell c = cellsRow.getCellByColumnIndex(cellIndex);
                    this.drawACellInScene(c);
                }
                cellsRow.switchOffRedrawFlag();


            }
        this.canvas.toFront();
        if (debug.equalsIgnoreCase("debug")) {
            long elapsed = (System.nanoTime() - start) / 1000;
            System.out.println("Time to draw the row " + cellsRow.getyPos() + " (micros): " + elapsed);
        }
    }

    public void drawACellInScene(Cell c) {
        int width = this.fcs.getWidthForSmallFont("W");
        String textToFill = null;
        if (c instanceof Variable) {
            Variable v = (Variable)c;
            textToFill = v.printFormattedValue();
        } else if (c instanceof Text) {
            textToFill = c.getValue();
        } else if (c instanceof Bar) {
            if (this.bars.get(c.getId()) != null) {
                this.updateBarValuesAndColor(c);
            } else {
                this.configureBar(c);
            }
        }
        if (textToFill != null && textToFill.length() > 0) {
            String nodeId = String.valueOf(c.getId());
            javafx.scene.text.Text myText = (javafx.scene.text.Text)this.group.getScene().lookup("#" + nodeId);
            if (myText != null){
                myText.setText(textToFill);
            }else{
                myText = new javafx.scene.text.Text(c.getxPos() * width, c.getPixelScreenYPos(), textToFill);
                myText.setFont(c.getFont());
                myText.setFill(c.getColor());
                myText.setId(String.valueOf(c.getId()));
                
                this.group.getChildren().add(myText);
            }
        }
    }
    // TODO Delete
    private void drawOnCanvas() {
        List<Canvas> filledCanvases = new ArrayList<Canvas>();

            //TODO: REMOVE AFTER TEST

            int width = 0;
            for (int row = 0; row < this.dataDisplayManager.getNumberOfRows(); row++) {
                long before =System.nanoTime();
                CellsRow cellsRow = this.dataDisplayManager.getOrCreateARow(row);
                if (cellsRow.isNecessaryToRedraw()) {
                    long start = System.nanoTime();
                    Canvas canvasForRow = cellsRow.getCanvas();
                    /*
                    if (!this.group.getChildren().contains(canvasForRow)) {
                        this.group.getChildren().add(canvasForRow);
                    }
                    */
                    this.clearCanvas(canvasForRow);
                    //Canvas canvasForRow = new Canvas(800, 400);
                    //canvasForRow.toFront();
                    GraphicsContext gc = canvasForRow.getGraphicsContext2D();
                    for (int cellIndex = 0; cellIndex < cellsRow.getCellsCount(); cellIndex++) {
                        Cell c = cellsRow.getCellByColumnIndex(cellIndex);
                        //if (c.isChanged()) {
                        gc.setFont(c.getFont());
                        gc.setFill(c.getColor());
                        String textToFill = null;
                        if (c instanceof Variable) {
                            textToFill = ((Variable) c).printFormattedValue();
                        } else if (c instanceof Text) {
                            textToFill = c.getValue();
                        } else if (c instanceof Bar) {
                            if (this.bars.get(c.getId()) != null) {
                                this.updateBarValuesAndColor(c);
                            } else {
                                this.configureBar(c);
                            }
                        }
                        if (textToFill != null && textToFill.length() > 0) {
                            Font smallFont = this.fcs.getSmallFont();
                            width = this.fcs.getWidthForFont(smallFont, "W");
                            //gc.clearRect(c.getxPos() * width, c.getPixelScreenYPosUpper(), c.getWidth(), c.getHeight());
                            gc.fillText(textToFill, c.getxPos() * width, cellsRow.getPixelScreenYPos());
                        }

                        //}
                    }
                    filledCanvases.add(canvasForRow);

                    cellsRow.switchOffRedrawFlag();
                    long elapsed = (System.nanoTime() - start) / 1000;
                    System.out.println("Time to draw a row (micros): " + elapsed);

                }
            }
            //TODO: manage the method - it costs a lot of time

    }


    private void updateBarValuesAndColor(Cell c) {
        Bar b = (Bar)c;
        double value = (c.getValue() != null)? Double.parseDouble(c.getValue()): ((Bar)c).getMinValue();
        double delta = b.getMaxValue() - b.getMinValue();
        double blueReference = b.getMinValue() + 0.8 * delta;
        Color barColor = (value >= blueReference)? Color.BLUE: Color.GREEN;
        this.bars.get(c.getId()).barColorProperty().set(barColor);
        this.bars.get(c.getId()).setValue(value);
    }

    private void clearCanvas(Canvas canvas) {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        canvas.getGraphicsContext2D().clearRect(0,0,width, height);

    }

    private void configureBar(Cell c) {
        // TODO: EXPERIMENTS WITH BARS
        double prefHeight = 200d;
        double prefWidth = 700d;
        int pixelScreenYPos = c.getPixelScreenYPos();


        Bar cellBar = (Bar) c;
        Gauge bar = null;
        if (!this.preFetchedBars.isEmpty()){
            bar = this.preFetchedBars.remove();
        }else{
            bar = this.createOrUpdateHorizontalBar(cellBar.getMinValue(), cellBar.getMaxValue());
        }

        //Gauge

        bar.addSection(new Section(cellBar.getMinValue(), 0, Color.GREEN));
        bar.addSection(new Section(0, cellBar.getMaxValue(), Color.BLUE));

        bar.setValue(cellBar.getMinValue());
        bar.setMinValue(cellBar.getMinValue());
        bar.setMaxValue(cellBar.getMaxValue());

        bar.setPrefSize(prefWidth, prefHeight);
        bar.setLayoutX(30);
        bar.setLayoutY(pixelScreenYPos - prefHeight / 2);
        this.group.getChildren().add(bar);
        this.bars.put(c.getId(), bar);
    }

    private Gauge createOrUpdateHorizontalBar(long minValue, long maxValue) {
        Gauge gauge = GaugeBuilder.create()
                .value(minValue)
                .minValue(minValue)
                .maxValue(maxValue)
                .skinType(Gauge.SkinType.LINEAR)
                .orientation(Orientation.HORIZONTAL)
                .sectionsVisible(true)
                .valueVisible(false)
                .foregroundBaseColor(Color.BLUE)
                .barColor(Color.GREEN)
                /*
                .sections(new Section(minValue, 0, Color.GREEN),
                        new Section(0, maxValue, Color.BLUE)
                )
                */
                .build();
        gauge.setSkin(new TRZLinearSkin(gauge));
        return gauge;


    }

    // TODO Delete
    private void drawHorizontalRows(CellsRow cellsRow) {
        this.drawHorizontalLinesOnCanvas(this.canvasForGrid);
        //gc.strokeLine(0, cellsRow.getPixelScreenYPos(), canvasWidth, cellsRow.getPixelScreenYPos());
    }

    public void drawGridForGraphicHelp(Canvas gridCanvas){
        this.drawHorizontalLinesOnCanvas(gridCanvas);
        this.drawVerticalLinesOnCanvas(gridCanvas);
        if (this.debug != null && this.debug.equalsIgnoreCase("debug")) {
        }
    }

    private void drawHorizontalLinesOnCanvas(Canvas gridCanvas) {
        int canvasHeight = Double.valueOf(gridCanvas.getHeight()).intValue();
        int canvasWidth =  Double.valueOf(gridCanvas.getWidth()).intValue();
        int rowHeight = this.fcs.getHeightForSmallFont("W");
        GraphicsContext gc = gridCanvas.getGraphicsContext2D();
        gc.setStroke(Color.RED);
        gc.setLineWidth(0.1d);
        int rows = canvasHeight / rowHeight;
        for (int i = 0; i < rows; i ++){
            gc.strokeLine(0, rowHeight * i, canvasWidth, rowHeight * i);
        }
    }
    private void drawVerticalLinesOnCanvas(Canvas gridCanvas) {
        int canvasHeight = Double.valueOf(gridCanvas.getHeight()).intValue();
        int canvasWidth =  Double.valueOf(gridCanvas.getWidth()).intValue();
        int fontWidth = this.fcs.getWidthForSmallFont("W");
        int verticalLines = canvasWidth / fontWidth;
        GraphicsContext gc = gridCanvas.getGraphicsContext2D();
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(0.1d);
        for (int i = 1; i < verticalLines; i++){
            gc.strokeLine(fontWidth * i, 0, fontWidth * i, canvasHeight);
        }
    }

    // TODO Delete
    private void drawVerticalDivsForRow(CellsRow cellsRow) {
        this.drawVerticalLinesOnCanvas(this.canvasForGrid);

        /*
        int maxHeight = 800;
        int width = fontAndColorSelector.getWidthForFont(font, "W");
        int x1 = fontWidth;
        int y1 = cellsRow.getPixelScreenYPos() - cellsRow.getMaxHeight();
        int y2 = cellsRow.getPixelScreenYPos();
        while (x1 <= maxHeight) {
            gc.strokeLine(x1, y1, x1, y2);
            x1 += width;
        }
        */
    }


    public void setCanvasForGrid(Canvas canvasForGrid) {
        this.canvasForGrid = canvasForGrid;
    }

    public void setDataDisplayManager(DataDisplayManager dataDisplayManager) {
        this.dataDisplayManager = dataDisplayManager;
    }
}