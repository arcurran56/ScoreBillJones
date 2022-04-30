package scorebj.traveller;

import scorebj.model.ScoreLine;
import scorebj.model.Traveller;

import javax.swing.table.AbstractTableModel;

public class TravellerTableModel extends AbstractTableModel {

    private final String INTEGER = "java.lang.Integer";
    private final String CONTRACT = "scorebj.traveller.Contract";
    private final String STRING = "java.lang.String";
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



    private Object[][] travellerTable = new Object[5][9];


    @Override
    public int getRowCount() {
        return 5;
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
                case STRING:
                    convertedValue = aValue;
                    break;
                case CONTRACT:
                    convertedValue = new Contract((String) aValue);
                    break;
                case DIRECTION:
                    convertedValue = ScoreLine.Direction.valueOf((String) aValue);
            }
        } catch (Exception e) {
        }
        if(convertedValue != travellerTable[rowIndex][columnIndex]) {
            travellerTable[rowIndex][columnIndex] = convertedValue;
            fireTableCellUpdated(rowIndex,columnIndex);
        }
    }

    @Override
    public int getColumnCount() {
        return 9;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return travellerTable[rowIndex][columnIndex];
    }

    public void setTraveller(Traveller traveller){
        ScoreLine scoreLine;
        for (int lineNo = 0; lineNo<traveller.getScoreLines().length; lineNo++) {
            scoreLine = traveller.getScoreLines()[lineNo];
                travellerTable[lineNo][0] = scoreLine.getNsPair();
                travellerTable[lineNo][1] = scoreLine.getEwPair();
                travellerTable[lineNo][2] = scoreLine.getContract();
                travellerTable[lineNo][3] = scoreLine.getPlayedBy();
                travellerTable[lineNo][4] = scoreLine.getTricks();
                travellerTable[lineNo][5] = scoreLine.getNSScore();
                travellerTable[lineNo][5] = scoreLine.getEWScore();
                travellerTable[lineNo][7] = scoreLine.getNsMPs();
                travellerTable[lineNo][8] = scoreLine.getEwMPs();
            }
    }

    public Traveller getTraveller(){
        Traveller traveller = new Traveller();

        ScoreLine scoreLine;
        for (int lineNo = 0; lineNo<traveller.getScoreLines().length; lineNo++) {
            scoreLine = traveller.getScoreLine(lineNo);
            scoreLine.setNsPair((Integer) travellerTable[lineNo][0]);
            scoreLine.setEwPair((Integer) travellerTable[lineNo][1]);
            scoreLine.setContract((Contract) travellerTable[lineNo][2]);
            scoreLine.setPlayedBy((ScoreLine.Direction) travellerTable[lineNo][3]);
            scoreLine.setTricks((Integer) travellerTable[lineNo][4]);
            scoreLine.scoreHand((false));
            //scoreLine.setNsMPs((Integer) travellerTable[lineNo][7]);
            //scoreLine.setEwMPs((Integer) travellerTable[lineNo][8]);
        }
        return traveller;
    }
}
