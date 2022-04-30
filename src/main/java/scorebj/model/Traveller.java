package scorebj.model;

public class Traveller {

    public Traveller() {

        scoreLines = new ScoreLine[5];
        for (int i = 0; i < 5; i++) {
            scoreLines[i] = new ScoreLine();
        }
    }

    public ScoreLine[] getScoreLines() {
        return scoreLines;
    }

    public void setScoreLines(ScoreLine[] scoreLines) {
        this.scoreLines = scoreLines;
    }

    public ScoreLine getScoreLine(int line) {
        return scoreLines[line];
    }

    private ScoreLine[] scoreLines = new ScoreLine[5];

    public void copy(Traveller traveller) {
        this.scoreLines = traveller.getScoreLines();

    }
}
