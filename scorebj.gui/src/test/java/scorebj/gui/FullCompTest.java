package scorebj.gui;

import org.junit.jupiter.api.Test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scorebj.model.*;
import scorebj.pairing.PairingTableModel;
import scorebj.traveller.TravellerTableModel;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.io.*;
import java.util.Arrays;

public class FullCompTest {
    private final static Logger logger = LogManager.getLogger();

    private final ScoringFormActions actions = new ScoringFormActions();
    private final ScoringBean scoringBean = new ScoringBean();
    private final TravellerTableModel travellerTableModel = new TravellerTableModel();
    private final PairingTableModel pairingTableModel = new PairingTableModel();
    private final DefaultComboBoxModel<String> defaultComboBoxModel = new DefaultComboBoxModel<>();

    private class TableInserter {
        private int row = 0;
        private int column = 0;

        TableInserter() {
        }

        void insert(String value) {
            StringBuilder logMessage = new StringBuilder("Inserting ");
            logMessage.append(value)
                    .append(" at ")
                    .append(row)
                    .append(", ")
                    .append(column);
            logger.debug(logMessage);
            travellerTableModel.setValueAt(value, row, column);
            column++;
        }

        public void nextRow() {
            logger.debug("...next row...");
            column = 0;
            row++;
        }
    }

    enum ReadingState {LINE_COUNT, SCORE_LINE}

    private final static int NS_PAIR = 1;
    private final static int EW_PAIR = 2;
    private final static int CONTRACT = 12;
    private final static int PLAYED_BY = 13;
    private final static int TRICKS = 14;

    public FullCompTest() {
    }

    @Test
    public void runTest() throws IOException, DataStoreException {

        String[] args = {"IntTestComp1", "5", "16", "10"};
        String[] pairings = {"David & Salette",
                "Liz B & Jane E",
                "Jill R & Rob",
                "Jill H & Jane D",
                "Wendy & Diana",
                "Diane & Alan",
                "Julia & Sue",
                "Clare & Jill A",
                "Lisbeth & Vicky",
                "Caroline & Andrew"
        };

        String competitionName = args[0];
        int noSets = Integer.parseUnsignedInt(args[1]);
        int noBoardsPerSet = Integer.parseUnsignedInt(args[2]);
        int noPairs = Integer.parseUnsignedInt(args[3]);

        DataStore.setTestMode(true);

        //Set up gui interface
        Traveller traveller = new Traveller(new BoardId(5, 16), (noPairs + 1) / 2);

        travellerTableModel.setTraveller(traveller);
        travellerTableModel.addTableModelListener(new TableModelListener() {
                                                      @Override
                                                      public void tableChanged(TableModelEvent e) {
                                                          logger.debug("Table event type, "
                                                                  + e.getType() + " (" + e.getFirstRow()
                                                                  + ", " +e.getColumn()
                                                                  + ") fired by " + e.getSource());
                                                          int firstRow = e.getFirstRow();
                                                          int column = e.getColumn();

                                                          actions.travellerTableChangedAction(scoringBean, travellerTableModel, firstRow, column);

                                                      }
                                                  }
        );

        actions.init(scoringBean, travellerTableModel, pairingTableModel, defaultComboBoxModel);

        //Add new Competition
        scoringBean.setNewCompetitionName(competitionName);
        scoringBean.setNewSets(args[1]);
        scoringBean.setNewBoardsPerSet(args[2]);
        scoringBean.setNewNoPairs(args[3]);
        actions.addCompActionPerformed(scoringBean);

        pairingTableModel.setNoPairs(noPairs);
        pairingTableModel.setPairings(Arrays.asList(pairings));
        TableModelEvent event = new TableModelEvent(pairingTableModel);
        actions.pairingTableChangedAction(event);

        StringBuilder builder = (new StringBuilder())
                .append(competitionName)
                .append(", ")
                .append(noSets)
                .append(", ")
                .append(noBoardsPerSet)
                .append(", ")
                .append(noPairs);
        logger.info(builder);

        InputStream inputStream = FullCompTest.class.getClassLoader().getResourceAsStream("FullCompTest.csv");
        assert inputStream != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String importLine;
        int lineIndex = 0;
        int lineCount = 0;
        int travellerIndex = 0;

        importLine = reader.readLine();
        ReadingState readingState = ReadingState.LINE_COUNT;

        //Complete travellers.
        String[] importLineValues;
        String splitLine;
        TableInserter inserter = new TableInserter();
        logger.info("Reading file...");
        int tno = 0;
        while (importLine != null) {
            logger.debug("Read: " + importLine);
            switch (readingState) {
                case LINE_COUNT:
                    //Complete traveller.
                    tno++;
                    logger.debug("Traveller #" + Integer.toString(tno) + "...");
                    inserter = new TableInserter();
                    importLineValues = importLine.split(",");
                    splitLine = "";
                    for (String s : importLineValues) {
                        splitLine = splitLine + ":" + s + ":";
                    }
                    logger.debug(splitLine);
                    lineCount = Integer.parseInt(importLineValues[0]);
                    lineIndex = 0;
                    readingState = ReadingState.SCORE_LINE;
                    break;

                case SCORE_LINE:
                    importLineValues = importLine.split(",");
                    logger.info("Line: " + lineIndex);
                    logger.info("Values: " + importLineValues);

                    inserter.insert(importLineValues[NS_PAIR]);
                    inserter.insert(importLineValues[EW_PAIR]);
                    inserter.insert(importLineValues[CONTRACT]);
                    inserter.insert(importLineValues[PLAYED_BY]);
                    inserter.insert(importLineValues[TRICKS]);
                    inserter.nextRow();

                    lineIndex++;
                    if (lineIndex == lineCount) {
                        readingState = ReadingState.LINE_COUNT;

                        //Next board...
                        logger.info("Next board");
                        actions.forwardButtonActionPerformed(scoringBean);
                    }
                    break;
            }
            importLine = reader.readLine();
        }
        logger.info("...outputting results...");
        actions.saveButtonActionPerformed();

        logger.info("...complete.");
        reader.close();
    }


}
