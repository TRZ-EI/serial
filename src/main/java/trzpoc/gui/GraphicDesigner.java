package trzpoc.gui;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import trzpoc.gui.hansolo.skins.TRZLinearSkin;
import trzpoc.structure.Bar;
import trzpoc.utils.ConfigurationHolder;
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
        this.debug = ConfigurationHolder.getInstance().getProperties().getProperty(ConfigurationHolder.DEBUG);
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


    public Gauge configureBar(Bar cellBar) {
        // TODO: EXPERIMENTS WITH BARS
        double prefHeight = 200d;
        double prefWidth = 890d;
        int pixelScreenYPos = cellBar.getPixelScreenYPos();
        Gauge bar = this.createOrUpdateHorizontalBar(cellBar.getMinValue(), cellBar.getMaxValue());

        bar.setValue(cellBar.getMinValue());
        bar.setMinValue(cellBar.getMinValue());
        bar.setMaxValue(cellBar.getMaxValue());



        bar.setPrefSize(prefWidth, prefHeight);
        bar.setLayoutX(-45);
        bar.setLayoutY(pixelScreenYPos - prefHeight / 2);
        return bar;
    }

    private Gauge createOrUpdateHorizontalBar(long minValue, long maxValue) {
        Gauge gauge = GaugeBuilder.create()
                .value(minValue)
                .minValue(minValue)
                .maxValue(maxValue)
                .skinType(Gauge.SkinType.LINEAR)
                .startFromZero(true)
                .keepAspect(false)
                .animated(true)
                .orientation(Orientation.HORIZONTAL)
                .valueVisible(false)
                .foregroundBaseColor(Color.BLUE)
                .barColor(Color.GREEN)



                .build();
        gauge.setSkin(new TRZLinearSkin(gauge));
        return gauge;


    }


    public void drawGridForGraphicHelp(Canvas gridCanvas){
        if (this.debug != null && this.debug.equalsIgnoreCase("debug")) {
            this.drawHorizontalLinesOnCanvas(gridCanvas);
            this.drawVerticalLinesOnCanvas(gridCanvas);
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