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

    private final String[] COLUMN_HEADERS = new String[]
            {"NS Pair",
                    "EW Pair",
                    "Contract",
                    "By",
                    "Tricks",
                    "NS Score",
                    "EW Score",
                    "NS MP",
                    "EW MP",
                    "NS OR",
                    "EW OR"};

    private final boolean[] editableColumns =
            {true, true, true, true, true, false, false, false, false, true, true};

    private final String[] columnClassNames = new String[]
            {INTEGER,
                    INTEGER,
                    CONTRACT,
                    DIRECTION,
                    INTEGER,
                    INTEGER,
                    INTEGER,
                    INTEGER,
                    INTEGER,
                    INTEGER,
                    INTEGER};


    private final ArrayList<ScoreLine> travellerTable = new ArrayList<>(INITIAL_CAPACITY);
    private int rowCount = 0;
    private BoardId boardId = new BoardId();

    public void setAutoFillEnabled(boolean autoFillEnabled) {
        this.autoFillEnabled = autoFillEnabled;
    }

    private boolean autoFillEnabled = true;

    public boolean isComplete() {
        boolean complete = true;
        for (ScoreLine scoreLine : travellerTable) {
            complete = complete && scoreLine.isComplete();
        }
        return complete;
    }

    public boolean isEmpty() {
        boolean empty = true;
        for (ScoreLine scoreLine : travellerTable) {
            if (!scoreLine.isEmpty()) {
                empty = false;
            }
        }
        return empty;
    }

    public BoardId getBoardId() {
        return boardId.clone();
    }

    public void clear() {
        for (ScoreLine sl : travellerTable) {
            for (int i = 0; i < editableColumns.length; i++) {
                if (editableColumns[i]) {
                    sl.set(i, null);
                }
            }
        }

        TableModelEvent event = new TableModelEvent(this);
        fireTableChanged(event);
    }

    private static class Matchup {
        Integer nsPair;
        Integer ewPair;
    }

    private static class AutoFillCache {
        int set;
        ArrayList<Matchup> cache = new ArrayList<>(INITIAL_CAPACITY);

        int getSet() {
            return set;
        }

        void setSet(int s) {
            set = s;
        }

        void add(Matchup matchup) {
            cache.add(matchup);
        }

        Matchup get(int index) {
            return cache.get(index);
        }

        int size() {
            return cache.size();
        }

    }

    private AutoFillCache autoFillCache;

    private final PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("score".equals(evt.getPropertyName())) {
                allocateMPs();
                createAutoFillCache();
            }
            if ("blank".equals(evt.getPropertyName())) {
                for (ScoreLine sl : travellerTable) {
                    sl.setNsPair(null);
                    sl.setEwPair(null);
                    sl.setContract(null);
                    sl.setTricks(null);
                    sl.setPlayedBy(null);
                    sl.setNsMPs(null);
                    sl.setEwMPs(null);
                }
            }
        }
    };

    private void processAutofill() {

        if (autoFillCache != null) {
            if (boardId.getSet() == autoFillCache.getSet()) {
                if (isEmpty()) {
                    autofill();
                }
            } else {
                autoFillCache = null;
            }
        }
        if (isComplete() && autoFillEnabled) {
            createAutoFillCache();
        }
    }

    private void createAutoFillCache() {
        if (isComplete()) {
            autoFillCache = new AutoFillCache();
            autoFillCache.setSet(boardId.getSet());
            for (int i = 0; i < travellerTable.size(); i++) {
                Matchup matchup = new Matchup();
                matchup.nsPair = travellerTable.get(i).getNsPair();
                matchup.ewPair = travellerTable.get(i).getEwPair();
                autoFillCache.add(matchup);
            }
            logger.debug("New autofill cache created.");
        }
    }

    private void autofill() {
        if (autoFillCache != null) {
            if (autoFillCache.size() > 0
                    && isEmpty()
                    && autoFillEnabled) {
                logger.debug("...autofilling...");
                for (int i = 0; i < travellerTable.size(); i++) {
                    travellerTable.get(i).setNsPair(autoFillCache.get(i).nsPair);
                    travellerTable.get(i).setEwPair(autoFillCache.get(i).ewPair);
                }
            }
        }
    }

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
        return COLUMN_HEADERS[column];
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
                    convertedValue = ScoreLine.Direction.valueOf(((String) aValue).toUpperCase());
            }
        } catch (Exception e) {
            logger.warn(e.toString());
        }
        (travellerTable.get(rowIndex)).set(columnIndex, convertedValue);
        fireTableCellUpdated(rowIndex, columnIndex);

    }

    @Override
    public int getColumnCount() {
        return 11;
        //return COLUMN_HEADERS.length;
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

        for (ScoreLine sl : travellerTable) {

            if (sl != null) {
                sl.activate(this.propertyChangeListener);
                sl.recalculate();
            }
        }
        rowCount = travellerTable.size();

        StringBuilder logLine = new StringBuilder()
                .append("Displaying Traveller: ");

        if (boardId != null) {
            //traveller = new Traveller(0);
            logLine
                    .append(boardId);
        } else {
            logLine.append(" null board ");
        }
        logLine
                .append(" with ")
                .append(rowCount)
                .append(" rows.");
        logger.debug(logLine);

        processAutofill();

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

    private void allocateMPs(){
        allocateMPs_alg1();
    }
    private void allocateMPs_alg1() {
        int netScore1;
        int netScore2;

        logger.debug("Traveller completed:");

        //Check for averaging and apply as required.
        int skippedCount = 0;
        int lineCount = 0;

        Integer nsScore;
        Integer ewScore;

        for (ScoreLine scoreLine : travellerTable) {
            lineCount++;
            if (scoreLine.isSkipped()) {
                skippedCount++;
            }
        }

        //Compare scores to get MPs
        for (ScoreLine scoreLine1 : travellerTable) {
            int nsMPs = 0;
            int ewMPs = 0;
            if (scoreLine1 != null) {
                //Hand not played: apply average.
                if (scoreLine1.isSkipped()) {
                    nsMPs = lineCount - 1;
                    ewMPs = lineCount - 1;
                } else {
                    //Hand played but check not null.
                    if (scoreLine1.getNSScore() != null && scoreLine1.getEWScore() != null) {
                        netScore1 = scoreLine1.getNSScore() - scoreLine1.getEWScore();

                        for (ScoreLine scoreLine2 : travellerTable) {
                            if (scoreLine2 != null) {
                                if (scoreLine2.getNSScore() != null && scoreLine2.getEWScore() != null) {
                                    if (scoreLine1 != scoreLine2 && !scoreLine2.isSkipped()) {
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
                    }
                    nsMPs += skippedCount;
                    ewMPs += skippedCount;
                }

                //scoreLine1.setComplete(true);
            }
            scoreLine1.setNsMPs(nsMPs);
            scoreLine1.setEwMPs(ewMPs);
            //scoreLine1.setComplete(true);
            logger.debug(scoreLine1);
        }
        logger.debug("..done.");

    }


    public String toString() {
        return "Traveller for " + (boardId == null ? " null " : boardId.toString());
    }

    public String getCompletionStatus() {
        return isComplete() ? "Complete" : "Incomplete";
    }
}
