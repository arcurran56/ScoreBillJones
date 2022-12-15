package scorebj.gui;

public class NewCompetitionBean {
    private String newCompetitionName = "";
    private String newSets = "";
    private String newBoardsPerSet = "";
    private String newNoPairs = "";

    public String getNewCompetitionName() {
        return newCompetitionName;
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

    public String getNewNoPairs() {
        return newNoPairs;
    }

    public void setNewNoPairs(String newNoPairs) {
        this.newNoPairs = newNoPairs;
    }

    public void setNewCompetitionName(String newCompetitionName) {
        this.newCompetitionName = newCompetitionName;
    }
}
