package scorebj.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreLineTest {

    @Test
    void scoreHand1() {
        ScoreLine scoreLine = new ScoreLine();
        scoreLine.setVulnerability(BoardId.Vulnerability.NONE);
        scoreLine.setNsPair(2);
        scoreLine.setEwPair(3);
        scoreLine.setContract(new Contract("3H"));
        scoreLine.setPlayedBy(ScoreLine.Direction.E);
        scoreLine.setTricks(11);
        assertEquals(0, scoreLine.getNSScore());
        assertEquals(200, scoreLine.getEWScore());

    }
    @Test
    void scoreHand2() {
        ScoreLine scoreLine = new ScoreLine();
        scoreLine.setVulnerability(BoardId.Vulnerability.NS);
        scoreLine.setNsPair(2);
        scoreLine.setEwPair(3);
        scoreLine.setContract(new Contract("4H*"));
        scoreLine.setPlayedBy(ScoreLine.Direction.N);
        scoreLine.setTricks(11);
        assertEquals(990, scoreLine.getNSScore());
        assertEquals(0, scoreLine.getEWScore());

    }
    @Test
    void scoreHand3() {
        ScoreLine scoreLine = new ScoreLine();
        scoreLine.setVulnerability(BoardId.Vulnerability.NS);
        scoreLine.setNsPair(2);
        scoreLine.setEwPair(3);
        scoreLine.setContract(new Contract("3H"));
        scoreLine.setPlayedBy(ScoreLine.Direction.W);
        scoreLine.setTricks(8);
        assertEquals(50, scoreLine.getNSScore());
        assertEquals(0, scoreLine.getEWScore());

    }
}