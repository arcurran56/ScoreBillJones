package scorebj.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scorebj.model.DataStoreException;
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

public class ScoringForm {
    private static final Logger logger = LogManager.getLogger();

    private final JFrame frame = new JFrame("Scoring Bill Jones");
    private final DefaultComboBoxModel<String> compComboBoxModel = new DefaultComboBoxModel<>();

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
    private JLabel currentBpSLabel;
    private JLabel newSetsLabel;
    private JLabel newBpSLabel;
    private JLabel currentSetsLabel;

    private final TravellerTableModel travellerTableModel = new TravellerTableModel();

    private final PairingTableModel pairingTableModel = new PairingTableModel();
    private final ScoringFormActions actions = new ScoringFormActions();


    public ScoringForm() throws DataStoreException {

        $$$setupUI$$$();

        TableColumnModel travellerTableColumnModel =
                new TravellerTableColumnModel();

        ScoringBean scoringBean = new ScoringBean();

        compComboBoxModel.addAll(actions.getCompetitionNames());

        String competitionName = "";
        if (compComboBoxModel.getSize() > 0) {
            compComboBox.setSelectedIndex(0);
            competitionName = (String) compComboBox.getSelectedItem();
        }
        ;

        scoringBean.setCurrentCompetitionName(competitionName);
        try {
            actions.init(scoringBean, travellerTableModel,
                    pairingTableModel, compComboBoxModel);
        } catch (DataStoreException e) {
            throw new RuntimeException(e);
        }

        setData(scoringBean);
        mainPanel.repaint();

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("back..." + e.getActionCommand());

                ScoringBean scoringBean = new ScoringBean();
                getData(scoringBean);
                actions.backButtonActionPerformed(scoringBean);
                setData(scoringBean);

                logger.debug("...repaint back");
                mainPanel.repaint();
            }

        });
        forwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("forward..." + e.getActionCommand());

                ScoringBean scoringBean = new ScoringBean();
                getData(scoringBean);
                actions.forwardButtonActionPerformed(scoringBean);
                setData(scoringBean);

                logger.debug("...repaint forward");
            }
        });
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("go..." + e.getActionCommand());

                ScoringBean scoringBean = new ScoringBean();
                getData(scoringBean);
                actions.goButtonActionPerformed(scoringBean);
                setData(scoringBean);

                logger.debug("...repaint go");

                mainPanel.repaint();
            }
        });
        travellerTableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                logger.debug("Traveller table changed: " + e.getSource());
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                ScoringBean scoringBean = new ScoringBean();
                getData(scoringBean);
                actions.travellerTableChangedAction(scoringBean, travellerTableModel, firstRow, column);
                setData(scoringBean);

                logger.debug("...repaint after traveller changed");

                mainPanel.repaint();
            }
        });
        pairingTableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                PairingTableModel pairingTableModel = (PairingTableModel) e.getSource();
                logger.debug("Pairing table changed from " + e.getSource());

                ScoringBean scoringBean = new ScoringBean();
                getData(scoringBean);
                logger.debug("...repaint after traveller changed");
                actions.pairingTableChangedAction(e);
                setData(scoringBean);

                logger.debug("...repaint after pairings changed");

                mainPanel.repaint();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("Save..." + e.getActionCommand());
                actions.saveButtonActionPerformed();

                logger.debug("...repaint after traveller changed");
                mainPanel.repaint();
            }
        });
        compComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("compComboBox: " + e.getActionCommand());

                //Get selected Competition and save currentCompetitionName for later...
                String currentCompetitionName = (String) compComboBox.getSelectedItem();
                logger.debug(currentCompetitionName + " selected...");

                ScoringBean scoringBean = new ScoringBean();
                scoringBean.setCurrentCompetitionName(currentCompetitionName);

                actions.compComboBoxActionPerformed(scoringBean);
                //Save current Traveller in old Competition.


                //travellerTableModel.setRowCount(savedTraveller.getScoreLines().size());
                setData(scoringBean);

                logger.debug("...repaint after new  competition");

                mainPanel.repaint();
            }
        });
        addComp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("Add..." + e.getActionCommand());

                ScoringBean scoringBean = new ScoringBean();
                getData(scoringBean);
                String newCompetitionName = scoringBean.getNewCompetitionName();

                actions.addCompActionPerformed(scoringBean);

                setData(scoringBean);

                compComboBoxModel.addElement(newCompetitionName);
                compComboBox.setSelectedIndex(Math.max(compComboBoxModel.getSize() - 1, 0));

                logger.debug("...repaint after add competitiion");

                mainPanel.repaint();

            }
        });
        deleteComp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("Delete..." + e.getActionCommand());
                String key = (String) compComboBox.getSelectedItem();
                int index = compComboBox.getSelectedIndex();

                ScoringBean scoringBean = new ScoringBean();
                getData(scoringBean);
                scoringBean.setCurrentCompetitionName(key);

                actions.deleteCompActionPerformed(scoringBean);
                //travellerTableModel.setRowCount(savedTraveller.getScoreLines().size());
                setData(scoringBean);

                compComboBox.setSelectedItem(0);

                logger.debug("...repaint after competition deleted");

                mainPanel.repaint();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("Clear..." + e.getActionCommand());

                ScoringBean scoringBean = new ScoringBean();
                getData(scoringBean);
                actions.clearButtonActionPerformed(scoringBean);

                setData(scoringBean);

                logger.debug("...repaint after clear");

                mainPanel.repaint();
            }
        });
    }

    public static void main(String[] args) throws DataStoreException {

        JFrame frame = new JFrame("ScoresheetForm");
        frame.setContentPane(new ScoringForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        TableColumnModel travellerTableColumnModel =
                new TravellerTableColumnModel();

        compComboBox = new JComboBox<>();
        compComboBox.setModel(compComboBoxModel);

        String competitionName = "";
        if (compComboBoxModel.getSize() > 0) {
            compComboBox.setSelectedIndex(0);
            competitionName = (String) compComboBox.getSelectedItem();
        }

        scoreTable = new JTable(travellerTableModel, travellerTableColumnModel);
        pairingTable = new JTable(pairingTableModel);

        setField = new JTextField();
        boardField = new JTextField();

        currentNoPairsField = new JTextField();
        currentSetsField = new JTextField();
        currentBpSField = new JTextField();

        completionStatusField = new JTextField();
        progressField = new JTextField();

        newCompField = new JTextField();
        newBpSField = new JTextField();
        newNoPairsField = new JTextField();
        newSetsField = new JTextField();

        logger.debug("...end createUIcomponents.");
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
        mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 2, new Insets(20, 20, 20, 20), -1, -1));
        mainPanel.setBackground(new Color(-2097185));
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 11, new Insets(20, 20, 20, 20), -1, -1));
        buttonPanel.setBackground(new Color(-2097185));
        mainPanel.add(buttonPanel, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(500, 50), null, 0, false));
        backButton = new JButton();
        backButton.setBackground(new Color(-1));
        backButton.setText("<");
        buttonPanel.add(backButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        forwardButton = new JButton();
        forwardButton.setBackground(new Color(-1));
        forwardButton.setText(">");
        buttonPanel.add(forwardButton, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        setLabel = new JLabel();
        this.$$$loadLabelText$$$(setLabel, this.$$$getMessageFromBundle$$$("score-bill-jones", "set"));
        buttonPanel.add(setLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        setField.setBackground(new Color(-1));
        setField.setColumns(4);
        setField.setHorizontalAlignment(4);
        setField.setText("");
        buttonPanel.add(setField, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        boardLabel = new JLabel();
        this.$$$loadLabelText$$$(boardLabel, this.$$$getMessageFromBundle$$$("score-bill-jones", "board"));
        buttonPanel.add(boardLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        boardField.setColumns(4);
        boardField.setHorizontalAlignment(4);
        boardField.setText("");
        buttonPanel.add(boardField, new com.intellij.uiDesigner.core.GridConstraints(0, 6, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        goButton = new JButton();
        goButton.setBackground(new Color(-1));
        goButton.setText("Go");
        buttonPanel.add(goButton, new com.intellij.uiDesigner.core.GridConstraints(0, 7, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        saveButton = new JButton();
        saveButton.setBackground(new Color(-1));
        saveButton.setEnabled(true);
        this.$$$loadButtonText$$$(saveButton, this.$$$getMessageFromBundle$$$("score-bill-jones", "save"));
        buttonPanel.add(saveButton, new com.intellij.uiDesigner.core.GridConstraints(0, 8, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        clearButton = new JButton();
        clearButton.setBackground(new Color(-1));
        clearButton.setText("CLEAR");
        buttonPanel.add(clearButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        completionStatusField.setEditable(false);
        completionStatusField.setText("");
        buttonPanel.add(completionStatusField, new com.intellij.uiDesigner.core.GridConstraints(0, 9, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        progressField.setEditable(false);
        progressField.setText("");
        buttonPanel.add(progressField, new com.intellij.uiDesigner.core.GridConstraints(0, 10, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        tablePanel = new JPanel();
        tablePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(20, 20, 20, 20), -1, -1));
        tablePanel.setBackground(new Color(-2097185));
        mainPanel.add(tablePanel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(529, 432), null, 0, true));
        tableScrollPane = new JScrollPane();
        tableScrollPane.setBackground(new Color(-1));
        tableScrollPane.setEnabled(false);
        tablePanel.add(tableScrollPane, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(1206, 428), null, 0, false));
        scoreTable.setAutoCreateRowSorter(false);
        scoreTable.setAutoResizeMode(4);
        scoreTable.setBackground(new Color(-1));
        scoreTable.setFillsViewportHeight(false);
        scoreTable.setGridColor(new Color(-13486862));
        scoreTable.setPreferredScrollableViewportSize(new Dimension(1500, 400));
        scoreTable.setRowHeight(40);
        tableScrollPane.setViewportView(scoreTable);
        compPanel = new JPanel();
        compPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 11, new Insets(20, 20, 20, 20), -1, -1));
        compPanel.setBackground(new Color(-2097185));
        mainPanel.add(compPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(529, 69), null, 0, false));
        newCompField.setText("");
        compPanel.add(newCompField, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 6, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        compComboBox.setEditable(true);
        compPanel.add(compComboBox, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currentNoPairsField.setEditable(false);
        currentNoPairsField.setText("");
        compPanel.add(currentNoPairsField, new com.intellij.uiDesigner.core.GridConstraints(0, 9, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        currentNoPairsLabel = new JLabel();
        currentNoPairsLabel.setText("Pairs");
        compPanel.add(currentNoPairsLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 10, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currentBpSLabel = new JLabel();
        currentBpSLabel.setText("Boards per Set");
        compPanel.add(currentBpSLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        compPanel.add(newSetsField, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        newSetsLabel = new JLabel();
        newSetsLabel.setText("Sets");
        compPanel.add(newSetsLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currentSetsField.setEditable(false);
        compPanel.add(currentSetsField, new com.intellij.uiDesigner.core.GridConstraints(0, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        deleteComp = new JButton();
        deleteComp.setBackground(new Color(-1));
        deleteComp.setText("Delete");
        compPanel.add(deleteComp, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currentBpSField.setEditable(false);
        compPanel.add(currentBpSField, new com.intellij.uiDesigner.core.GridConstraints(0, 7, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        newBpSLabel = new JLabel();
        newBpSLabel.setText("Boards per Set");
        compPanel.add(newBpSLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 8, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        compPanel.add(newBpSField, new com.intellij.uiDesigner.core.GridConstraints(3, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        newNoPairsLabel = new JLabel();
        newNoPairsLabel.setText("No of Pairs");
        compPanel.add(newNoPairsLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 5, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        compPanel.add(newNoPairsField, new com.intellij.uiDesigner.core.GridConstraints(3, 6, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        currentSetsLabel = new JLabel();
        currentSetsLabel.setText("Sets");
        compPanel.add(currentSetsLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 6, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addComp = new JButton();
        addComp.setBackground(new Color(-1));
        addComp.setText("Add");
        compPanel.add(addComp, new com.intellij.uiDesigner.core.GridConstraints(2, 6, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        compPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(1, 7, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 40), null, null, 0, false));
        pairPanel = new JPanel();
        pairPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(20, 20, 20, 20), -1, -1));
        pairPanel.setBackground(new Color(-2097185));
        mainPanel.add(pairPanel, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        pairingScrollPane = new JScrollPane();
        pairPanel.add(pairingScrollPane, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        pairingTable.setAutoResizeMode(4);
        pairingTable.setFillsViewportHeight(true);
        pairingTable.setGridColor(new Color(-16777216));
        pairingScrollPane.setViewportView(pairingTable);
        setLabel.setLabelFor(setField);
        boardLabel.setLabelFor(boardField);
        currentNoPairsLabel.setLabelFor(currentNoPairsField);
        currentBpSLabel.setLabelFor(currentBpSField);
        newSetsLabel.setLabelFor(newSetsField);
        newBpSLabel.setLabelFor(newBpSField);
        newNoPairsLabel.setLabelFor(newNoPairsField);
        currentSetsLabel.setLabelFor(currentSetsField);
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
        completionStatusField.setText(data.getCompletionStatus());
        progressField.setText(data.getProgress());
        newCompField.setText(data.getNewCompetitionName());
        currentNoPairsField.setText(data.getCurrentNoPairs());
        newSetsField.setText(data.getNewSets());
        currentSetsField.setText(data.getCurrentSets());
        currentBpSField.setText(data.getCurrentBoardsPerSet());
        newBpSField.setText(data.getNewBoardsPerSet());
        newNoPairsField.setText(data.getNewNoPairs());

    }

    public void getData(ScoringBean data) {
        data.setNewSet(setField.getText());
        data.setNewBoard(boardField.getText());
        data.setCompletionStatus(completionStatusField.getText());
        data.setProgress(progressField.getText());
        data.setNewCompetitionName(newCompField.getText());
        data.setCurrentNoPairs(currentNoPairsField.getText());
        data.setNewSets(newSetsField.getText());
        data.setCurrentSets(currentSetsField.getText());
        data.setCurrentBoardsPerSet(currentBpSField.getText());
        data.setNewBoardsPerSet(newBpSField.getText());
        data.setNewNoPairs(newNoPairsField.getText());
    }

    public boolean isModified(ScoringBean data) {
        if (setField.getText() != null ? !setField.getText().equals(data.getNewSet()) : data.getNewSet() != null)
            return true;
        if (boardField.getText() != null ? !boardField.getText().equals(data.getNewBoard()) : data.getNewBoard() != null)
            return true;
        if (completionStatusField.getText() != null ? !completionStatusField.getText().equals(data.getCompletionStatus()) : data.getCompletionStatus() != null)
            return true;
        if (progressField.getText() != null ? !progressField.getText().equals(data.getProgress()) : data.getProgress() != null)
            return true;
        if (newCompField.getText() != null ? !newCompField.getText().equals(data.getNewCompetitionName()) : data.getNewCompetitionName() != null)
            return true;
        if (currentNoPairsField.getText() != null ? !currentNoPairsField.getText().equals(data.getCurrentNoPairs()) : data.getCurrentNoPairs() != null)
            return true;
        if (newSetsField.getText() != null ? !newSetsField.getText().equals(data.getNewSets()) : data.getNewSets() != null)
            return true;
        if (currentSetsField.getText() != null ? !currentSetsField.getText().equals(data.getCurrentSets()) : data.getCurrentSets() != null)
            return true;
        if (currentBpSField.getText() != null ? !currentBpSField.getText().equals(data.getCurrentBoardsPerSet()) : data.getCurrentBoardsPerSet() != null)
            return true;
        if (newBpSField.getText() != null ? !newBpSField.getText().equals(data.getNewBoardsPerSet()) : data.getNewBoardsPerSet() != null)
            return true;
        if (newNoPairsField.getText() != null ? !newNoPairsField.getText().equals(data.getNewNoPairs()) : data.getNewNoPairs() != null)
            return true;
        return false;
    }
}



