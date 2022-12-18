package scorebj.model;

import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Competition {
    private static final Logger logger = LogManager.getLogger();
    private final String DEFAULT_COMPETITION_NAME = "";

    private String competitionName = DEFAULT_COMPETITION_NAME;
    private final Date date = new Date();
    private int noSets;
    private int noBoardsPerSet;
    private int noPairs;
    private final List<String> pairings = new ArrayList<>(20);

    private final List<Traveller> travellers = new ArrayList<>(100);

    public Competition() {
        Traveller defaultTraveller = new Traveller();
        //defaultTraveller.setBoardId(new BoardId(1, 1));
        travellers.add(0, defaultTraveller);
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public int getNoSets() {
        return noSets;
    }

    public void setNoSets(int noSets) {
        int oldVal = this.noSets;
        this.noSets = noSets;
    }

    public int getNoBoardsPerSet() {
        return noBoardsPerSet;
    }

    public void setNoBoardsPerSet(int noBoardsPerSet) {
        int oldVal = this.noBoardsPerSet;
        this.noBoardsPerSet = noBoardsPerSet;

    }

    public List<String> getPairings() {
        return pairings;
    }

    public void setPairings(List<String> pairings) {

        this.pairings.clear();
        this.pairings.addAll(pairings);
    }

    public List<Traveller> getTravellers() {
        return travellers;
    }

    public Traveller getTraveller(BoardId boardId) {
        int index = (boardId.getSet() - 1) * noBoardsPerSet + boardId.getBoard() - 1;
        Traveller traveller = null;
        if (index >= 0) {
            traveller = travellers.get(index);
        }
        if (traveller == null) {
            traveller = new Traveller(boardId.clone(), noPairs / 2);
        }
        return traveller;
    }

    public List<String> travellersToText() {
        List<String> strings = new ArrayList<>(500);
        BoardId boardId = new BoardId(noSets, noBoardsPerSet);
        StringBuilder header;
        for (Traveller traveller : travellers) {
            header = new StringBuilder();
            header.append(boardId.getSet())
                    .append(",")
                    .append(boardId.getBoard())
                    .append(",")
                    .append(boardId.getVulnerabilityStatus());
            strings.add(header.toString());
            strings.addAll(traveller.toStringList());
            boardId = boardId.next();
        }
        return strings;
    }

    public int getNoPairs() {
        return noPairs;
    }

    public void setNoPairs(int noPairs) {
        int oldVal = this.noPairs;
        this.noPairs = noPairs;
    }

    /**
     * Initialise Competition creating requisite number of empty travellers and pairings.
      */
    public void initialise() {
        logger.debug("Initialising Competition, " + competitionName + "...");
        logger.debug("...for " + noPairs + " pairs...");
        logger.debug("...with " + noSets + " of " + noBoardsPerSet + ".");
        BoardId boardId = new BoardId(noSets, noBoardsPerSet);
        if (noSets > 0 && noBoardsPerSet > 0 && noPairs > 0) {
            travellers.clear();
            Traveller traveller;
            for (int i = 0; i < noSets * noBoardsPerSet; i++) {
                traveller = new Traveller(boardId.clone(), noPairs / 2);
                travellers.add(traveller);
                boardId = boardId.next();
            }
            for (int i = 0; i < noPairs; i++) {
                pairings.add("");
            }
        }
    }

    public String getProgress() {
        logger.debug("...checking progress...");
        int completionCount = 0;
        for (Traveller t : travellers) {
            if (t.isComplete()) completionCount++;
        }
        String msg = " " + completionCount +
                "/" +
                travellers.size() +
                " complete";
        logger.debug(msg);
        return msg;
    }
    public String toString(){
        return "Competition: " + competitionName
                + "(" + noPairs + " pairs, " + noSets + " sets, " + noBoardsPerSet + ")";
    }
}
