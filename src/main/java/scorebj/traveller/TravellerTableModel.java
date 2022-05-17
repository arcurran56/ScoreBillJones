package scorebj.traveller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scorebj.model.ScoreLine;
import scorebj.model.Traveller;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TravellerTableModel extends AbstractTableModel {
    private static Logger logger = LogManager.getLogger();

    private final String INTEGER = "java.lang.Integer";
    private final String CONTRACT = "scorebj.traveller.Contract";
    //private final String STRING = "java.lang.String";
    private final String DIRECTION = "scorebj.model.ScoreLine.Direction";

    private final String[] travellerColumnHeaders = new String[]
            {"NS Pair",
                    "EW Pair",
                    "Contract",
                    "By",
                    "Tricks",
                    "NS Score",
                    "EW Score",
                    "NS MP",
                    "EW MP"};

    private final boolean[] editableColumns =
            {true, true, true, true, true, false, false, false, false};

    private final String[] columnClassNames = new String[]
            {INTEGER,
                    INTEGER,
                    CONTRACT,
                    DIRECTION,
                    INTEGER,
                    INTEGER,
                    INTEGER,
                    INTEGER,
                    INTEGER};


    private int noPairs;
    private List<ScoreLine> travellerTable;

    public TravellerTableModel(int noPairs){
        this.noPairs = noPairs;
        travellerTable = new ArrayList<ScoreLine>(noPairs/2);

        for (int i=0; i<noPairs/2; i++) {
            travellerTable.add(new ScoreLine());
        }
    }

    @Override
    public int getRowCount() {
        return noPairs/2;
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
        Object convertedValue = null;
        try {
            switch (columnClassNames[columnIndex]){
                case INTEGER:
                    convertedValue = Integer.valueOf((String) aValue);
                    break;
/*                case STRING:
                    convertedValue = aValue;
                    break;*/
                case CONTRACT:
                    convertedValue = new Contract((String) aValue);
                    break;
                case DIRECTION:
                    convertedValue = ScoreLine.Direction.valueOf((String) aValue);
            }
        } catch (Exception e) {
        }

        ScoreLine scoreLine = travellerTable.get(rowIndex);
        if(convertedValue != (Object) scoreLine.get(columnIndex)) {
            (travellerTable.get(rowIndex)).set(columnIndex, convertedValue);
            fireTableCellUpdated(rowIndex,columnIndex);
        }
    }

    @Override
    public int getColumnCount() {
        return travellerColumnHeaders.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return travellerTable.get(rowIndex).get(columnIndex);
    }

    public void setTraveller(Traveller traveller) {
        //ScoreLine scoreLine;
        travellerTable.clear();
        travellerTable.addAll(traveller.getScoreLines());

        StringBuilder logLine = new StringBuilder()
                .append("Traveller size: ")
                .append(travellerTable.size());
        logger.debug(logLine);
    }

    public Traveller getTraveller(){
        Traveller traveller = new Traveller();
        traveller.addAll(travellerTable);
        return traveller;
    }

    public void setNoPairs(int noPairs) {
        this.noPairs = noPairs;
    }
}
