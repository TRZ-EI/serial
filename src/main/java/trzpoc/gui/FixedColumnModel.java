package trzpoc.gui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class FixedColumnModel {

    public static void main(String args[]) {

        final Object rowData[][] = { { "1", "one", "I" }, { "2", "two", "II" }, { "3", "three", "III" } };

        final String columnNames[] = { "#", "E", "R" };

        final TableModel fixedColumnModel = new AbstractTableModel() {
            public int getColumnCount() {
                return 1;
            }

            public String getColumnName(int column) {
                return columnNames[column];
            }

            public int getRowCount() {
                return rowData.length;
            }

            public Object getValueAt(int row, int column) {
                return rowData[row][column];
            }
        };

        final TableModel mainModel = new AbstractTableModel() {
            public int getColumnCount() {
                return columnNames.length - 1;
            }

            public String getColumnName(int column) {
                return columnNames[column + 1];
            }

            public int getRowCount() {
                return rowData.length;
            }

            public Object getValueAt(int row, int column) {
                return rowData[row][column + 1];
            }
        };

        JTable fixedTable = new JTable(fixedColumnModel);

        for (int i = 0; i < fixedTable.getColumnModel().getColumnCount(); i++) {
            fixedTable.getColumnModel().getColumn(i).setPreferredWidth(100);
        }

        fixedTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JTable mainTable = new JTable(mainModel);
        mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < mainTable.getColumnModel().getColumnCount(); i++) {
            mainTable.getColumnModel().getColumn(i).setPreferredWidth(200);
        }

        ListSelectionModel model = fixedTable.getSelectionModel();
        mainTable.setSelectionModel(model);

        JScrollPane scrollPane = new JScrollPane(mainTable);
        Dimension fixedSize = fixedTable.getPreferredSize();
        JViewport viewport = new JViewport();
        viewport.setView(fixedTable);
        viewport.setPreferredSize(fixedSize);
        viewport.setMaximumSize(fixedSize);
        scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, fixedTable.getTableHeader());
        scrollPane.setRowHeaderView(viewport);

        JFrame frame = new JFrame("Fixed Column Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setSize(300, 150);
        frame.setVisible(true);
        mainTable.setValueAt("esterina",0, 1);
        mainTable.repaint();

    }
}

