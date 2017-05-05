package trzpoc.structure;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 26/03/17
 * Time: 12.12
 */
public class DataDisplayManager {
    private ArrayList<CellsRow> rows;


    public static DataDisplayManager getNewInstance(){
        return new DataDisplayManager();
    }
    private DataDisplayManager(){
        this.rows = new ArrayList<CellsRow>();
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
        int indexInCollection = this.rows.indexOf(CellsRow.getEmptyInstance().setyPos(rowIndex));
        return this.rows.get(indexInCollection);
    }

    private boolean existRow(int rowIndex) {
        return this.rows.contains(CellsRow.getEmptyInstance().setyPos(rowIndex));
    }

    public int getNumberOfRows(){
        return this.rows.size();
    }


    public void AddOrUpdateCellInMatrix(Cell dataParsed) {
        int rowIndex = dataParsed.getyPos();
        CellsRow row = this.getOrCreateARow(rowIndex);
        row.addOrUpdateACell(dataParsed);
    }

    public ArrayList<CellsRow> getRows() {
        return rows;
    }

    public void setRows(ArrayList<CellsRow> rows) {
        this.rows = rows;
    }
}
