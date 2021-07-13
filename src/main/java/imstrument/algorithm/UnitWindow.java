package imstrument.algorithm;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class UnitWindow extends JPanel {
    /**
     * Table model for the Jtable
     */
    final AlgoUnitTableModel unitsTableModel;
    /**
     * Jtable showing the algorithm units of the algorithm in creation
     */
    final JTable unitsTable;
    /**
     * Reference to the algorithms units that will be modified
     */
    final ArrayList<AlgorithmUnit> groups;
    /**
     * wrapper for the units table so it can handle "any" amount of units
     */
    JScrollPane unitsTableWrapper;

    /**
     * Operators selector for the selected unit
     */
    final JPopupMenu operatorSelector;
    /**
     * Synchronizer wirth the Algorithm canvas
     */
    final CustomAlgorithmCreator.AlgorithmCreationSynchronizer controller;

    public UnitWindow(ArrayList<AlgorithmUnit> groups, CustomAlgorithmCreator.AlgorithmCreationSynchronizer controller){
        this.groups = groups;
        this.controller = controller;
        unitsTableModel = new AlgoUnitTableModel(this.groups, this.controller);
        unitsTable = new JTable(unitsTableModel);

        /* create the operator select menu */
        operatorSelector = new JPopupMenu();

        // create the operator list from which we will need to chose
        ListModel<String> operatorsListModel = new AbstractListModel<>() {
            @Override
            public int getSize() { return Algorithm.operatorTable.size(); }
            @Override
            public String getElementAt(int index) { return Algorithm.operatorTable.keySet().stream().skip(index).iterator().next(); }
        };
        final JList<String> operatorsList = new JList<>(operatorsListModel);
        // only one operator per group is allowed
        operatorsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // save the selected operator to the selected rows
        JButton saveSelection = new JButton("Save");
        saveSelection.addActionListener(e -> {
            for(int row : unitsTable.getSelectedRows())
                groups.get(row).operator = Algorithm.operatorTable.get(operatorsList.getSelectedValue());

            /* clean up */
            resizeColumnWidth(unitsTable);
            unitsTableModel.fireTableCellUpdated(unitsTable.getSelectedRow(), unitsTable.getSelectedColumn());
        });

        // close the operators selection popup menu
        JButton closeSelection = new JButton("Close"); // close the operatorsSelector popup menu
        closeSelection.addActionListener(e -> {
            operatorSelector.setVisible(false);
            operatorsList.clearSelection();
        });
        // build the operators selection menu
        JPanel operatorSelectorButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        operatorSelectorButtonPanel.add(saveSelection);
        operatorSelectorButtonPanel.add(closeSelection);
        operatorSelector.add(operatorSelectorButtonPanel);
        operatorSelector.add(operatorsList);

        /* create the table with the data */
        unitsTable.getColumn("DELETE").setCellRenderer(new JTableButtonRenderer());
        unitsTable.addMouseListener(new JTableButtonMouseListener(unitsTable, operatorSelector));
        unitsTableWrapper = new JScrollPane(unitsTable);

        add(unitsTableWrapper, BorderLayout.CENTER);
    }

    /**
     * Notifies the table model that the data changed
     */
    void updateTable(){
        unitsTableModel.fireTableDataChanged();
        // resize to fit new data
        resizeColumnWidth(unitsTable);
        repaint();
    }

    /**
     * Resizes the column to fit the data that is going to be shown in the table
     * @param table to fit the columns
     */
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

    /**
     * Mouse listener for the units displaying Jtable
     */
    private static class JTableButtonMouseListener extends MouseAdapter {
        private final JTable table;
        private final JPopupMenu operatorSelector;

        public JTableButtonMouseListener(JTable table, JPopupMenu operatorSelector) {
            this.table = table;
            this.operatorSelector = operatorSelector;
        }

        public void mouseClicked(MouseEvent e) {
            int column = table.getColumnModel().getColumnIndexAtX(e.getX()); // get the coloum of the button
            int row    = e.getY()/table.getRowHeight(); //get the row of the button

            /*Checking the row or column is valid or not*/
            if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
                Object value = table.getValueAt(row, column);
                // if the clicked value is a button then emulate a click
                if (value instanceof JButton) {
                    /*perform a click event*/
                    ((JButton)value).doClick();
                }

                // if the clicked value is the operator column then show the operators selector popup
                if(table.getColumnName(column).equals(AlgoUnitTableModel.operatorColumnName)){
                    operatorSelector.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
    }

    /**
     * Rendered to have the delete button inside the JTable
     */
    private static class JTableButtonRenderer implements TableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return (JButton)value;
        }
    }

}
