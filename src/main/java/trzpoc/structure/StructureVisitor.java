package trzpoc.structure;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import eu.hansolo.medusa.Gauge;
import javafx.collections.ObservableList;
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
    public void visit(Text cell){
        value = cell.getValue();
        this.findTextAndFillWithData(cell);
    }
    public void visit(Number number) {
        this.value = number.printFormattedValue();
        this.findTextAndFillWithData(number);
    }

    public void visit(Variable cell){
        if (cell.isAConfiguration()){
            this.configurations.put(cell.getId(), cell);
        }else {
            cell = this.mergeWithConfiguration(cell);
            if (cell != null) {
                this.value = cell.printFormattedValue();
                this.findTextAndFillWithData(cell);
            }
        }
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

    public void visit(Clear cell){
        //this.mainWindow.getRoot().getScene().
        ObservableList<Node> nodes = this.mainWindow.getRoot().getChildren();
        this.mainWindow.getRoot().getChildren().removeAll(nodes);
        this.mainWindow.getRows().clear();
        this.mainWindow.drawGridOnCanvas();
    }
    public void visit(RowCleaner cell){
        Collection<javafx.scene.Node> contents = this.mainWindow.getRows().get(cell.getId());
        this.mainWindow.getRoot().getChildren().removeAll(contents);
    }
    public void visit(Bar cell){
        this.findBarAndFillWithData(cell);
    }

    private void findBarAndFillWithData(Bar variable){
        String id = String.valueOf(variable.getId());
        Node bar = this.mainWindow.getRoot().getScene().lookup("#" + id);
        if (bar == null){
            bar = GraphicDesigner.createNewInstance().configureBar(variable);
            this.addNodeToJfxTree(variable, bar);
        }
    }


    private void findTextAndFillWithData(Cell variable){
        String id = String.valueOf(variable.getId());
        javafx.scene.text.Text myText = (javafx.scene.text.Text)this.mainWindow.getRoot().getScene().lookup("#" + id);
        if (myText == null){
            myText = createTextNode(variable);
            this.addNodeToJfxTree(variable, myText);
        }
        this.updateValue(variable);
        //myText.setText(this.value);
    }

    private void updateValue(Cell cell) {
        for (Node n: this.multipleItems.get(cell.getId())){
            if (n instanceof javafx.scene.text.Text){
               this.update(cell, (javafx.scene.text.Text)n);
            }else{
               this.update((Gauge)n, cell.getValue());
            }
        }
    }
    private void update(Cell cell, javafx.scene.text.Text t){
        this.updateStartingPositionToWriteText(cell, t);
        t.setText(this.value);
    }

    private void update(Gauge t, String rawValue){
        double valueInDouble = Double.parseDouble(rawValue);
        if (valueInDouble >= t.getMaxValue() || valueInDouble <= t.getMinValue()){
            t.barColorProperty().set(Color.RED);
            // Add 30% value to compensate zero pos fixed to 70% of lenght bar
            //t.setValue(valueInDouble + (valueInDouble * 0.3));
            t.setValue(valueInDouble * 10);
        }else{
            t.barColorProperty().set(Color.GREEN);
            t.setValue(valueInDouble);
        }

    }


    private void addNodeToJfxTree(Cell variable, javafx.scene.Node myText) {
        this.mainWindow.getRoot().getChildren().add(myText);
        this.mainWindow.getRows().put(variable.getyPos(), myText);
        this.multipleItems.put(variable.getId(), myText);
    }

    private javafx.scene.text.Text createTextNode(Cell variable) {
        String id = String.valueOf(variable.getId());
        javafx.scene.text.Text retValue;
        retValue = new javafx.scene.text.Text();
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