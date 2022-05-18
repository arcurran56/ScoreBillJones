package scorebj.model;

import java.util.ArrayList;
import java.util.List;

public class Traveller {

    private int size;

    public Traveller() {
    }

    public Traveller(int size) {
        this.size = size;
        for (int i = 0; i < size; i++) {
            scoreLines.add(new ScoreLine());
        }
    }

    public List<ScoreLine> getScoreLines() {
        return scoreLines;
    }

    public void setScoreLines(List<ScoreLine> scoreLines) {
        this.scoreLines = scoreLines;
    }

    public ScoreLine getScoreLine(int line) {
        return scoreLines.get(line);
    }

    private List<ScoreLine> scoreLines = new ArrayList<>(5);

    public void copy(Traveller traveller) {
        this.scoreLines = traveller.getScoreLines();

    }

    public void addAll(List<ScoreLine> scoreLines) {
        this.scoreLines.addAll(scoreLines);
    }

    public void clear() {
        scoreLines.clear();
        for (int i = 0; i < size; i++) {
            scoreLines.add(new ScoreLine());
        }
    }

    public void scoreHand(int line, boolean vulnerable) {
        ScoreLine scoreLine = getScoreLine(line);
        scoreLine.scoreHand(vulnerable);
        allocateMPs();
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
    public boolean isComplete(){
        int completedLines = 0;
        boolean complete;
        for (ScoreLine scoreLine: scoreLines){
            if (scoreLine.getNsMPs() != null){
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
}
