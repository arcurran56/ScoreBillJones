package scorebj.gui;

public class ScoringBean {

    private String currentCompetitionName = "";
    private String currentSets = "0";
    private String currentBoardsPerSet = "0";
    private String currentNoPairs = "0";

    private String newCompetitionName = "";
    private String newSets = "0";
    private String newBoardsPerSet = "0";
    private String newNoPairs = "0";

    private String newSet = "0";
    private String travellerComplete = "";
    private String progress = "";
    private String completionStatus = "";

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

    private String newBoard = "0";

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
    public String getCurrentCompetitionName() {
        return currentCompetitionName;
    }

    public void setCurrentCompetitionName(String currentCompetitionName) {
        this.currentCompetitionName = currentCompetitionName;
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
    
    public String getProgress() {
        return progress;
    }

    public void setProgress(final String progress) {
        this.progress = progress;
    }

    public String getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(final String completionStatus) {
        this.completionStatus = completionStatus;
    }
}