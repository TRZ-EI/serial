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


    public int getWidthByCells(){
        int retValue = 0;
        for (Iterator<Cell> it = this.cells.iterator(); it.hasNext(); ) {
            retValue += it.next().getWidth();
        }
        return retValue;
    }
    // TO DO: TEST AND VERIFY
    public int getMaxHeight(){
        List<Integer> heights = new ArrayList<>();
        Iterator<Cell> iterator = this.cells.iterator();
        while(iterator.hasNext()){
            heights.add(iterator.next().getHeight());
        }
        Collections.sort(heights);
        return heights.get(heights.size() - 1);
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
