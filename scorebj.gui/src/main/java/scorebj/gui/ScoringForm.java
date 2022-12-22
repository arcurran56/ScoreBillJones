package scorebj.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private JButton publishButton;

    private final TravellerTableModel travellerTableModel = new TravellerTableModel();

    private final PairingTableModel pairingTableModel = new PairingTableModel();
    private final ScoringFormActions actions = new ScoringFormActions();


    public ScoringForm() {

        $$$setupUI$$$();

        startUp();


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("back..." + e.getActionCommand());

                CurrentTravellerBean currentTravellerBean = new CurrentTravellerBean();
                getData(currentTravellerBean);
                actions.backButtonActionPerformed(currentTravellerBean);
                setData(currentTravellerBean);

                logger.debug("...repaint back");
                mainPanel.repaint();
            }

        });
        forwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("forward..." + e.getActionCommand());

                CurrentTravellerBean currentTravellerBean = new CurrentTravellerBean();
                getData(currentTravellerBean);
                actions.forwardButtonActionPerformed(currentTravellerBean);
                setData(currentTravellerBean);

                logger.debug("...repaint forward");
            }
        });
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("go..." + e.getActionCommand());

                CurrentTravellerBean currentTravellerBean = new CurrentTravellerBean();
                getData(currentTravellerBean);
                actions.goButtonActionPerformed(currentTravellerBean);
                setData(currentTravellerBean);

                logger.debug("...repaint go");

                mainPanel.repaint();
            }
        });
        travellerTableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                logger.debug("Traveller table change type " + Integer.toString(e.getType()) + " from " + e.getSource());
                int firstRow = e.getFirstRow();
                int column = e.getColumn();

                CurrentTravellerBean currentTravellerBean = new CurrentTravellerBean();
                getData(currentTravellerBean);
                actions.travellerTableChangedAction(currentTravellerBean, travellerTableModel, firstRow, column);
                setData(currentTravellerBean);

                logger.debug("...repaint after traveller changed");

                mainPanel.repaint();
            }
        });
        pairingTableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                PairingTableModel pairingTableModel = (PairingTableModel) e.getSource();
                int type = e.getType();
                logger.debug("Pairing table change type "
                        + type + " from " + e.getSource());

                actions.pairingTableChangedAction(e);

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
                logger.debug("compComboBox: "
                        + e.getActionCommand()
                        + "-"
                        + e.paramString()
                        + " from "
                        + e.getSource());

                //Get selected Competition and save currentCompetitionName for later...
                String selectedCompetitionName = (String) compComboBox.getSelectedItem();
                logger.debug(selectedCompetitionName + " selected...");

                CurrentCompetitionBean currentCompetitionBean = new CurrentCompetitionBean();
                getData(currentCompetitionBean);
                currentCompetitionBean.setSelectedCompetitionName(selectedCompetitionName);

                actions.compComboBoxActionPerformed(currentCompetitionBean);

                //travellerTableModel.setRowCount(savedTraveller.getScoreLines().size());
                setData(currentCompetitionBean);

                logger.debug("...repaint after new competition selected.");

                mainPanel.repaint();
            }
        });
        addComp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("Add..."
                        + e.getActionCommand()
                        + "-"
                        + e.paramString()
                        + " from "
                        + e.getSource());

                NewCompetitionBean newCompetitionBean = new NewCompetitionBean();
                getData(newCompetitionBean);
                String newCompetitionName = newCompetitionBean.getNewCompetitionName();
                logger.debug("Adding: " + newCompetitionName);

                actions.addCompActionPerformed(newCompetitionBean);

                setData(newCompetitionBean);


                //compComboBox.setSelectedIndex(Math.max(compComboBoxModel.getSize() - 1, 0));

                logger.debug("...repaint after add competition");
                mainPanel.repaint();

            }
        });
        deleteComp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("Delete..."
                        + e.getActionCommand()
                        + "-"
                        + e.paramString()
                        + " from "
                        + e.getSource());
                String selectedCompetitionName = (String) compComboBox.getSelectedItem();

                actions.deleteCompActionPerformed(selectedCompetitionName);

                logger.debug("...repaint after competition deleted");
                mainPanel.repaint();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("Clear..."
                        + e.getActionCommand()
                        + "-"
                        + e.paramString()
                        + " from "
                        + e.getSource());

                CurrentTravellerBean currentTravellerBean = new CurrentTravellerBean();
                getData(currentTravellerBean);
                actions.clearButtonActionPerformed(currentTravellerBean);
                setData(currentTravellerBean);

                logger.debug("...repaint after clear");

                mainPanel.repaint();
            }
        });

        publishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("Publish..." + e.getActionCommand());
                actions.publishButtonActionPerformed();

                logger.debug("...repaint after publish.");
                mainPanel.repaint();
            }
        });

    }

    private void startUp() {
        try {
            actions.init(travellerTableModel,
                    pairingTableModel, compComboBoxModel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        compComboBoxModel.addAll(actions.getCompetitionNames());

        CurrentCompetitionBean currentCompetitionBean = new CurrentCompetitionBean();
        CurrentTravellerBean currentTravellerBean = new CurrentTravellerBean();
        NewCompetitionBean newCompetitionBean = new NewCompetitionBean();

        if (compComboBoxModel.getSize() > 0) {
            compComboBox.setSelectedIndex(0);
            actions.compComboBoxActionPerformed(currentCompetitionBean);
        }


        actions.goButtonActionPerformed(currentTravellerBean);
        setData(currentCompetitionBean);
        setData(newCompetitionBean);
        setData(currentTravellerBean);
    }

    public static void main(String[] args) {
        boolean testMode = false;
        if (args.length > 0) {
            testMode = "test".equals(args[0]);
        }
        ScoringFormActions.setTestMode(testMode);
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


    public void setData(CurrentCompetitionBean data) {
        currentNoPairsField.setText(data.getCurrentNoPairs());
        currentSetsField.setText(data.getCurrentSets());
        currentBpSField.setText(data.getCurrentBoardsPerSet());
    }

    public void getData(CurrentCompetitionBean data) {
        data.setCurrentNoPairs(currentNoPairsField.getText());
        data.setCurrentSets(currentSetsField.getText());
        data.setCurrentBoardsPerSet(currentBpSField.getText());
    }

    public void setData(NewCompetitionBean data) {
        newCompField.setText(data.getNewCompetitionName());
        newSetsField.setText(data.getNewSets());
        newBpSField.setText(data.getNewBoardsPerSet());
        newNoPairsField.setText(data.getNewNoPairs());
    }

    public void getData(NewCompetitionBean data) {
        data.setNewCompetitionName(newCompField.getText());
        data.setNewSets(newSetsField.getText());
        data.setNewBoardsPerSet(newBpSField.getText());
        data.setNewNoPairs(newNoPairsField.getText());
    }

    public void setData(CurrentTravellerBean data) {
        setField.setText(data.getSet());
        boardField.setText(data.getBoard());
        completionStatusField.setText(data.getCompletionStatus());
        progressField.setText(data.getProgress());
    }

    public void getData(CurrentTravellerBean data) {
        data.setSet(setField.getText());
        data.setBoard(boardField.getText());
        data.setCompletionStatus(completionStatusField.getText());
        data.setProgress(progressField.getText());
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
        buttonPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 12, new Insets(20, 20, 20, 20), -1, -1));
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
        buttonPanel.add(completionStatusField, new com.intellij.uiDesigner.core.GridConstraints(0, 10, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        progressField.setEditable(false);
        progressField.setText("");
        buttonPanel.add(progressField, new com.intellij.uiDesigner.core.GridConstraints(0, 11, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        publishButton = new JButton();
        publishButton.setText("PUBLISH");
        buttonPanel.add(publishButton, new com.intellij.uiDesigner.core.GridConstraints(0, 9, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        compComboBox.setEditable(false);
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
}



