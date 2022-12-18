package scorebj.model;


import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class Traveller {

    @XStreamOmitField
    private static final Logger logger = LogManager.getLogger();

    public int getSize() {
        return size;
    }

    private final int size;

    private final BoardId boardId;

    public BoardId getBoardId() {
        return boardId;
    }

     public Traveller(){
        this(new BoardId(1,1),1);
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
        logger.debug("Copying Traveller for board " + traveller.getBoardId());
        this.scoreLines = traveller.getScoreLines();
    }

    public void addAll(List<ScoreLine> scoreLines) {
        this.scoreLines.addAll(scoreLines);
        int newSize = scoreLines.size();
        if ( newSize != size) {
            logger.warn("Size mismatch: " + newSize + " vs " + size);
        }
    }

    public void clear() {
        logger.debug("Clearing...");
        scoreLines.clear();
        for (int i = 0; i < size; i++) {
            scoreLines.add(new ScoreLine());
        }
    }
    public boolean isComplete() {
        logger.debug("...checking complete...");
        boolean complete = true;
        ScoreLine scoreLine;
        for (int i = 0; i < size; i++) {
            scoreLine = scoreLines.get(i);
            if (scoreLine != null) {
                if (!scoreLine.isComplete()) {
                    complete = false;
                }
            }
            else {
                complete = false;
            }
        }
        return complete;
    }

    public boolean isEmpty() {
        logger.debug("...checking empty...");
        boolean empty = true;
        for (ScoreLine scoreLine: scoreLines){
            if (!scoreLine.isEmpty()){
                empty = false;
            }
        }
        return empty;
    }

    public List<String> toStringList(){
        List<String> stringList = new ArrayList<>(5);
        StringBuilder textLine = new StringBuilder("NS Pair,EW Pair,Contract,By,Tricks,Score NS,Score EW,MP NS,MP EW,NS MP O/R,EW MP O/R");
        stringList.add(textLine.toString());
        for (ScoreLine scoreLine: scoreLines){
            textLine = new StringBuilder();
            textLine.append(scoreLine.getNsPair())
                    .append(",")
                    .append(scoreLine.getEwPair())
                    .append(",")
                    .append((scoreLine.getContract()!=null?scoreLine.getContract().toString():"null"))
                    .append(",")
                    .append(scoreLine.getPlayedBy())
                    .append(",")
                    .append(scoreLine.getTricks())
                    .append(",")
                    .append(scoreLine.getNSScore())
                    .append(",")
                    .append(scoreLine.getEWScore())
                    .append(",")
                    .append(scoreLine.getNsMPs())
                    .append(",")
                    .append(scoreLine.getEwMPs())
                    .append(",")
                    .append(scoreLine.getNsOverride())
                    .append(",")
                    .append(scoreLine.getEwOverride());
            stringList.add(textLine.toString());

        }
        stringList.add("");
        return stringList;
    }
    public String getCompletionStatus() {
        return isComplete()?"Done":"Incomplete";
    }

    public String toString(){
        StringBuilder builder = new StringBuilder("Traveller: ")
                .append("Board: ")
                .append(boardId)
                .append("; size: ")
                .append(size);
        return builder.toString();
    }
}
