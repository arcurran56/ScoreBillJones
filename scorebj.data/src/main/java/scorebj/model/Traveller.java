package scorebj.model;


import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;


public class Traveller implements PropertyChangeListener {

    @XStreamOmitField
    private static final Logger logger = LogManager.getLogger();
    private final int size;

    private final BoardId boardId;

    public BoardId getBoardId() {
        return boardId;
    }

     public Traveller(){
        throw new RuntimeException();
    }
    public Traveller(BoardId boardId, int size) {
        this.size = size;
        this.boardId = boardId;

        ScoreLine scoreLine;
        for (int i = 0; i < size; i++) {
            scoreLine = new ScoreLine();
            scoreLine.setVulnerability(boardId.getVulnerabilityStatus());
            scoreLines.add(scoreLine);
        }

    }

    public List<ScoreLine> getScoreLines() {
        return scoreLines;
    }

    public ScoreLine getScoreLine(int line) {
        return scoreLines.get(line);
    }

    private List<ScoreLine> scoreLines = new ArrayList<>(5);

    public void copy(Traveller traveller) {
        this.scoreLines = traveller.getScoreLines();
    }

    /**
     * Generate a new traveller pre-filled with pairings from this Traveller.
     * @param newBoardId
     * @return
     */
    public Traveller generatePrefilled(BoardId newBoardId) {
        Traveller newTraveller = new Traveller(newBoardId, size);

        List<ScoreLine> newScoreLines = newTraveller.getScoreLines();
        int nsPair;
        int ewPair;
        int newSet = newBoardId.getSet();
        int oldSet = boardId.getSet();
        boolean complete = isComplete();
        if (newSet == oldSet && complete) {
            for (int i=0; i<size; i++) {
                nsPair = scoreLines.get(i).getNsPair();
                ewPair = scoreLines.get(i).getEwPair();
                newScoreLines.get(i).setNsPair(nsPair);
                newScoreLines.get(i).setEwPair(ewPair);
            }
        }
        return  newTraveller;
    }

    public void addAll(List<ScoreLine> scoreLines) {
        this.scoreLines.addAll(scoreLines);
        int newSize = scoreLines.size();
        if ( newSize != size) {
            logger.warn("Size mismatch: " + newSize + " vs " + size);
        }

    }

    public void clear() {
        scoreLines.clear();
        for (int i = 0; i < size; i++) {
            scoreLines.add(new ScoreLine());
        }
    }

    private void allocateMPs() {
        int netScore1;
        int netScore2;

        for (ScoreLine scoreLine1 : scoreLines) {
            int nsMPs = 0;
            int ewMPs = 0;
            if (scoreLine1 != null) {
                if (scoreLine1.getNSScore() != null && scoreLine1.getEWScore() != null) {
                    netScore1 = scoreLine1.getNSScore() - scoreLine1.getEWScore();

                    for (ScoreLine scoreLine2 : scoreLines) {
                        if (scoreLine2 != null) {
                            if (scoreLine2.getNSScore() != null && scoreLine2.getEWScore() != null) {
                                if (scoreLine1 != scoreLine2) {
                                    netScore2 = scoreLine2.getNSScore() - scoreLine2.getEWScore();
                                    switch (Integer.compare(netScore1, netScore2)) {
                                        case -1:
                                            ewMPs = ewMPs + 2;
                                            break;
                                        case 0:
                                            nsMPs++;
                                            ewMPs++;
                                            break;
                                        case 1:
                                            nsMPs = nsMPs + 2;
                                            break;

                                    }
                                }
                            }
                        }

                    }
                    scoreLine1.setNsMPs(nsMPs);
                    scoreLine1.setEwMPs(ewMPs);
                }

            }

        }

    }
    public boolean isEmpty() {
        int emptyLines = 0;
        boolean empty;
        for (ScoreLine scoreLine: scoreLines){
            if (scoreLine.getNsPair() == null && scoreLine.getEwPair() == null){
                emptyLines++;
            }
        }
        empty = emptyLines == size;
        return empty;
    }
    public boolean isComplete(){
        int completedLines = 0;
        boolean complete;
        for (ScoreLine scoreLine: scoreLines){
            if (scoreLine.getNsMPs() != null
            && scoreLine.getNsPair() != null
            && scoreLine.getEwPair() != null){
                completedLines++;
            }
        }
        complete = completedLines == size;
        return complete;
    }
    public List<String> toStringList(){
        List<String> stringList = new ArrayList<>(5);
        StringBuilder textLine = new StringBuilder("NS Pair,EW Pair,Contract,By,Tricks,Score NS,Score EW,MP NS,MP EW");
        stringList.add(textLine.toString());
        for (ScoreLine scoreLine: scoreLines){
            textLine = new StringBuilder();
            textLine.append(scoreLine.getNsPair())
                    .append(",")
                    .append(scoreLine.getEwPair())
                    .append(",")
                    .append((scoreLine.getContract()!=null?scoreLine.getContract().toString():"null"))
                    .append(",")
                    .append(scoreLine.getTricks())
                    .append(",")
                    .append(scoreLine.getNSScore())
                    .append(",")
                    .append(scoreLine.getEWScore())
                    .append(",")
                    .append(scoreLine.getEWScore())
                    .append(",")
                    .append(scoreLine.getNsMPs())
                    .append(",")
                    .append(scoreLine.getEwMPs());
            stringList.add(textLine.toString());

        }
        stringList.add("");
        return stringList;
    }

    public String getCompetionStatus() {
        return isComplete()?"":"Unfinished";
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("score".equals(evt.getPropertyName())) {
            allocateMPs();
        }
    }
}
