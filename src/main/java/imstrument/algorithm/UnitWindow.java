package imstrument.algorithm;

import imstrument.algorithm.operators.Operator;
import org.lwjgl.system.CallbackI;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class UnitWindow extends JPanel {
    AlgoUnitTableModel unitsTableModel;
    JTable unitsTable;
    ArrayList<AlgorithmUnit> groups;
    CustomAlgorithmCreator.CreatorController controller;
    JScrollPane unitsTableWrapper;

    public UnitWindow(ArrayList<AlgorithmUnit> groups, CustomAlgorithmCreator.CreatorController controller){
        this.controller = controller;
        this.groups = groups;

        unitsTableModel = new AlgoUnitTableModel();
        unitsTable = new JTable(unitsTableModel);
        unitsTable.getColumn("DELETE").setCellRenderer(new JTableButtonRenderer());
        unitsTable.addMouseListener(new JTableButtonMouseListener(unitsTable));
        unitsTableWrapper = new JScrollPane(unitsTable);

        add(unitsTableWrapper, BorderLayout.CENTER);
    }

    void updateList(){
        unitsTableModel.fireTableDataChanged();
        resizeColumnWidth(unitsTable);
        repaint();
    }

    public void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            if(width > 300)
                width=300;
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    class AlgoUnitTableModel extends AbstractTableModel  {
        private final String[] COLUMN_NAMES = new String[]{"RECTANGLE", "OPERATORS", "DELETE"};
        private final Class<?>[] COLUMN_TYPES = new Class<?>[] {String.class, String.class, JButton.class};

        @Override
        public int getRowCount() {
            return groups.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_NAMES.length;
        }

        @Override public String getColumnName(int columnIndex) {
            return COLUMN_NAMES[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return COLUMN_TYPES[columnIndex];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex){
                case 0:
                    return Arrays.toString(groups.get(rowIndex).rect);
                case 1:
                    StringBuilder stringBuilder = new StringBuilder();
                    for(Operator operator : groups.get(rowIndex).operators) {
                        stringBuilder.append(operator).append(" ");
                    }
                    return stringBuilder.toString();
                case 2:
                    final JButton deleteButton = new JButton("Delete");
                    deleteButton.addActionListener(e -> {
                        groups.remove(rowIndex);
                        controller.update();
                        unitsTableModel.fireTableDataChanged();
                    });
                    return deleteButton;
                default:
                    return null;
            }
        }
    }

    private static class JTableButtonMouseListener extends MouseAdapter {
        private final JTable table;

        public JTableButtonMouseListener(JTable table) {
            this.table = table;
        }

        public void mouseClicked(MouseEvent e) {
            int column = table.getColumnModel().getColumnIndexAtX(e.getX()); // get the coloum of the button
            int row    = e.getY()/table.getRowHeight(); //get the row of the button

            /*Checking the row or column is valid or not*/
            if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
                Object value = table.getValueAt(row, column);
                if (value instanceof JButton) {
                    /*perform a click event*/
                    ((JButton)value).doClick();
                }
            }
        }
    }

    private static class JTableButtonRenderer implements TableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton button = (JButton)value;
            return button;
        }
    }

}
