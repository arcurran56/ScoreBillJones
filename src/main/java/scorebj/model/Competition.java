package scorebj.model;

import java.io.IOException;
import java.util.*;

public class Competition {
    private final String DEFAULT_COMPETITION_NAME = "Bill Jones";
    private final int DEFAULT_SETS = 5;
    private final int DEFAULT_BOARDS_PER_SET = 16;
    private final int DEFAULT_NO_PAIRS = 10;

    private String competitionName = DEFAULT_COMPETITION_NAME;
    private Date date = new Date();
    private int noSets = DEFAULT_SETS;
    private int noBoardsPerSet = DEFAULT_BOARDS_PER_SET;
    private int noPairs = DEFAULT_NO_PAIRS;

    private final String[] DEFAULT_PAIRINGS = {"David & Salette",
            "Liz B & Jane E",
            "Jill R & Rob",
            "Jill H & Jane D",
            "Wendy & Diana",
            "Diane & Alan",
            "Julia & Sue",
            "Clare & Jill A",
            "Lisbeth & Vicky",
            "Caroline & Andrew"};

    private List<String> pairings = new ArrayList<>(10);
    private final Traveller[] travellers = new Traveller[noSets * noBoardsPerSet];

    private final Map<Integer, Integer[]> resultsMap = new TreeMap<>();

    private BoardId currentBoard;

    public Competition() {

        pairings = Arrays.asList(DEFAULT_PAIRINGS);
        Traveller traveller;
        for (int index = 0; index < noSets * noBoardsPerSet; index++) {
            traveller = new Traveller(noPairs / 2);
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

    public List<String> getPairings() {
        return pairings;
    }

    public void setPairings(List<String> pairings) {
        this.pairings = pairings;
    }

    public Traveller[] getTravellers() {
        return travellers;
    }

    public Traveller getTraveller(BoardId boardId) {
        int index = (boardId.getSet() - 1) * noBoardsPerSet + boardId.getBoard() - 1;
        return travellers[index];
    }

    public void saveResults() {
        Result result = new Result(noPairs, noSets, noBoardsPerSet);
        result.collate(pairings, travellers);
        try {
            result.printDetails();
            result.printSummary();
            result.printMatrix();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> travellersToText() {
        List<String> strings = new ArrayList<>(500);
        BoardId boardId = new BoardId(noSets, noBoardsPerSet);
        StringBuilder header;
        for (Traveller traveller : travellers) {
            header = new StringBuilder();
            header.append(boardId.getSet())
                    .append(",")
                    .append(boardId.getBoard());
            strings.add(header.toString());
            strings.addAll(traveller.toStringList());
            boardId.next();
        }
        return strings;
    }
}
