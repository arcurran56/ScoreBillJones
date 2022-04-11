package scorebj.model;

import java.util.Date;
import java.util.List;

public class Competition {
    private String competitionName;
    private Date date;
    private int noOfSets;
    private int noOfBoards;
    private List<Pairing> pairings;
    private final BoardScore[][] boards = new BoardScore[noOfSets][noOfBoards];

    private int finalScore;

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNoOfSets() {
        return noOfSets;
    }

    public void setNoOfSets(int noOfSets) {
        this.noOfSets = noOfSets;
    }

    public int getNoOfBoards() {
        return noOfBoards;
    }

    public void setNoOfBoards(int noOfBoards) {
        this.noOfBoards = noOfBoards;
    }

    public List<Pairing> getPairings() {
        return pairings;
    }

    public void setPairings(List<Pairing> pairings) {
        this.pairings = pairings;
    }

    public BoardScore[][] getBoards() {
        return boards;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }
    public void BoardId 
}
