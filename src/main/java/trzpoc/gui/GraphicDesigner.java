package trzpoc.gui;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.Section;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import trzpoc.gui.hansolo.skins.TRZLinearSkin;
import trzpoc.structure.*;
import trzpoc.utils.FontAndColorSelector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class GraphicDesigner {
    private  String debug;
    private Canvas canvas, canvasForGrid;
    private Group group;
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



    private void updateBarValuesAndColor(Cell c) {
        Bar b = (Bar)c;
        double value = (c.getValue() != null)? Double.parseDouble(c.getValue()): ((Bar)c).getMinValue();
        double delta = b.getMaxValue() - b.getMinValue();
        double blueReference = b.getMinValue() + 0.8 * delta;
        Color barColor = (value >= blueReference)? Color.BLUE: Color.GREEN;
        this.bars.get(c.getId()).barColorProperty().set(barColor);
        this.bars.get(c.getId()).setValue(value);
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



    public void setCanvasForGrid(Canvas canvasForGrid) {
        this.canvasForGrid = canvasForGrid;
    }

}