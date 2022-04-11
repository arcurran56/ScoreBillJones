package scorebj.traveller;

import scorebj.traveller.Contract;

import javax.swing.table.AbstractTableModel;

public class TravellerTableModel extends AbstractTableModel {
    private final String[]     travellerColumnHeaders = new String[]
            {"NS Pair",
                    "EW Pair",
                    "Contract",
                    "By",
                    "Tricks",
                    "NS Score",
                    "EW Score",
                    "NS MP",
                    "EW MP"};

    private Object[][] travellerTable = new Object[10][9];

    private final String INTEGER = "java.lang.Integer";
    private final String CONTRACT = "scorebj.traveller.Contract";
    private final String STRING = "java.lang.String";

    private final String[] columnClassNames = new String[]
        {INTEGER,
            INTEGER,
            CONTRACT,
            STRING,
                INTEGER,
            INTEGER,
            INTEGER,
            INTEGER,
            INTEGER};

    private final boolean[] editableColumns =
            {true, true, true, true, true, false, false, false, false};

    @Override
    public int getRowCount() {
        return 10;
    }

    @Override
    public String getColumnName(int column) {
        return travellerColumnHeaders[column];
    }

    @Override
    public int findColumn(String columnName) {
        return super.findColumn(columnName);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        String classString = columnClassNames[columnIndex];
        Class<?> columnClass = null;
        try {
            columnClass = Class.forName(classString);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return columnClass;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return editableColumns[columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        travellerTable[rowIndex][columnIndex] = aValue;

    }

    @Override
    public int getColumnCount() {
        return 9;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return travellerTable[rowIndex][columnIndex];
    }
}
