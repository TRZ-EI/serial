package trzpoc.structure;

import trzpoc.utils.FontAndColorSelector;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 26/03/17
 * Time: 12.12
 */
public class DataDisplayManager {
    private ArrayList<CellsRow> rows;
    private int defaultHeight;

    public static DataDisplayManager getNewInstance(){
        return new DataDisplayManager();
    }
    private DataDisplayManager(){

        this.rows = new ArrayList<CellsRow>();
        this.defaultHeight = FontAndColorSelector.getNewInstance().getHeightForSmallFont("W");

    }

    public DataDisplayManager prepareDisplayMap(int rows){
        for (int i = 0; i < rows; i ++){
            this.getOrCreateARow(i);
        }
        return this;
    }

    public CellsRow getOrCreateARow(int rowIndex){
        return (this.existRow(rowIndex))? this.returnRow(rowIndex): this.createARow(rowIndex);
    }

    private CellsRow createARow(int rowIndex) {
        this.rows.add(CellsRow.getEmptyInstance().setyPos(rowIndex).setDefaultHeight(this.defaultHeight));
        this.calculatePixelYPos();
        return this.returnRow(rowIndex);
    }

    public void calculatePixelYPos() {
        int pixelYPos = 0;
        for (int i = 0; i < this.rows.size(); i ++){
            if (i == 0){
                pixelYPos = this.rows.get(i).getMaxHeight();
                this.rows.get(i).setPixelScreenYPos(pixelYPos);
            }else{
                pixelYPos = this.rows.get(i - 1).getPixelScreenYPos() + this.rows.get(i).getMaxHeight();
                this.rows.get(i).setPixelScreenYPos(pixelYPos);
            }
        }
    }

    private CellsRow returnRow(int rowIndex) {
        CellsRow retValue = null;
        int indexInCollection = this.rows.indexOf(CellsRow.getEmptyInstance().setyPos(rowIndex));
        retValue = this.rows.get(indexInCollection);
        this.calculatePixelYPos();
        return retValue;
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
