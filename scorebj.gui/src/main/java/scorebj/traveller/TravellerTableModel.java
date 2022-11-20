package scorebj.traveller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scorebj.model.BoardId;
import scorebj.model.Contract;
import scorebj.model.ScoreLine;
import scorebj.model.Traveller;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class TravellerTableModel extends AbstractTableModel {
    private static final int INITIAL_CAPACITY = 10;
    private static final Logger logger = LogManager.getLogger();

    private final String INTEGER = "java.lang.Integer";
    private final String CONTRACT = "scorebj.model.Contract";
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


    private int noPairs = 0;
    private final List<ScoreLine> travellerTable = new ArrayList<>(INITIAL_CAPACITY);
    private int rowCount = 0;
    private BoardId boardId;

    private final PropertyChangeListener propertyChangeListener = new PropertyChangeListener(){

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("score".equals(evt.getPropertyName())){
                allocateMPs();
            }
        }
    };

    public TravellerTableModel() {
        addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                logger.debug("TableChanged event.");
                int column = e.getColumn();
                int row = e.getFirstRow();
            }
        });
    }

    @Override
    public int getRowCount() {
        return rowCount;
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
            switch (columnClassNames[columnIndex]) {
                case INTEGER:
                    convertedValue = Integer.valueOf((String) aValue);
                    break;
                case CONTRACT:
                    convertedValue = new Contract((String) aValue);
                    break;
                case DIRECTION:
                    convertedValue = ScoreLine.Direction.valueOf((String) aValue);
            }
        } catch (Exception e) {
            logger.warn(e.toString());
        }
        (travellerTable.get(rowIndex)).set(columnIndex, convertedValue);
        fireTableCellUpdated(rowIndex, columnIndex);

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
        boardId = traveller.getBoardId();
        travellerTable.clear();
        travellerTable.addAll(traveller.getScoreLines());
        rowCount = travellerTable.size();

        StringBuilder logLine = new StringBuilder()
                .append("Displaying Traveller: ");

        if (boardId != null) {
            //traveller = new Traveller(0);
            logLine
                    .append("Set ")
                    .append(boardId.getSet())
                    .append(", Board ")
                    .append(boardId.getBoard());
        } else {
            logLine.append(" null board ");
        }
        logLine
                .append(" with ")
                .append(rowCount)
                .append(" rows.");
        logger.debug(logLine);

        TableModelEvent e = new TableModelEvent(this);
        fireTableChanged(e);
    }

    public Traveller getTraveller() {
        Traveller traveller = new Traveller(this.boardId, rowCount);
        List<ScoreLine> lines = traveller.getScoreLines();

        for (int i = 0; i < rowCount; i++) {
            lines.set(i, travellerTable.get(i));
        }
        return traveller;
    }
    private void allocateMPs() {
        int netScore1;
        int netScore2;


        for (ScoreLine scoreLine1 : travellerTable) {
            int nsMPs = 0;
            int ewMPs = 0;
            if (scoreLine1 != null) {
                if (scoreLine1.getNSScore() != null && scoreLine1.getEWScore() != null) {
                    netScore1 = scoreLine1.getNSScore() - scoreLine1.getEWScore();

                    for (ScoreLine scoreLine2 : travellerTable) {
                        if (scoreLine2 != null) {
                            if (scoreLine2.getNSScore() != null && scoreLine2.getEWScore() != null) {
                                if (scoreLine1 != scoreLine2) {
                                    netScore2 = scoreLine2.getNSScore() - scoreLine2.getEWScore();
                                    switch (Integer.compare(netScore1, netScore2)) {
                                        case -1:
                                            ewMPs = ewMPs + 2;
                                            break;
                                        case 0:
                                            nsMPs++;
                                            ewMPs++;
                                            break;
                                        case 1:
                                            nsMPs = nsMPs + 2;
                                            break;

                                    }
                                }
                            }
                        }

                    }
                    scoreLine1.setNsMPs(nsMPs);
                    scoreLine1.setEwMPs(ewMPs);
                }

            }

        }

    }
    public String toString() {
        return "Traveller for " + (boardId==null?" null ":boardId.toString());
    }
}
