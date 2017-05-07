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


    public void addOrUpdateCellInMatrix(Cell dataParsed) {
        Cell c = null;
        if (dataParsed instanceof Variable){
            c = this.findCellById(dataParsed.getId());
        }else{
            c = this.findCellByPosition(dataParsed);
        }
        if (c != null){
            c.setValue(dataParsed.getValue());
        }else{
            int rowIndex = dataParsed.getyPos();
            CellsRow row = this.getOrCreateARow(rowIndex);
            row.addOrUpdateACell(dataParsed);
        }
    }
    private Cell findCellByPosition(Cell toFind) {
        Cell retValue = null, tempValue = null;
        for (int rowIndex = 0; rowIndex < this.getNumberOfRows(); rowIndex ++){
            CellsRow aRow = this.returnRow(rowIndex);
            for (int columnIndex = 0; columnIndex < aRow.getCellsCount(); columnIndex ++){
                tempValue = aRow.getCellByColumnIndex(columnIndex);
                if (toFind.equals(tempValue)){
                    retValue = tempValue;
                    break;
                }
            }
        }
        return retValue;
    }

    private Cell findCellById(int id) {
        Cell retValue = null, tempValue = null;
        for (int rowIndex = 0; rowIndex < this.getNumberOfRows(); rowIndex ++){
            CellsRow aRow = this.returnRow(rowIndex);
            for (int columnIndex = 0; columnIndex < aRow.getCellsCount(); columnIndex ++){
                tempValue = aRow.getCellByColumnIndex(columnIndex);
                if (tempValue.getId() == id){
                    retValue = tempValue;
                    break;
                }
            }
        }
        return retValue;
    }

    public ArrayList<CellsRow> getRows() {
        return rows;
    }

    public void setRows(ArrayList<CellsRow> rows) {
        this.rows = rows;
    }
}
