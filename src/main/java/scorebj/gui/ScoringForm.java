package scorebj.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scorebj.model.*;
import scorebj.pairing.PairingTableModel;
import scorebj.traveller.TravellerTableColumnModel;
import scorebj.traveller.TravellerTableModel;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;
import java.util.function.Function;

public class ScoringForm {
    private static final Logger logger = LogManager.getLogger();

    private final JFrame frame = new JFrame("Scoring Bill Jones");

    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JPanel infoPanel;
    private JTable scoreTable;
    private JLabel setLabel;
    private JTextField setField;
    private JLabel boardLabel;
    private JTextField boardField;
    private JButton backButton;
    private JButton forwardButton;
    private JButton saveButton;
    private JScrollPane tableScrollPane;
    private JPanel tablePanel;
    private JButton goButton;
    private JPanel compPanel;
    private JTextField newCompField;
    private JComboBox<String> compComboBox;
    private JButton addComp;
    private JButton deleteComp;
    private JTextField newSetsField;
    private JTextField newBpSField;
    private JTextField currentSetsField;
    private JTextField currentBpSField;
    private JPanel pairPanel;
    private JTable pairingTable;
    private JScrollPane pairingScrollPane;
    private JTextField currentNoPairsField;
    private JLabel currentNoPairsLabel;
    private JTextField newNoPairsField;
    private JLabel newNoPairsLabel;
    private JButton clearButton;
    private JTextField completionStatusField;
    private JTextField progressField;

    private static DataStore dataStore;
    private final ScoringBean scoringBean;
    private final TravellerTableModel travellerTableModel = new TravellerTableModel();
    private Competition competition;
    private BoardId currentBoardId;
    private Vector<String> nameList;
    private final PairingTableModel pairingTableModel = new PairingTableModel();
    private TableColumnModel pairingTableColumnModel;


    public ScoringForm() {

        $$$setupUI$$$();

        scoringBean = new ScoringBean();
        competition = dataStore.getCompetition(0);

        int noSets = 0;
        int noPairs = 0;
        int noBoardsPerSet = 0;
        String competitionName = null;
        List<String> pairings = null;

        if (competition != null) {

            noSets = competition.getNoSets();
            noPairs = competition.getNoPairs();
            noBoardsPerSet = competition.getNoBoardsPerSet();
            competitionName = competition.getCompetitionName();

            BoardId boardId = new BoardId(noSets, noBoardsPerSet);

            Traveller traveller = competition.getTraveller(boardId);
            //travellerTableModel.setNoPairs(noPairs);
            travellerTableModel.setTraveller(traveller);

            pairings = competition.getPairings();
            pairingTableModel.setNoPairs(noPairs);
            pairingTableModel.setPairings(pairings);

            scoringBean.setCurrentSets(Integer.toString(noSets));
            scoringBean.setCurrentBoardsPerSet(Integer.toString(noBoardsPerSet));
            scoringBean.setCurrentNoPairs(Integer.toString(noPairs));

            scoringBean.setNewSet(Integer.toString(boardId.getSet()));
            scoringBean.setNewBoard(Integer.toString(boardId.getBoard()));

            String completionStatus = traveller.getCompetionStatus();
            scoringBean.setTravellerComplete(completionStatus);
            scoringBean.setProgress(competition.getProgress());


        }
        setData(scoringBean);

        assert pairings != null;
        StringBuilder logLine = new StringBuilder()
                .append("Initialisation:  Selected Competition:")
                .append(competitionName)
                .append(", pairs: ")
                .append(noPairs)
                .append("(")
                .append(pairings.size())
                .append(")");

        logger.debug(logLine);
        mainPanel.repaint();

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("back");
                navigateTraveller(BoardId::prev);

                mainPanel.repaint();
            }

        });
        forwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("fwd");
                navigateTraveller(BoardId::next);

                mainPanel.repaint();
            }
        });
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("go");

                navigateTraveller((bd) -> bd.select(
                        Integer.parseUnsignedInt(scoringBean.getNewSet()),
                        Integer.parseUnsignedInt(scoringBean.getNewBoard())));


/*                //Save current Traveller.
                scoreTable.clearSelection();
                BoardId boardId = scoringBean.getBoardId();
                Traveller savedTraveller = competition.getTraveller(boardId);
                Traveller newTraveller = travellerTableModel.getTraveller();
                savedTraveller.copy(newTraveller);

                //Go to specified traveller.
                getData(scoringBean);

                //Update view.
                savedTraveller = competition.getTraveller(boardId);
                travellerTableModel.setTraveller(savedTraveller);
                setData(scoringBean);

                dataStore.persist(competition);*/
                mainPanel.repaint();
            }
        });
        travellerTableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                logger.debug("Traveller table changed: " + e.getSource());

                ScoringBean scoringBean = new ScoringBean();
                getData(scoringBean);
                TravellerTableModel travellerTableModel = (TravellerTableModel) e.getSource();
                Traveller newTraveller = travellerTableModel.getTraveller();
                newTraveller.scoreHand(e.getFirstRow(), true);
                BoardId currentBoardId = newTraveller.getBoardId();

 /*
                int noSets = competition.getNoSets();
                int noBoardsPerSet = competition.getNoBoardsPerSet();
                int noPairs = competition.getNoPairs();

                BoardId boardId1 = new BoardId(noSets, noBoardsPerSet);
                boardId1.setSet();
                scoringBean.setNewBoard();
*/
                Traveller savedTraveller = competition.getTraveller(currentBoardId);
                savedTraveller.copy(newTraveller);

                scoringBean.setTravellerComplete(newTraveller.getCompetionStatus());
                scoringBean.setProgress(competition.getProgress());

                setData(scoringBean);
                dataStore.persist(competition);

                mainPanel.repaint();
            }
        });
        pairingTableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                PairingTableModel pairingTableModel = (PairingTableModel) e.getSource();
                logger.debug("Pairing table changed from " + e.getSource());
                List<String> pairings = pairingTableModel.getPairings();
                competition.setPairings(pairings);

                dataStore.persist(competition);

                mainPanel.repaint();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("Save...");
                dataStore.persist(competition);
                try {
                    competition.saveResults();
                } catch (DataStoreException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        compComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("compComboBox: " + e.getActionCommand());

                //Get selected Competition and save key for later...
                String key = (String) compComboBox.getSelectedItem();
                logger.debug(key + " selected...");

                ScoringBean scoringBean = new ScoringBean();
                getData(scoringBean);

                //Save current Traveller in old Competition.
                Traveller savedTraveller;
                Traveller newTraveller = travellerTableModel.getTraveller();

                BoardId boardId = newTraveller.getBoardId();
                ;
                if (competition != null && scoreTable != null && boardId != null) {
                    scoreTable.clearSelection();
                    savedTraveller = competition.getTraveller(boardId);
                    savedTraveller.copy(newTraveller);

                    //...and pairings
                    competition.setPairings(pairingTableModel.getPairings());

                    //...and persist.
                    dataStore.persist(competition);
                } else {
                    logger.debug("...traveller not saved.");
                }
                ;

                //Fetch newly chosen Competition.
                competition = dataStore.getCompetition(key);
                String name = competition.getCompetitionName();
                int noPairs = competition.getNoPairs();
                int noSets = competition.getNoSets();
                int noBoardsPerSet = competition.getNoBoardsPerSet();

                List<String> pairings = competition.getPairings();

                boardId = new BoardId(competition.getNoSets(), competition.getNoBoardsPerSet());

                scoringBean.setNewSet(Integer.toString(boardId.getSet()));
                scoringBean.setNewBoard(Integer.toString(boardId.getBoard()));

                scoringBean.setCurrentNoPairs(Integer.toString(noPairs));
                scoringBean.setCurrentSets(Integer.toString(noSets));
                scoringBean.setCurrentBoardsPerSet(Integer.toString(noBoardsPerSet));

                pairingTableModel.setNoPairs(noPairs);
                pairingTableModel.setPairings(pairings);

                StringBuilder logLine = new StringBuilder()
                        .append("New Competition selected:")
                        .append(name)
                        .append(", pairs: ")
                        .append(noPairs)
                        .append("(")
                        .append(pairings.size())
                        .append(")");

                logger.debug(logLine);

                //Update view.
                savedTraveller = competition.getTraveller(boardId);
                travellerTableModel.setTraveller(savedTraveller);

                scoringBean.setTravellerComplete(savedTraveller.getCompetionStatus());
                scoringBean.setProgress(competition.getProgress());

                //travellerTableModel.setRowCount(savedTraveller.getScoreLines().size());
                setData(scoringBean);

                mainPanel.repaint();
            }
        });
        addComp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("Add...");
                getData(scoringBean);
                competition = new Competition();
                competition.setCompetitionName(scoringBean.getNewCompetitionName());
                int noPairs = Integer.parseUnsignedInt(scoringBean.getNewNoPairs());
                List<String> pairings = null;
                try {
                    competition.setNoSets(Integer.parseUnsignedInt(scoringBean.getNewSets()));
                    competition.setNoBoardsPerSet(Integer.parseUnsignedInt(scoringBean.getNewBoardsPerSet()));
                    competition.setNoPairs(Integer.parseUnsignedInt(scoringBean.getNewNoPairs()));
                    competition.initialise();

                    pairings = competition.getPairings();
                    dataStore.persist(competition);

                } catch (NumberFormatException ex) {
                    logger.warn("Exception caught in Add listener: " + ex.toString());
                }
                BoardId boardId = new BoardId(competition.getNoSets(), competition.getNoBoardsPerSet());
                scoringBean.setCurrentSets(scoringBean.getNewSets());
                scoringBean.setCurrentBoardsPerSet(scoringBean.getNewBoardsPerSet());
                scoringBean.setCurrentNoPairs(scoringBean.getNewBoardsPerSet());

                pairingTableModel.setNoPairs(noPairs);
                pairingTableModel.setPairings(pairings);

                StringBuilder logLine = new StringBuilder()
                        .append("Competition:")
                        .append(scoringBean.getNewCompetitionName())
                        .append(", pairs: ")
                        .append(noPairs)
                        .append("(")
                        .append(pairings.size())
                        .append(")");

                logger.debug(logLine);

                //Update view.
                Traveller traveller = competition.getTraveller(boardId);
                travellerTableModel.setTraveller(traveller);

                scoringBean.setProgress(competition.getProgress());
                scoringBean.setTravellerComplete(traveller.getCompetionStatus());

                //travellerTableModel.setRowCount(savedTraveller.getScoreLines().size());
                setData(scoringBean);

                //Update comboBox with latest list of Competition names.
                Set<String> names = dataStore.getCompetitionNames();
                nameList.clear();
                nameList.addAll(names);
                compComboBox.setSelectedItem(scoringBean.getNewCompetitionName());

                mainPanel.repaint();

            }
        });
        deleteComp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("Delete...");
                String key = (String) compComboBox.getSelectedItem();
                int index = compComboBox.getSelectedIndex();
                dataStore.delete(key);

                Set<String> names = dataStore.getCompetitionNames();
                nameList.clear();
                nameList.addAll(names);

                int newIndex = Math.min(index - 1, 0);
                compComboBox.setSelectedIndex(newIndex);
                String newSelection = (String) compComboBox.getSelectedItem();
                competition = dataStore.getCompetition(newSelection);

                int noPairs = competition.getNoPairs();
                List<String> pairings = competition.getPairings();

                BoardId boardId = new BoardId(competition.getNoSets(), competition.getNoBoardsPerSet());
                scoringBean.setCurrentSets(scoringBean.getNewSets());
                scoringBean.setCurrentBoardsPerSet(scoringBean.getNewBoardsPerSet());
                scoringBean.setCurrentNoPairs(scoringBean.getNewBoardsPerSet());

                pairingTableModel.setNoPairs(noPairs);
                pairingTableModel.setPairings(pairings);

                StringBuilder logLine = new StringBuilder()
                        .append("Competition:")
                        .append(scoringBean.getNewCompetitionName())
                        .append(", pairs: ")
                        .append(noPairs)
                        .append("(")
                        .append(pairings.size())
                        .append(")");

                logger.debug(logLine);

                //Update view.
                Traveller traveller = competition.getTraveller(boardId);
                travellerTableModel.setTraveller(traveller);

                scoringBean.setTravellerComplete(traveller.getCompetionStatus());
                scoringBean.setProgress(competition.getProgress());

                //travellerTableModel.setRowCount(savedTraveller.getScoreLines().size());
                setData(scoringBean);

                mainPanel.repaint();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("Clear");

                ScoringBean scoringBean = new ScoringBean();
                getData(scoringBean);

                Traveller displayedTraveller = travellerTableModel.getTraveller();
                displayedTraveller.clear();
                travellerTableModel.setTraveller(displayedTraveller);
                BoardId board = displayedTraveller.getBoardId();

                if (competition != null) {
                    competition.getTraveller(board).copy(displayedTraveller);
                    dataStore.persist(competition);
                }
                //travellerTableModel.setTraveller(traveller);
                scoringBean.setTravellerComplete(displayedTraveller.getCompetionStatus());
                scoringBean.setProgress(competition.getProgress());
                mainPanel.repaint();
            }
        });
    }

    public static void main(String[] args) throws DataStoreException {

        dataStore = DataStore.create();

        JFrame frame = new JFrame("ScoresheetForm");
        frame.setContentPane(new ScoringForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        TableColumnModel columnModel =
                new TravellerTableColumnModel();

        scoreTable = new JTable(travellerTableModel, columnModel);

        //TableColumnModel pairingTableColumnModel = new DefaultTableColumnModel();

        pairingTable = new JTable(pairingTableModel);

        setField = new JTextField();
        boardField = new JTextField();

        nameList = new Vector<String>(dataStore.getCompetitionNames());
        compComboBox = new JComboBox<String>(nameList);


    }

    private void navigateTraveller(Function<BoardId, BoardId> navigateTo) {
        //Save current Traveller.
        scoreTable.clearSelection();

        ScoringBean scoringBean = new ScoringBean();
        getData(scoringBean);

        Traveller formTraveller = travellerTableModel.getTraveller();
        BoardId boardId = formTraveller.getBoardId();
        Traveller savedTraveller = competition.getTraveller(boardId);
        formTraveller.setBoardId(boardId.clone());
        savedTraveller.copy(formTraveller);

        //Fetch specified traveller
        BoardId newBoardId = navigateTo.apply(boardId);
        scoringBean.setNewSet(Integer.toString(newBoardId.getSet()));
        scoringBean.setNewBoard(Integer.toString(newBoardId.getBoard()));

        //Update view.
        Traveller newTraveller = competition.getTraveller(newBoardId);
        if (newTraveller.isEmpty() && Objects.equals(boardId.getSet(), newBoardId.getSet())) {
            logger.debug("Traveller " + newBoardId.getSet() + "-" + newBoardId.getBoard() + " is blank.");
            newTraveller = formTraveller.generatePrefilled(newBoardId);
        }
        travellerTableModel.setTraveller(newTraveller);

        scoringBean.setTravellerComplete(newTraveller.getCompetionStatus());
        scoringBean.setProgress(competition.getProgress());

        setData(scoringBean);

        dataStore.persist(competition);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(6, 4, new Insets(0, 0, 0, 0), -1, -1));
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayoutManager(1, 11, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(buttonPanel, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(529, 34), null, 0, false));
        backButton = new JButton();
        backButton.setText("<");
        buttonPanel.add(backButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        forwardButton = new JButton();
        forwardButton.setText(">");
        buttonPanel.add(forwardButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        setLabel = new JLabel();
        this.$$$loadLabelText$$$(setLabel, this.$$$getMessageFromBundle$$$("score-bill-jones", "set"));
        buttonPanel.add(setLabel, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        setField.setBackground(new Color(-1));
        setField.setColumns(4);
        setField.setHorizontalAlignment(4);
        setField.setText("");
        buttonPanel.add(setField, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        boardLabel = new JLabel();
        this.$$$loadLabelText$$$(boardLabel, this.$$$getMessageFromBundle$$$("score-bill-jones", "board"));
        buttonPanel.add(boardLabel, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        boardField.setColumns(4);
        boardField.setHorizontalAlignment(4);
        boardField.setText("");
        buttonPanel.add(boardField, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        goButton = new JButton();
        goButton.setText("Go");
        buttonPanel.add(goButton, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        saveButton = new JButton();
        this.$$$loadButtonText$$$(saveButton, this.$$$getMessageFromBundle$$$("score-bill-jones", "save"));
        buttonPanel.add(saveButton, new GridConstraints(0, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        clearButton = new JButton();
        clearButton.setText("CLEAR");
        buttonPanel.add(clearButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        completionStatusField = new JTextField();
        completionStatusField.setEditable(false);
        completionStatusField.setText("completionStatusField");
        buttonPanel.add(completionStatusField, new GridConstraints(0, 9, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        progressField = new JTextField();
        progressField.setEditable(false);
        progressField.setText("");
        buttonPanel.add(progressField, new GridConstraints(0, 10, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        tablePanel = new JPanel();
        tablePanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tablePanel.setBackground(new Color(-855310));
        mainPanel.add(tablePanel, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(529, 432), null, 0, true));
        tableScrollPane = new JScrollPane();
        tableScrollPane.setBackground(new Color(-1));
        tableScrollPane.setEnabled(false);
        tablePanel.add(tableScrollPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scoreTable.setAutoCreateRowSorter(false);
        scoreTable.setAutoResizeMode(0);
        scoreTable.setBackground(new Color(-1));
        scoreTable.setFillsViewportHeight(false);
        scoreTable.setGridColor(new Color(-13486862));
        scoreTable.setPreferredScrollableViewportSize(new Dimension(1500, 400));
        scoreTable.setRowHeight(40);
        tableScrollPane.setViewportView(scoreTable);
        compPanel = new JPanel();
        compPanel.setLayout(new GridLayoutManager(3, 11, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(compPanel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(529, 69), null, 0, false));
        newCompField = new JTextField();
        newCompField.setText("comp");
        compPanel.add(newCompField, new GridConstraints(1, 0, 1, 6, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        compComboBox.setEditable(true);
        compPanel.add(compComboBox, new GridConstraints(0, 0, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Boards per Set");
        compPanel.add(label1, new GridConstraints(0, 8, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currentNoPairsField = new JTextField();
        currentNoPairsField.setEditable(false);
        currentNoPairsField.setText("n");
        compPanel.add(currentNoPairsField, new GridConstraints(0, 9, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        currentNoPairsLabel = new JLabel();
        currentNoPairsLabel.setText("Pairs");
        compPanel.add(currentNoPairsLabel, new GridConstraints(0, 10, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currentSetsField = new JTextField();
        currentSetsField.setEditable(false);
        compPanel.add(currentSetsField, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Sets");
        compPanel.add(label2, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currentBpSField = new JTextField();
        currentBpSField.setEditable(false);
        compPanel.add(currentBpSField, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        deleteComp = new JButton();
        deleteComp.setText("Delete");
        compPanel.add(deleteComp, new GridConstraints(2, 9, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addComp = new JButton();
        addComp.setText("Add");
        compPanel.add(addComp, new GridConstraints(2, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        newNoPairsField = new JTextField();
        compPanel.add(newNoPairsField, new GridConstraints(2, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        newNoPairsLabel = new JLabel();
        newNoPairsLabel.setText("No of Pairs");
        compPanel.add(newNoPairsLabel, new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        newBpSField = new JTextField();
        compPanel.add(newBpSField, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Boards per Set");
        compPanel.add(label3, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        newSetsField = new JTextField();
        compPanel.add(newSetsField, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Sets");
        compPanel.add(label4, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(318, 11), null, 10, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 20, false));
        final Spacer spacer3 = new Spacer();
        mainPanel.add(spacer3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(529, 14), null, 20, false));
        final Spacer spacer4 = new Spacer();
        mainPanel.add(spacer4, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(529, 14), null, 20, false));
        pairPanel = new JPanel();
        pairPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(pairPanel, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        pairingScrollPane = new JScrollPane();
        pairPanel.add(pairingScrollPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        pairingTable.setAutoResizeMode(4);
        pairingTable.setFillsViewportHeight(true);
        pairingScrollPane.setViewportView(pairingTable);
        final Spacer spacer5 = new Spacer();
        pairPanel.add(spacer5, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        setLabel.setLabelFor(tableScrollPane);
        boardLabel.setLabelFor(boardField);
        label1.setLabelFor(currentBpSField);
        label2.setLabelFor(pairingScrollPane);
        label3.setLabelFor(newBpSField);
        label4.setLabelFor(newSetsField);
    }

    private static Method $$$cachedGetBundleMethod$$$ = null;

    private String $$$getMessageFromBundle$$$(String path, String key) {
        ResourceBundle bundle;
        try {
            Class<?> thisClass = this.getClass();
            if ($$$cachedGetBundleMethod$$$ == null) {
                Class<?> dynamicBundleClass = thisClass.getClassLoader().loadClass("com.intellij.DynamicBundle");
                $$$cachedGetBundleMethod$$$ = dynamicBundleClass.getMethod("getBundle", String.class, Class.class);
            }
            bundle = (ResourceBundle) $$$cachedGetBundleMethod$$$.invoke(null, path, thisClass);
        } catch (Exception e) {
            bundle = ResourceBundle.getBundle(path);
        }
        return bundle.getString(key);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

    public void setData(ScoringBean data) {
        setField.setText(data.getNewSet());
        boardField.setText(data.getNewBoard());
        completionStatusField.setText(data.getTravellerComplete());
        progressField.setText(data.getProgress());
        newCompField.setText(data.getNewCompetitionName());
        currentNoPairsField.setText(data.getCurrentNoPairs());
        currentSetsField.setText(data.getCurrentSets());
        currentBpSField.setText(data.getCurrentBoardsPerSet());
        newNoPairsField.setText(data.getNewNoPairs());
        newBpSField.setText(data.getNewBoardsPerSet());
        newSetsField.setText(data.getNewSets());
    }

    public void getData(ScoringBean data) {
        data.setNewSet(setField.getText());
        data.setNewBoard(boardField.getText());
        data.setTravellerComplete(completionStatusField.getText());
        data.setProgress(progressField.getText());
        data.setNewCompetitionName(newCompField.getText());
        data.setCurrentNoPairs(currentNoPairsField.getText());
        data.setCurrentSets(currentSetsField.getText());
        data.setCurrentBoardsPerSet(currentBpSField.getText());
        data.setNewNoPairs(newNoPairsField.getText());
        data.setNewBoardsPerSet(newBpSField.getText());
        data.setNewSets(newSetsField.getText());
    }

    public boolean isModified(ScoringBean data) {
        if (setField.getText() != null ? !setField.getText().equals(data.getNewSet()) : data.getNewSet() != null)
            return true;
        if (boardField.getText() != null ? !boardField.getText().equals(data.getNewBoard()) : data.getNewBoard() != null)
            return true;
        if (completionStatusField.getText() != null ? !completionStatusField.getText().equals(data.getTravellerComplete()) : data.getTravellerComplete() != null)
            return true;
        if (progressField.getText() != null ? !progressField.getText().equals(data.getProgress()) : data.getProgress() != null)
            return true;
        if (newCompField.getText() != null ? !newCompField.getText().equals(data.getNewCompetitionName()) : data.getNewCompetitionName() != null)
            return true;
        if (currentNoPairsField.getText() != null ? !currentNoPairsField.getText().equals(data.getCurrentNoPairs()) : data.getCurrentNoPairs() != null)
            return true;
        if (currentSetsField.getText() != null ? !currentSetsField.getText().equals(data.getCurrentSets()) : data.getCurrentSets() != null)
            return true;
        if (currentBpSField.getText() != null ? !currentBpSField.getText().equals(data.getCurrentBoardsPerSet()) : data.getCurrentBoardsPerSet() != null)
            return true;
        if (newNoPairsField.getText() != null ? !newNoPairsField.getText().equals(data.getNewNoPairs()) : data.getNewNoPairs() != null)
            return true;
        if (newBpSField.getText() != null ? !newBpSField.getText().equals(data.getNewBoardsPerSet()) : data.getNewBoardsPerSet() != null)
            return true;
        if (newSetsField.getText() != null ? !newSetsField.getText().equals(data.getNewSets()) : data.getNewSets() != null)
            return true;
        return false;
    }
}



