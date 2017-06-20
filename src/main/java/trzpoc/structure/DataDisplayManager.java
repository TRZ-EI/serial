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
        if (dataParsed instanceof Variable){
           this.manageVariable(dataParsed);

        }else if(dataParsed instanceof Text){
            this.manageText(dataParsed);

        }else if(dataParsed instanceof Bar){
            this.manageBar(dataParsed);
        }
        this.calculatePixelYPos();
    }

    private void manageVariable(Cell dataParsed) {
        Variable v = (Variable)dataParsed;
        Cell existentCell = this.findCellById(v.getId());
        if (existentCell != null){
            this.setPossibleValueForVariable(existentCell, v);
        }else if (v.isAConfiguration()){
            this.insertNewCell(v);
        }
    }
    private void manageText(Cell dataParsed) {
        Text t = (Text)dataParsed;
        Cell existentCell = this.findCellByPosition(t);
        if (existentCell != null) {
            this.setPossibleValueForText(existentCell, t);
        }else{
            this.insertNewCell(t);
        }
    }
    private void manageBar(Cell dataParsed) {
        Bar b = (Bar)dataParsed;
        Cell existentCell = this.findCellById(b.getId());
        if (existentCell != null){
            this.setPossibleValuesForBar(existentCell, b);
        }else{
            this.insertNewCell(b);
        }
    }
    private void insertNewCell(Cell dataParsed) {
        int rowIndex = dataParsed.getyPos();
        CellsRow row = this.getOrCreateARow(rowIndex);
        row.addOrUpdateACell(dataParsed);
    }
    private void setPossibleValueForVariable(Cell c, Cell dataParsed) {

            if (((Variable) dataParsed).isAConfiguration()) {
                c.setFont(dataParsed.getFont());
                c.setxPos(dataParsed.getxPos());
                c.setyPos(dataParsed.getyPos());
            }else{
                c.setValue(dataParsed.getValue());
            }
    }

    private void setPossibleValueForText(Cell c, Cell dataParsed) {
            c.setValue(dataParsed.getValue());
            c.setxPos(dataParsed.getxPos());
            c.setyPos(dataParsed.getyPos());
    }

    private void setPossibleValuesForBar(Cell c, Cell dataParsed) {
        if (c instanceof Bar && dataParsed instanceof Bar){
            ((Bar) c).setMaxValue(((Bar) dataParsed).getMaxValue());
            ((Bar) c).setMinValue(((Bar) dataParsed).getMinValue());
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
