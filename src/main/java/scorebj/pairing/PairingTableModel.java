package scorebj.pairing;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PairingTableModel extends AbstractTableModel {
    List<String> pairings;
    private int noPairs;

    private final String[] COLUMN_NAMES = {"Pair #", "Names"};

    private final String[] COLUMN_CLASSES = {"java.lang.Integer", "java.lang.String"};

    @Override
    public Class<?> getColumnClass(int columnIndex) {
            String classString = COLUMN_CLASSES[columnIndex];
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
        return EDITABLE_COLUMNS[columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            pairings.set(rowIndex, (String) aValue);
        }
        fireTableCellUpdated(rowIndex,columnIndex);
    }

    private final boolean[] EDITABLE_COLUMNS = {false,true};

    public PairingTableModel() {
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    public int getNoPairs() {
        return noPairs;
    }

    public void setNoPairs(int noPairs) {
        this.noPairs = noPairs;
        pairings = new ArrayList<>(noPairs);
        for (int i = 0; i<noPairs; i++) {
            pairings.add("");
        }
    }

    public void setPairings(List<String> pairings) {
        for (int i=0; i<noPairs; i++){
            if (i< pairings.size()) {
                this.pairings.set(i, pairings.get(i));
            }
            else { this.pairings.set(i, "");}
        }
    }

    @Override
    public int getRowCount() {
        return noPairs;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0: return rowIndex+1;
            case 1: return pairings.get(rowIndex);

        }
        return null;
    }

    public List<String> getPairings() {
        return pairings;
    }
}
