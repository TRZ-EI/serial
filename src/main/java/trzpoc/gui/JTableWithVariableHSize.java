package trzpoc.gui; /**
 * Created with IntelliJ IDEA.
 * User: luigi
 * Date: 15/01/17
 * Time: 10.46
 */

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.Vector;

public class JTableWithVariableHSize {


    private int width, height;
    private JTable table;
    private JFrame frame;
    private Color color;
    private Font font;
    private int row, column;
    private Array2DForCellAttributes array2DForCellAttributes;

    public JTableWithVariableHSize(int width, int height) {
        this.width = width;
        this.height = height;
        this.row = -1;
        this.column = -1;


    }

    public JFrame getFrame() {
        return this.frame;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public JTable getTable() {
        return this.table;
    }

    public Font getMyFont() {
        return this.font;
    }

    public void setMyFont(Font font) {
        this.font = font;
    }

    public Array2DForCellAttributes getArray2DForCellAttributes() {
        return this.array2DForCellAttributes;
    }

    public Color getBackground() {
        return Color.GREEN;
    }

    public static void main(String[] argv) throws IOException {
        JTableWithVariableHSize window = new JTableWithVariableHSize(20, 20);
        window.init();
        window.getDemoFrame().setVisible(true);

        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        String input = "fanculo";
        System.out.println(input);
        int firstRow = 10;
        int secondRow = 11;
        int colIndex  = 5;
        while ((input = reader.readLine()).length() != 0) {
            System.out.println(input);


            char value = input.charAt(input.length() - 1);
            Font forFirstRowFont = new Font("Courier New", Font.BOLD, 20);
            Font forSecondRowFont = new Font("Arial", Font.BOLD, 20);
            /*
            window.changeFontAndColor(firstRow, colIndex, forSecondRowFont, Color.RED);
            window.changeContent(firstRow, colIndex, value);
            //window.getTable().repaint();
            */

            window.setColor(Color.BLUE);
            window.setMyFont(new Font("Courier New", Font.BOLD, 20));

            window.setRow(secondRow);
            window.setColumn(colIndex);
            window.getTable().setValueAt(new Character('C'), secondRow, colIndex);
            window.getTable().repaint();

            window.setColor(Color.RED);
            window.setMyFont(new Font("Courier New", Font.BOLD, 20));
            window.setRow(secondRow + 1);
            window.setColumn(colIndex);
            window.getTable().setValueAt(new Character('C'), secondRow + 1, colIndex);
            window.getTable().repaint();




            //window.changeFontAndColor(secondRow, colIndex, forSecondRowFont, Color.BLUE);
            //window.changeContent(secondRow, colIndex, value);
            //window.getTable().repaint();

            //window.getTable().repaint();
            //window.changeFontAndColor(secondRow + 1, colIndex, forSecondRowFont, Color.GREEN);
            //window.changeContent(secondRow + 1, colIndex, value);

            //window.changeFontAndColor(secondRow + 2, colIndex, forSecondRowFont, Color.RED);
            //window.changeContent(secondRow + 2, colIndex, value);

            //window.changeFontAndColor(secondRow + 3, colIndex, forSecondRowFont, Color.MAGENTA);
            //window.changeContent(secondRow + 3, colIndex, value);

            colIndex ++;
        }


    }

    private void init() {
        this.array2DForCellAttributes = new Array2DForCellAttributes(40, 24);
        this.array2DForCellAttributes.fillMatrix();
        this.frame = new JFrame("TRZ POC");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Vector<Character> row = new Vector<Character>();
        ;
        for (int k = 0; k < 40; k++) {
            row.add(new Character(' '));
        }
        Vector<Vector> data = new Vector<Vector>();
        for (int i = 0; i < 24; i++) {
            data.add((Vector) row.clone());
        }


        //this.tableModel = new StringTableModel();

        this.table = new JTable(data, row);

        Font defaultFont = new Font("Courier New", Font.BOLD, 20);
        VariableRowHeightRenderer r = new VariableRowHeightRenderer(defaultFont, Color.BLUE, new Dimension(this.width, this.height));
        /*
        for (int i = 0; i < this.table.getColumnCount(); i ++){
            this.table.getColumnModel().getColumn(i).setCellRenderer(r);
        }
        */

        this.table.setDefaultRenderer(Character.class, r);


        table.setTableHeader(null);
        table.setShowGrid(false);
        //table.setEnabled(false);
        table.setRowHeight(this.height);

        JScrollPane scrollPane = new JScrollPane(table);
        this.frame.add(scrollPane, BorderLayout.CENTER);
        this.frame.setSize(800, 480);

        //frame.getContentPane().add(new JScrollPane(table));
        //frame.pack();


    }

    public JFrame getDemoFrame() {
        return frame;
    }

    public void changeContent(int row, int column, char value) {
        this.table.setValueAt(value, row, column);
        DefaultTableModel model = (DefaultTableModel) this.table.getModel();
        model.fireTableCellUpdated(row, column);

    }

    public void changeFontAndColor(int row, int column, Font font, Color color) {
        this.setRow(row);
        this.setColumn(column);
        this.setColor(color);
        this.setMyFont(font);
        this.table.revalidate();
        this.table.repaint();

/*
        int size = font.getSize();
        VariableRowHeightRenderer renderer = (VariableRowHeightRenderer) this.table.getCellRenderer(row, column);
        renderer.setColor(color);
        renderer.setFont(font);
        renderer.getTableCellRendererComponent(this.table, 'R', false, false, row, column);
*/


        //TableColumn col = this.table.getColumnModel().getColumn(column);
        //col.setCellRenderer(new VariableRowHeightRenderer(font, Color.RED, new Dimension(size, size)));
    }

    private class VariableRowHeightRenderer /*extends JLabel*/ extends DefaultTableCellRenderer {

        private Font font;
        private Color color;
        private Dimension dimension;

        public VariableRowHeightRenderer(Font font, Color color, Dimension dimension) {
            super();
            this.setOpaque(true);
            this.setHorizontalAlignment(JLabel.CENTER);
            this.font = font;
            this.color = color;
            this.dimension = dimension;
        }


        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {


            Component retValue = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            Color actual = getArray2DForCellAttributes().get(column, row).getColor();
            Font actualFont = getArray2DForCellAttributes().get(column, row).getFont();
            //Color actual = this.getForeground();
            //Font actualFont = this.getFont();

            this.setPreferredSize(this.dimension);
            table.setRowHeight(row, dimension.height);

            if (row == getRow() && column == getColumn()) {
                CellAttributes cellAttributes = getArray2DForCellAttributes().get(column, row);
                cellAttributes.setColor(getColor());
                cellAttributes.setFont(getMyFont());
                this.setForeground(getColor());
                this.setFont(getMyFont());
                Dimension fontDimension = this.calculateFontDimensions(getMyFont(), value);
                table.setRowHeight(row, fontDimension.height);
                getArray2DForCellAttributes().set(cellAttributes, column, row);

                setRow(-1);
                setColumn(-1);
            } else {
                this.setForeground(actual);
                this.setFont(actualFont);
            }
            setBackground(getBackground());



            // Esperimenti con le righe
            //table.setRowHeight(row, 50);
            //table.setRowHeight(1, 100);

            //Graphics g = this.getComponentGraphics();
            //FontMetrics fm = g.getFontMetrics(font);


            //this.setFont(this.font);
            Character c = (Character) value;
            this.setText(String.valueOf(c.charValue()));


            //table.setRowHeight(row, getPreferredSize().height + row * 10);
            return retValue;
            //return this;
        }

        private Dimension calculateFontDimensions(Font myFont, Object value) {
            Character c = (Character) value;
            FontMetrics fontMetrics = this.getFontMetrics(myFont);
            int hgt = fontMetrics.getHeight();
            int width = fontMetrics.stringWidth(String.valueOf(c.charValue()));
            return new Dimension(hgt + 2, width + 2);
        }
    }
}

