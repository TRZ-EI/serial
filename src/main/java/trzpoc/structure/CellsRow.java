package trzpoc.structure;

import javafx.scene.canvas.Canvas;
import trzpoc.utils.FontAndColorSelector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 19/03/17
 * Time: 16.38
 */
public class CellsRow {
    private List<Cell> cells;
    private int maxWidth;
    private int pixelScreenYPos;
    private int yPos;
    private int defaultHeight; // pixels
    private boolean redraw;
    private Canvas canvas;
    


    public static CellsRow getEmptyInstance() {
        return new CellsRow();
    }

    private CellsRow(){
        this.redraw = false;
        this.cells = new ArrayList<Cell>();
    }
    public int getWidthByCells(){
        int retValue = 0;
        for (Iterator<Cell> it = this.cells.iterator(); it.hasNext(); ) {
            retValue += it.next().getWidth();
        }
        return retValue;
    }
    // TO DO: TEST AND VERIFY
    public int getMaxHeight(){
        return FontAndColorSelector.getNewInstance().getHeightForSmallFont("W");
    }

    public Cell addOrUpdateACell(Cell cell){
        return this.cells.contains(cell)? this.updateCell(cell): this.addCell(cell);
    }
    public Cell getCellByColumnIndex(int columnIndex){
        return (Cell) this.cells.toArray()[columnIndex];
    }

    private Cell addCell(Cell cell) {
        cell.setPixelScreenYPos(this.pixelScreenYPos);
        this.cells.add(cell);
        int indexInCollection = this.cells.indexOf(cell);
        this.redraw = true;
        return this.cells.get(indexInCollection);
    }

    private Cell updateCell(Cell cell) {
        int indexInCollection = this.cells.indexOf(cell);
        Cell cellToUpdate = this.cells.get(indexInCollection);
        if (cell.getColor() != null){
            cellToUpdate.setColor(cell.getColor());
        }
        if (cell.getFont() != null){
            cellToUpdate.setFont(cell.getFont());
        }
        if (cell instanceof Variable && cellToUpdate instanceof Variable){
            Variable v = (Variable)cellToUpdate;
            Variable c = (Variable)cell;
            v.setDecimalLenght(c.getDecimalLenght());
            v.setIntegerLenght(c.getIntegerLenght());
        }
        cellToUpdate.setValue(cell.getValue());
        cellToUpdate.setPixelScreenYPos(this.pixelScreenYPos);

        this.cells.add(indexInCollection, cellToUpdate);
        this.redraw = true;
        return this.cells.get(indexInCollection);
    }

    public int getCellsCount(){
        return this.cells.size();
    }
    public int getyPos() {
        return yPos;
    }
    public CellsRow setyPos(int yPos) {
        this.yPos = yPos;
        return this;
    }
    public List<Cell> getCells() {
        return cells;
    }
    public CellsRow setCells(List<Cell> cells) {
        this.cells = cells;
        return this;
    }
    @Override
    public boolean equals(Object o) {
        boolean  result = false;
        if (o instanceof CellsRow){
            CellsRow other = (CellsRow) o;
            result = this.yPos == other.getyPos();
        }
        return result;
    }


    public int getPixelScreenYPos() {
        return pixelScreenYPos;
    }

    public CellsRow setPixelScreenYPos(int pixelScreenYPos) {
        this.pixelScreenYPos = pixelScreenYPos;
        return this;
    }

    public int getDefaultHeight() {
        return defaultHeight;
    }

    public CellsRow setDefaultHeight(int defaultHeight) {
        this.defaultHeight = defaultHeight;
        return this;
    }
    public boolean isNecessaryToRedraw(){
        return this.redraw;
    }
    public CellsRow switchOffRedrawFlag(){
        this.redraw = false;
        return this;
    }

    public Canvas getCanvas() {
        if (this.canvas == null){
            this.canvas = new Canvas(800, 400);
        }
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }
}
