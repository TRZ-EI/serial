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


    public static GraphicDesigner createNewInstanceByGroupAndCanvasAndDebugParam(Group group, Canvas canvas, String debug){
        return new GraphicDesigner(group, canvas, debug);
    }


    private GraphicDesigner(Group group, Canvas canvas, String debug) {
        this.group = group;
        this.canvas = canvas;
        this.debug = debug;
        this.bars = new HashMap<>();
        this.preFetchedBars.add(this.createOrUpdateHorizontalBar(0,0));
        this.preFetchedBars.add(this.createOrUpdateHorizontalBar(0,0));
        this.fcs = FontAndColorSelector.getNewInstance();
    }
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

    public void drawASingleRowOnCanvas(CellsRow cellsRow) {
        this.drawGrid();
        this.drawRowOnCanvas(cellsRow);
    }

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

    private void drawRowOnCanvas(CellsRow cellsRow) {
            List<Canvas> filledCanvases = new ArrayList<Canvas>();
            int width = 0;
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
                            this.configureBar(c, cellsRow.getPixelScreenYPos());
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
                if (debug.equalsIgnoreCase("debug")) {
                    long elapsed = (System.nanoTime() - start) / 1000;
                    System.out.println("Time to draw the row " + cellsRow.getyPos() + " (micros): " + elapsed);
                }

            }
        this.addOrReplaceCanvasesToGroup(filledCanvases);
        this.canvas.toFront();
        //TODO: REMOVE AFTER TEST

    }

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
                                this.configureBar(c, cellsRow.getPixelScreenYPos());
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
            this.addOrReplaceCanvasesToGroup(filledCanvases);
    //TODO: REMOVE AFTER TEST

    }

    private void addOrReplaceCanvasesToGroup(List<Canvas> filledCanvases) {
        for (Canvas canvas: filledCanvases){
            if (this.group.getChildren().contains(canvas)){
                this.group.getChildren().remove(canvas);
                this.group.getChildren().add(canvas);
            }else{
                this.group.getChildren().add(canvas);
            }
        }
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

    private void configureBar(Cell c, int pixelScreenYPos) {
        // TODO: EXPERIMENTS WITH BARS
        double prefHeight = 200d;
        double prefWidth = 700d;

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

    private void drawHorizontalRows(CellsRow cellsRow) {
        if (this.debug.equalsIgnoreCase("debug")) {
            GraphicsContext gc = this.canvasForGrid.getGraphicsContext2D();
            gc.setStroke(Color.RED);
            gc.setLineWidth(0.1d);
            gc.strokeLine(0, cellsRow.getPixelScreenYPos(), 800, cellsRow.getPixelScreenYPos());
        }
    }

    private void drawVerticalDivsForRow(CellsRow cellsRow) {
        FontAndColorSelector fontAndColorSelector = FontAndColorSelector.getNewInstance();
        Font font = fontAndColorSelector.getSmallFont();
        /*
        if (cellsRow.getCellsCount() > 0){
          font = cellsRow.getCellByColumnIndex(0).getFont();
        }
        */
        if (this.debug.equalsIgnoreCase("debug") && font != null) {
            GraphicsContext gc = this.canvasForGrid.getGraphicsContext2D();
            int maxHeight = 800;
            int width = fontAndColorSelector.getWidthForFont(font, "W");
            int x1 = width;
            int y1 = cellsRow.getPixelScreenYPos() - cellsRow.getMaxHeight();
            int y2 = cellsRow.getPixelScreenYPos();
            gc.setStroke(Color.BLUE);
            gc.setLineWidth(0.1d);
            while (x1 <= maxHeight) {
                gc.strokeLine(x1, y1, x1, y2);
                x1 += width;
            }
        }
    }

    public void setCanvasForGrid(Canvas canvasForGrid) {
        this.canvasForGrid = canvasForGrid;
    }

    public void setDataDisplayManager(DataDisplayManager dataDisplayManager) {
        this.dataDisplayManager = dataDisplayManager;
    }
}