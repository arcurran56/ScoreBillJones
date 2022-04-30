package scorebj.gui;

import scorebj.model.BoardId;
import scorebj.model.Competition;
import scorebj.model.ScoreLine;
import scorebj.model.Traveller;
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
import java.util.ResourceBundle;

public class ScoresheetForm {
    private JFrame frame = new JFrame("Scoring Bill Jones");

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

    private ScoreTableBean scoreTableBean;
    private TravellerTableModel travellerTableModel;
    private Competition competition = new Competition();
    private BoardId currentBoardId;


    public ScoresheetForm() {

        $$$setupUI$$$();


        scoreTableBean = new ScoreTableBean();
        scoreTableBean.setSet("1");
        scoreTableBean.setBoard("1");
        setData(scoreTableBean);

        BoardId boardId = scoreTableBean.getBoardId();
        Traveller traveller = competition.getTraveller(boardId);
        travellerTableModel.setTraveller(traveller);


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("back");

                //Save currrent Traveller.
                scoreTable.clearSelection();
                BoardId boardId = scoreTableBean.getBoardId();
                Traveller savedTraveller = competition.getTraveller(boardId);
                Traveller newTraveller = travellerTableModel.getTraveller();
                savedTraveller.copy(newTraveller);

                //Fetch previous
                boardId = scoreTableBean.prev();

                //Update view.
                savedTraveller = competition.getTraveller(boardId);
                travellerTableModel.setTraveller(savedTraveller);
                setData(scoreTableBean);
                mainPanel.repaint();
            }
        });
        forwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("fwd");

                //Save currrent Traveller.
                scoreTable.clearSelection();
                BoardId boardId = scoreTableBean.getBoardId();
                Traveller savedTraveller = competition.getTraveller(boardId);
                Traveller newTraveller = travellerTableModel.getTraveller();
                savedTraveller.copy(newTraveller);

                //Fetch previous
                boardId = scoreTableBean.next();

                //Update view.
                savedTraveller = competition.getTraveller(boardId);
                travellerTableModel.setTraveller(savedTraveller);
                setData(scoreTableBean);

                mainPanel.repaint();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getData(scoreTableBean);
            }
        });
        travellerTableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                TravellerTableModel travellerTableModel = (TravellerTableModel) e.getSource();
                Traveller newTraveller = travellerTableModel.getTraveller();

                ScoreLine scoreLine;
                for (int row = 0; row < 5; row++) {
                    scoreLine = newTraveller.getScoreLine(row);
                    scoreLine.scoreHand(false);
                }

                BoardId boardId1 = scoreTableBean.getBoardId();
                Traveller savedTraveller = competition.getTraveller(boardId1);
                savedTraveller.copy(newTraveller);

                mainPanel.repaint();
            }
        });
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame("ScoresheetForm");
        frame.setContentPane(new ScoresheetForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        travellerTableModel = new TravellerTableModel();
        TableColumnModel columnModel =
                new TravellerTableColumnModel();
        //new DefaultTableColumnModel();

        scoreTable = new JTable(travellerTableModel, columnModel);

        setField = new JTextField();
        boardField = new JTextField();


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
        mainPanel.setLayout(new GridBagLayout());
        infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(infoPanel, gbc);
        setLabel = new JLabel();
        this.$$$loadLabelText$$$(setLabel, this.$$$getMessageFromBundle$$$("score-bill-jones", "set"));
        infoPanel.add(setLabel);
        setField.setBackground(new Color(-1));
        setField.setColumns(4);
        setField.setHorizontalAlignment(4);
        setField.setText("");
        infoPanel.add(setField);
        boardLabel = new JLabel();
        this.$$$loadLabelText$$$(boardLabel, this.$$$getMessageFromBundle$$$("score-bill-jones", "board"));
        infoPanel.add(boardLabel);
        boardField.setColumns(4);
        boardField.setHorizontalAlignment(4);
        boardField.setText("");
        infoPanel.add(boardField);
        goButton = new JButton();
        goButton.setText("Go");
        infoPanel.add(goButton);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(buttonPanel, gbc);
        backButton = new JButton();
        backButton.setText("<");
        buttonPanel.add(backButton);
        forwardButton = new JButton();
        forwardButton.setText(">");
        buttonPanel.add(forwardButton);
        saveButton = new JButton();
        this.$$$loadButtonText$$$(saveButton, this.$$$getMessageFromBundle$$$("score-bill-jones", "save"));
        buttonPanel.add(saveButton);
        tablePanel = new JPanel();
        tablePanel.setLayout(new GridBagLayout());
        tablePanel.setBackground(new Color(-4787514));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(tablePanel, gbc);
        tableScrollPane = new JScrollPane();
        tableScrollPane.setBackground(new Color(-866604));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        tablePanel.add(tableScrollPane, gbc);
        scoreTable.setAutoCreateRowSorter(false);
        scoreTable.setAutoResizeMode(0);
        scoreTable.setBackground(new Color(-65680));
        scoreTable.setFillsViewportHeight(false);
        scoreTable.setGridColor(new Color(-13486862));
        scoreTable.setPreferredScrollableViewportSize(new Dimension(1500, 400));
        scoreTable.setRowHeight(40);
        tableScrollPane.setViewportView(scoreTable);
        setLabel.setLabelFor(tableScrollPane);
        boardLabel.setLabelFor(boardField);
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

    public void setData(ScoreTableBean data) {
        setField.setText(data.getSet());
        boardField.setText(data.getBoard());
    }

    public void getData(ScoreTableBean data) {
        data.setSet(setField.getText());
        data.setBoard(boardField.getText());
    }

    public boolean isModified(ScoreTableBean data) {
        if (setField.getText() != null ? !setField.getText().equals(data.getSet()) : data.getSet() != null) return true;
        if (boardField.getText() != null ? !boardField.getText().equals(data.getBoard()) : data.getBoard() != null)
            return true;
        return false;
    }
}



