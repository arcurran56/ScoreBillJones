package scorebj.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import scorebj.model.BoardId;
import scorebj.model.Competition;

public class ScoringBean {

    private static final Logger logger = LogManager.getLogger();
    private Competition currentCompetition;

    private String currentCompetitionName;
    private String currentSets = "0";
    private String currentBoardsPerSet = "0";
    private String currentNoPairs = "0";

    private String newCompetitionName = "";
    private String newSets = "";
    private String newBoardsPerSet = "";
    private String newNoPairs = "";

    private String newSet = "1";

    private String travellerComplete;
    private String progress;

    public String getNewSet() {
        return newSet;
    }

    public void setNewSet(String newSet) {
        this.newSet = newSet;
    }

    public String getNewBoard() {
        return newBoard;
    }

    public void setNewBoard(String newBoard) {
        this.newBoard = newBoard;
    }

    private String newBoard = "1";

    public String getCurrentNoPairs() {
        return currentNoPairs;
    }

    public void setCurrentNoPairs(String noPairs) {
        this.currentNoPairs = noPairs;
     }

    public String getNewNoPairs() {
        return newNoPairs;
    }

    public void setNewNoPairs(String newNoPairs) {
        this.newNoPairs = newNoPairs;
    }

    public ScoringBean() {
    }

    public String getCurrentSets() {
        return currentSets;
    }

    public void setCurrentSets(String currentSets) {
        this.currentSets = currentSets;
    }

    public String getCurrentBoardsPerSet() {
        return currentBoardsPerSet;
    }

    public void setCurrentBoardsPerSet(String currentBoardsPerSet) {
        this.currentBoardsPerSet = currentBoardsPerSet;
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


    public String getTravellerComplete() {
        return travellerComplete;
    }

    public void setTravellerComplete(String travellerComplete) {
        this.travellerComplete = travellerComplete;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(final String progress) {
        this.progress = progress;
    }
}