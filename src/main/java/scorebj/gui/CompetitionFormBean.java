package scorebj.gui;

public class CompetitionFormBean {
    private String noSets;
    private String noBoards;
    private String competitionName;
    private String date;

    public CompetitionFormBean() {
    }

    public String getNoSets() {
        return noSets;
    }

    public void setNoSets(final String noSets) {
        this.noSets = noSets;
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(final String competitionName) {
        this.competitionName = competitionName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }
}