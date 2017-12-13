package trzpoc.structure;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import eu.hansolo.medusa.Gauge;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import trzpoc.gui.DrawingText;
import trzpoc.gui.GraphicDesigner;

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

    public StructureVisitor(DrawingText drawingText) {
        this.mainWindow = drawingText;
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
                value = cell.printFormattedValue();
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
    }
        return retValue;
    }

    public void visit(Clear cell){
        this.mainWindow.getRoot().getChildren().clear();
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
               this.update((javafx.scene.text.Text)n);
            }else{
               this.update((Gauge)n, cell.getValue());
            }
        }
    }
    private void update(javafx.scene.text.Text t){
        t.setText(this.value);
    }
    private void update(Gauge t, String rawValue){
        double valueInDouble = Double.parseDouble(rawValue);
        double delta = t.getMaxValue() - t.getMinValue();
        //double blueReference = t.getMinValue() + 0.8 * delta;
        double blueReference = 0d;
        Color barColor = (valueInDouble > blueReference)? Color.BLUE: Color.GREEN;
        t.barColorProperty().set(barColor);
        t.setValue(valueInDouble);
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
        retValue.setX(variable.getPixelScreenXPos());
        retValue.setY(variable.getPixelScreenYPos());
        retValue.setId(id);
        retValue.setFill(variable.getColor());
        retValue.setFont(variable.getFont());
        return retValue;
    }

}
