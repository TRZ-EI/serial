package trzpoc.structure;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 01/11/17
 * Time: 15.04
 */
public class VariableCollector {

    private static VariableCollector instance;
    private Map <Integer, Cell> variableCollection;

    public static VariableCollector getSingleInstance(){
        if (instance == null){
            instance = new VariableCollector();
        }
        return instance;
    }
    private VariableCollector(){
        this.variableCollection = new HashMap<>();
    }

    public Cell addOrGetUpdatedInstance(Cell cell){
        Cell c = this.variableCollection.get(cell.getId());
        if (c == null){
            this.variableCollection.put(cell.getId(), cell);
            c = this.variableCollection.get(cell.getId());
        }
        else {
            c = this.updateCellValues(c, cell);
            this.variableCollection.put(c.getId(), c);
            c = this.variableCollection.get(c.getId());
        }

        return c;
    }

    private Cell updateCellValues(Cell c, Cell cell) {
        if(((Variable)cell).isAConfiguration()){
            this.variableCollection.put(cell.getId(), cell);
            c = this.variableCollection.get(cell.getId());
        }else{
            c.setValue(cell.getValue());
        }
        return c;
    }

    public Cell findCellById(int id){
        return this.variableCollection.get(id);
    }

    public void clear() {
        this.variableCollection.clear();
    }
}
