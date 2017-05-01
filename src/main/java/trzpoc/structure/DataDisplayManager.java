package trzpoc.structure;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 26/03/17
 * Time: 12.12
 */
public class DataDisplayManager {
    private Collection<CellsRow> rows;


    public static DataDisplayManager getNewInstance(){
        return new DataDisplayManager();
    }

    public DataDisplayManager prepareDisplayMap(int rows){
        for (int i = 0; i < rows; i ++){
            this.rows.add(CellsRow.getEmptyInstance().setyPos(i));
        }
        return this;
    }


    public CellsRow getOrCreateARow(int rowIndex){
        return (this.existRow(rowIndex))? this.returnRow(rowIndex): this.createARow(rowIndex);
    }

    private CellsRow createARow(int rowIndex) {
        this.rows.add(CellsRow.getEmptyInstance().setyPos(rowIndex));
        return this.returnRow(rowIndex);
    }

    private CellsRow returnRow(int rowIndex) {
        CellsRow row = null, retValue = null;
        Iterator<CellsRow> iterator = this.rows.iterator();
        while (iterator.hasNext()){
            row = iterator.next();
            if(row.getyPos() == rowIndex){
                retValue = row;
                break;
            }
        }
        return retValue;
    }

    private boolean existRow(int rowIndex) {
        return this.rows.contains(CellsRow.getEmptyInstance().setyPos(rowIndex));
    }

    public int getNumberOfRows(){
        return this.rows.size();
    }


    private DataDisplayManager(){
        this.rows = new HashSet<CellsRow>();
    }


}
