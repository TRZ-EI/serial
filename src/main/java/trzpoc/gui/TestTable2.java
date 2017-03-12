package trzpoc.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.IOException;
import java.util.Vector;

public class TestTable2 {

    private JFrame frame;
    private Color color;
    private Font font;
    private int row, column;
    private Array2DForCellAttributes array2DForCellAttributes;

    //private CellAttributes[][] = new CellAttributes[200][5];

    public JFrame getFrame(){
        return this.frame;
    }
    public void setColor(Color color){
        this.color = color;
    }
    public Color getColor(){
        return this.color;
    }
    public int getRow(){
        return this.row;
    }
    public int getColumn(){
        return this.column;
    }

    public void setRow(int row){
        this.row = row;
    }
    public void setColumn(int column){
        this.column = column;
    }
    public JTable getTable(){
        return this.table;
    }

    public Font getMyFont(){
        return this.font;
    }
    public void setFont(Font font){
        this.font = font;
    }


    public Array2DForCellAttributes getArray2DForCellAttributes(){
        return this.array2DForCellAttributes;
    }


    public void setValueAt(String value, int row, int column){
        this.table.setValueAt(value, row, column);
    }

    private class TableSearchRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component tableCellRendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            Dimension d = new Dimension(40, 40);
            tableCellRendererComponent.setPreferredSize(d);
            Color actual = getArray2DForCellAttributes().get(column, row).getColor();
            Font actualFont = getArray2DForCellAttributes().get(column, row).getFont();


            if (row == getRow() && column == getColumn()){
                CellAttributes c = getArray2DForCellAttributes().get(column, row);
                c.setColor(getColor());
                c.setFont(getMyFont());
                int rowHeight = (getMyFont() == null)? 20: getMyFont().getSize();
                table.setRowHeight(row, rowHeight);
                getArray2DForCellAttributes().set(c, column, row);
                setForeground(getColor());
                setFont(getMyFont());
            }else{
                setForeground(actual);
                setFont(actualFont);
            }

            /*
            if (getSearch() != null && getSearch().length() > 0 && value.toString().contains(getSearch())) {
                setBackground(getColor());

            }
            */

            return tableCellRendererComponent;
        }
    }

    protected void initUI() {
        this.array2DForCellAttributes = new Array2DForCellAttributes(40, 24);
        this.array2DForCellAttributes.fillMatrixByColor(Color.WHITE);

        DefaultTableModel model = new DefaultTableModel() {
            public boolean isCellEditable(int rowIndex, int mColIndex) {
                return false;
            }
        };

        for (int i = 0; i < 40; i++) {
            model.addColumn("Column " + (i + 1));
        }
        for (int i = 0; i < 24; i++) {
            Vector<Object> row = new Vector<Object>();
            for (int j = 0; j < 40; j++) {
                row.add(new Character(' '));
            }
            model.addRow(row);
        }

        table = new JTable(model);
        this.setColumnSize(table.getColumnModel(), 100);

        TableSearchRenderer renderer = new TableSearchRenderer();
        table.setDefaultRenderer(Object.class, renderer);
        table.setTableHeader(null);
        table.setShowGrid(true);
        table.setRowHeight(40);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);


        this.frame = new JFrame("TRZ POC");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        frame.add(scrollpane, BorderLayout.CENTER);
        frame.setSize(800, 480);
        frame.setVisible(true);
        frame.setResizable(false);
        this.setColumnSize(table.getColumnModel(), 40);

    }

    private void setColumnSize(TableColumnModel model, int width) {
        TableColumn column = null;
        int cols = model.getColumnCount();
        for(int i = 0; i < model.getColumnCount(); i ++){
            column = model.getColumn(i);
            int w = column.getWidth();
            int pw = column.getPreferredWidth();

            column.setPreferredWidth(width);

        }
    }



    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException, IOException {
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        /*
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new TestTable2().initUI();
            }
        });
        */
        TestTable2 window = new TestTable2();
        window.initUI();
        window.getFrame().setVisible(true);

        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        String input = "run";

        while ((input = reader.readLine()).length() != 0) {
            System.out.println(input);

            if (input.equalsIgnoreCase("change")) {
                window.setColor(Color.CYAN);
                window.setFont(new Font("Courier New", Font.BOLD, 20));

                window.setRow(1);
                window.setColumn(2);
                window.setValueAt("C", 1, 2);
                window.getTable().repaint();
            }
            if (input.equalsIgnoreCase("second")) {
                window.setColor(Color.GREEN);
                window.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
                window.setRow(2);
                window.setColumn(2);
                window.setValueAt("S", 2, 2);
                window.getTable().repaint();
            }


            char value = input.charAt(input.length() - 1);

            /*

            if (input.equalsIgnoreCase("R")){
                Font defaultFont = new Font("Courier New", Font.BOLD, 20);
                window.changeFontAndColor(11, 10, defaultFont, Color.RED);
                window.changeContent(11, 10, value);

            }else {
                window.changeContent(10, 10, value);
            }
            */

            /*
            char value = input.charAt(input.length() - 1);

            window.changeContent(10, 10, value);
            window.getDemoFrame().repaint();
            */
            /*
            if (input.equalsIgnoreCase("1")){
                //window.getDemoFrame().
            }
            */


        }




    }

    private JTable table;

}