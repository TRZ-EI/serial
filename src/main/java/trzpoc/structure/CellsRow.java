package trzpoc.structure;

import java.util.ArrayList;
import java.util.Collections;
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
    private int yPos;
    


    public static CellsRow getEmptyInstance() {
        return new CellsRow();
    }

    private CellsRow(){
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
        int defaultHeight = 20; // pixels
        List<Integer> heights = new ArrayList<>();
        Iterator<Cell> iterator = this.cells.iterator();
        while(iterator.hasNext()){
            heights.add(iterator.next().getHeight());
        }
        Collections.sort(heights);
        return (heights.size() > 0)? heights.get(heights.size() - 1):defaultHeight;
    }

    public Cell addOrUpdateACell(Cell cell){
        return this.cells.contains(cell)? this.updateCell(cell): this.addCell(cell);
    }
    public Cell getCellByColumnIndex(int columnIndex){
        return (Cell) this.cells.toArray()[columnIndex];
    }

    private Cell addCell(Cell cell) {
        this.cells.add(cell);
        int indexInCollection = this.cells.indexOf(cell);
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
        this.cells.add(indexInCollection, cellToUpdate);
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



}
