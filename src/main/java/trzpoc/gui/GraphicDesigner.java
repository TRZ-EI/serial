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
import trzpoc.structure.serial.SerialDataFacade;
import trzpoc.utils.FontAndColorSelector;

import java.io.IOException;

public class GraphicDesigner {
    private  String debug;
    private Canvas canvas;
    private Group group;


    public static GraphicDesigner createNewInstanceByGroupAndCanvasAndDebugParam(Group group, Canvas canvas, String debug){
        return new GraphicDesigner(group, canvas, debug);
    }


    private GraphicDesigner(Group group, Canvas canvas, String debug) {
        this.group = group;
        this.canvas = canvas;
        this.debug = debug;
    }

    public void drawOnCanvas() {
        SerialDataFacade sd = SerialDataFacade.createNewInstance();
        try {
            String realFileName = this.getClass().getClassLoader().getResource("inputExamples.csv").getFile();
            DataDisplayManager dm = sd.fillMatrixWithData(realFileName);

            GraphicsContext gc = this.canvas.getGraphicsContext2D();
            int width = 0;
            for (int row = 0; row < dm.getNumberOfRows(); row++) {
                CellsRow cellsRow = dm.getOrCreateARow(row);
                // DRAW ROWS
                this.drawHorizontalRows(cellsRow, gc);
                boolean isAlreaadyPlotted = false;
                for (int cellIndex = 0; cellIndex < cellsRow.getCellsCount(); cellIndex++) {
                    Cell c = cellsRow.getCellByColumnIndex(cellIndex);
                    // DRAW VERTICAL DIVS FOR ROW
                    if (!isAlreaadyPlotted) {
                        this.drawVerticalDivsForRow(cellsRow, c, gc);
                        isAlreaadyPlotted = true;
                    }
                    gc.setFont(c.getFont());
                    gc.setFill(c.getColor());
                    String textToFill = null;
                    if (c instanceof Variable) {
                        textToFill = ((Variable) c).printFormattedValue();
                    } else if (c instanceof Text) {
                        textToFill = c.getValue();
                    } else if (c instanceof Bar) {
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

    }

    private void configureBar(Cell c, int pixelScreenYPos) {
        // TODO: EXPERIMENTS WITH BARS
        double prefHeight = 200d;
        double prefWidth = 700d;

        Bar cellBar = (Bar) c;
        Gauge bar = this.createOrUpdateHorizontalBar(cellBar.getMinValue(), cellBar.getMaxValue());
        bar.setPrefSize(prefWidth, prefHeight);
        bar.setLayoutX(30);
        bar.setLayoutY(pixelScreenYPos - prefHeight / 2);
        this.group.getChildren().add(bar);
    }

    private Gauge createOrUpdateHorizontalBar(long minValue, long maxValue) {
        Gauge gauge = GaugeBuilder.create()
                .minValue(minValue)
                .maxValue(maxValue)
                .skinType(Gauge.SkinType.LINEAR)
                .orientation(Orientation.HORIZONTAL)
                .sectionsVisible(true)
                .valueVisible(false)
                .foregroundBaseColor(Color.BLUE)
                .barColor(Color.GREEN)
                .sections(new Section(minValue, 0, Color.GREEN),
                        new Section(0, maxValue, Color.BLUE)
                )
                .build();
        gauge.setSkin(new TRZLinearSkin(gauge));
        return gauge;


    }

    private void drawHorizontalRows(CellsRow cellsRow, GraphicsContext gc) {
        if (this.debug.equalsIgnoreCase("debug")) {
            gc.setStroke(Color.RED);
            gc.setLineWidth(0.1d);
            gc.strokeLine(0, cellsRow.getPixelScreenYPos(), 800, cellsRow.getPixelScreenYPos());
        }
    }

    private void drawVerticalDivsForRow(CellsRow cellsRow, Cell c, GraphicsContext gc) {
        if (this.debug.equalsIgnoreCase("debug")) {
            int maxHeight = 800;
            Font f = c.getFont();
            int width = FontAndColorSelector.getNewInstance().getWidthForFont(f, "W");
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
}