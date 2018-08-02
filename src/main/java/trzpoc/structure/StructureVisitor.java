package trzpoc.structure;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import eu.hansolo.medusa.Gauge;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import trzpoc.gui.DrawingText;
import trzpoc.gui.GraphicDesigner;
import trzpoc.utils.RightTextAligner;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 19/11/17
 * Time: 16.54
 */
public class StructureVisitor {

    private String value;
    private DrawingText mainWindow;
    private Multimap<Integer, Node> multipleItems = ArrayListMultimap.create();
    private Map<Integer, Variable> configurations = new HashMap<>();
    private RightTextAligner rightTextAligner;

    public StructureVisitor(DrawingText drawingText) {
        this.mainWindow = drawingText;
        this.rightTextAligner = RightTextAligner.getSingleInstance();
    }
    public Runnable visit(Text cell){
        value = cell.getValue();
        return this.findTextAndFillWithData(cell);
    }
    public Runnable visit(Number number) {
        this.value = number.printFormattedValue();
        return this.findTextAndFillWithData(number);
    }

    public Runnable visit(Variable cell){
        Runnable retValue = null;
        if (cell.isAConfiguration()){
            this.configurations.put(cell.getId(), cell);
        }else {
            cell = this.mergeWithConfiguration(cell);
            if (cell != null) {
                this.value = cell.printFormattedValue();
                retValue = this.findTextAndFillWithData(cell);
            }
        }
        return retValue;
    }

    private Variable mergeWithConfiguration(Variable cell) {
        Variable retValue = null;
        Variable configuration = this.configurations.get(cell.getId());
        if (configuration != null){
            retValue = (Variable) cell.setDecimalLenght(configuration.getDecimalLenght())
                    .setIntegerLenght(configuration.getIntegerLenght()).setFont(configuration.getFont())
                    .setColor(configuration.getColor()).setxPos(configuration.getxPos()).setyPos(configuration.getyPos());
        }else{
            retValue = cell;
        }
        return retValue;
    }

    public Runnable visit(Clear cell){
        //this.mainWindow.getRoot().getScene().
        //ObservableList<Node> nodes = this.mainWindow.getRoot().getChildren();
        //this.mainWindow.getRoot().getChildren().removeAll(nodes);
        this.mainWindow.getRows().clear();
        return new JfxCleaner(this.mainWindow);
    }
    public Runnable visit(RowCleaner cell){
        Collection<javafx.scene.Node> contents = this.mainWindow.getRows().get(cell.getId());



        return new JfxRowCleaner(this.mainWindow, contents);
    }
    public Runnable visit(Bar cell){
        return this.findBarAndFillWithData(cell);
    }

    private Runnable findBarAndFillWithData(Bar variable){
        Runnable returnValue = null;
        String id = String.valueOf(variable.getId());
        Node bar = this.mainWindow.getRoot().getScene().lookup("#" + id);
        if (bar == null){
            bar = GraphicDesigner.createNewInstance().configureBar(variable);
            returnValue = this.addNodeToJfxTree(variable, bar);
        }
        return returnValue;
    }


    private Runnable findTextAndFillWithData(Cell variable){
        Runnable retValue = null;
        String id = String.valueOf(variable.getId());

        // TO TEST: REMOVE SEARCH
        javafx.scene.text.Text myText = (javafx.scene.text.Text)this.mainWindow.getRoot().getScene().lookup("#" + id);
        if (myText == null){
            myText = createTextNode(variable);
            retValue = this.addNodeToJfxTree(variable, myText);
        }
        retValue = this.updateValue(variable);
        return retValue;
    }

/*
    // WRITE FOR TEXT
    private void findTextAndFillWithData(Cell variable){
        // TO TEST: REMOVE SEARCH
        javafx.scene.text.Text myText = this.createTextNode(variable);
        this.addNodeToJfxTree(variable, myText);
        this.updateValue(variable);
    }
*/

    private Runnable updateValue(Cell cell) {
        Runnable retValue = null;
        for (Node n: this.multipleItems.get(cell.getId())){
            if (n instanceof javafx.scene.text.Text){
               retValue = this.update(cell, (javafx.scene.text.Text)n);
            }else{
               retValue = this.update((Gauge)n, cell.getValue());
            }
        }
        return retValue;
    }
    private Runnable update(Cell cell, javafx.scene.text.Text t){
        this.updateStartingPositionToWriteText(cell, t);
        return new JfxTextUpdater(t, value);
    }

    private Runnable update(Gauge t, String rawValue){
        Color color = null;
        double finalValue = 0;

        double valueInDouble = Double.parseDouble(rawValue);
        if (valueInDouble >= t.getMaxValue() || valueInDouble <= t.getMinValue()){
            color = Color.RED;
            finalValue = valueInDouble * 10;
            // Add 30% value to compensate zero pos fixed to 70% of lenght bar
            //t.setValue(valueInDouble + (valueInDouble * 0.3));
        }else{
            color = Color.GREEN;
            finalValue = valueInDouble;
        }
        return new JfxGaugeBarUpdater(color, finalValue, t);
    }


    private Runnable addNodeToJfxTree(Cell variable, Node myText) {
        this.mainWindow.getRows().put(variable.getyPos(), myText);
        this.multipleItems.put(variable.getId(), myText);
        return new JfxTreeEnhancer(myText, this.mainWindow);
    }

    private javafx.scene.text.Text createTextNode(Cell variable) {
        String id = String.valueOf(variable.getId());
        javafx.scene.text.Text retValue = new javafx.scene.text.Text();
        this.updateStartingPositionToWriteText(variable, retValue);
        retValue.setId(id);
        retValue.setFill(variable.getColor());
        retValue.setFont(variable.getFont());
        return retValue;
    }

    private void updateStartingPositionToWriteText(Cell variable, javafx.scene.text.Text retValue) {
        int rightPos = this.rightTextAligner.calculatePrintingPositionByCell(variable);
        retValue.setX(rightPos);
        retValue.setY(variable.getPixelScreenYPos());
    }

}
