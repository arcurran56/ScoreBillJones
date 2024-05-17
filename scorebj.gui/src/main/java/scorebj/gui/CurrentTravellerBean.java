package scorebj.gui;

public class CurrentTravellerBean {
    private String board = "1";
    private String set ="1";
    private String completionStatus = "Incomplete";
    private String progress = "";

    public String getVulnerability() {
        return vulnerability;
    }

    public void setVulnerability(String vulnerability) {
        this.vulnerability = vulnerability;
    }

    private String vulnerability ="None";

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getCompletionStatus() {
        return completionStatus;
    }

    public CurrentTravellerBean() {
    }

     public void setCompletionStatus(final String completionStatus) {
        this.completionStatus = completionStatus;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(final String progress) {
        this.progress = progress;
    }
}
