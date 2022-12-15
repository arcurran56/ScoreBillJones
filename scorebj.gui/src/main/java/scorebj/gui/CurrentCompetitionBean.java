package scorebj.gui;

public class CurrentCompetitionBean {

    private String selectedCompetitionName = "";
    private String currentSets = "0";
    private String currentBoardsPerSet = "0";
    private String currentNoPairs = "0";


    public String getCurrentNoPairs() {
        return currentNoPairs;
    }

    public void setCurrentNoPairs(String noPairs) {
        this.currentNoPairs = noPairs;
     }


    public CurrentCompetitionBean() {
    }
    public String getSelectedCompetitionName() {
        return selectedCompetitionName;
    }

    public void setSelectedCompetitionName(String selectedCompetitionName) {
        this.selectedCompetitionName = selectedCompetitionName;
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

    }