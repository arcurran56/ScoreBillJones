package scorebj.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import scorebj.model.BoardId;
import scorebj.model.Competition;

public class ScoringBean {

    private static final Logger logger = LogManager.getLogger();
    private Competition currentCompetition;
    //private Competition newCompetition;
    private String currentCompetitionName;
    private String currentSets;
    private String currentBoardsPerSet;
    private String currentNoPairs;

    private String newCompetitionName;
    private String newSets;
    private String newBoardsPerSet;
    private String newNoPairs;

    public String getCurrentNoPairs() {
        return Integer.toString(currentCompetition.getNoPairs());
    }

    public void setCurrentNoPairs(String noPairs) {

        try {
            int noPairsVal = Integer.parseUnsignedInt(noPairs);
            currentCompetition.setNoPairs(noPairsVal);
        } catch (NumberFormatException e) {
            logger.warn(e.toString());
        }
    }

    public String getNewNoPairs() {
        return newNoPairs;
    }

    public void setNewNoPairs(String newNoPairs) {
        this.newNoPairs = newNoPairs;
    }

    private BoardId boardId = new BoardId(5, 16);
    public ScoringBean() {
    }

    public String getCurrentCompetitionName() {
        return currentCompetition.getCompetitionName();
    }

    public void setCurrentCompetitionName(String currentCompetitionName) {
        currentCompetition.setCompetitionName(currentCompetitionName);
    }

    public String getCurrentSets() {
        return Integer.toString(currentCompetition.getNoSets());
    }

    public void setCurrentSets(String currentSets) {
        try {
            int currentSetsValue = Integer.parseUnsignedInt(currentSets);
            this.currentCompetition.setNoSets(currentSetsValue);

        } catch (NumberFormatException e) {
            logger.warn(e.toString());
        }
    }

    public Competition getCurrentCompetition() {
        return currentCompetition;
    }

    public void setCurrentCompetition(Competition currentCompetition) {
        this.currentCompetition = currentCompetition;
    }

    public String getCurrentBoardsPerSet() {
        return Integer.toString(currentCompetition.getNoBoardsPerSet());
    }

    public void setCurrentBoardsPerSet(String currentBoardsPerSet) {
        int currentBoardsPerSetVal = Integer.parseUnsignedInt(currentBoardsPerSet);
        try {
            this.currentCompetition.setNoBoardsPerSet(currentBoardsPerSetVal);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }

    public String getNewCompetitionName() {
        return newCompetitionName;
    }

    public void setNewCompetitionName(String newCompetitionName) {
        this.newCompetitionName = newCompetitionName;
    }

    public String getNewSets() {
        return newSets;
    }

    public void setNewSets(String newSets) {
        this.newSets = newSets;
    }

    public String getNewBoardsPerSet() {
        return newBoardsPerSet;
    }

    public void setNewBoardsPerSet(String newBoardsPerSet) {
        this.newBoardsPerSet = newBoardsPerSet;
    }

    public BoardId getBoardId() {
        return boardId;
    }

    public void setBoardId(BoardId boardId) {
        this.boardId = boardId;
    }

    public String getSet() {
        return boardId.getSet().toString();
    }

    public void setSet(String set) {
        try {
            int parsedSet = Integer.parseUnsignedInt(set);
            this.boardId.setSet(parsedSet);
        } catch (NumberFormatException e) {
            logger.warn(e.toString())  ;
        }
    }

    public String getBoard() {
        return boardId.getBoard().toString();
    }

    public void setBoard(String board) {
        try {
            int parsedBoard = Integer.parseUnsignedInt(board);
            this.boardId.setBoard(parsedBoard);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public BoardId next() {
        return boardId.next();
    }

    public BoardId prev() {
        return boardId.prev();
    }

}