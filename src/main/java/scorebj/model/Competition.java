package scorebj.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Competition {
    private final String DEFAULT_COMPETITION_NAME = "Bill Jones";
    private final int DEFAULT_SETS = 5;
    private final int DEFAULT_BOARDS_PER_SET = 16;

    private String competitionName= DEFAULT_COMPETITION_NAME;
    private Date date = new Date();
    private int noSets = DEFAULT_SETS;
    private int noBoardsPerSet = DEFAULT_BOARDS_PER_SET;

    private List<Pairing> pairings = new ArrayList<>(10);
    private final Traveller[] travellers = new Traveller[noSets * noBoardsPerSet];

    private BoardId currentBoard;

    public Competition(){

        Traveller traveller;
        for (int index=0; index<noSets*noBoardsPerSet; index++){
            traveller = new Traveller();
            travellers[index] = traveller;
        }
    }

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

    public int getNoSets() {
        return noSets;
    }

    public void setNoSets(int noSets) {
        this.noSets = noSets;
    }

    public int getNoBoardsPerSet() {
        return noBoardsPerSet;
    }

    public void setNoBoardsPerSet(int noBoardsPerSet) {
        this.noBoardsPerSet = noBoardsPerSet;
    }

    public List<Pairing> getPairings() {
        return pairings;
    }

    public void setPairings(List<Pairing> pairings) {
        this.pairings = pairings;
    }

    public Traveller[] getTravellers() {
        return travellers;
    }

    public Traveller getTraveller(BoardId boardId){
        int index = (boardId.getSet()-1)* noBoardsPerSet + boardId.getBoard() -1;
        return travellers[index];
    }


}
