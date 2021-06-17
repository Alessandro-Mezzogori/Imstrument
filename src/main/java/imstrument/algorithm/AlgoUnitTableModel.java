package imstrument.algorithm;

import imstrument.algorithm.operators.Operator;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;

public class AlgoUnitTableModel extends AbstractTableModel {
    static final String buttonName = "Delete";
    static final String operatorColumnName = "OPERATORS";
    static final String[] COLUMN_NAMES = new String[]{"RECTANGLE", operatorColumnName, "DELETE"};
    static final Class<?>[] COLUMN_TYPES = new Class<?>[] {String.class, String.class, JButton.class};

    final ArrayList<AlgorithmUnit> groups;
    final CustomAlgorithmCreator.AlgorithmCreationSynchronizer controller;
    public AlgoUnitTableModel(ArrayList<AlgorithmUnit> groups, CustomAlgorithmCreator.AlgorithmCreationSynchronizer controller){
        this.groups = groups;
        this.controller = controller;
    }

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
                return groups.get(rowIndex).operator == null ? "" : groups.get(rowIndex).operator.getName();
            case 2:
                final JButton deleteButton = new JButton(buttonName);
                deleteButton.addActionListener(e -> {
                    groups.remove(rowIndex);
                    controller.update();
                    AlgoUnitTableModel.this.fireTableDataChanged();
                });
                return deleteButton;
            default:
                return null;
        }
    }
}