package scorebj.model;

import org.junit.jupiter.api.Test;
import scorebj.traveller.Contract;

import static org.junit.jupiter.api.Assertions.*;

class ScoreLineTest {

    @Test
    void scoreHand1() {
        ScoreLine scoreLine = new ScoreLine();
        scoreLine.setNsPair(2);
        scoreLine.setEwPair(3);
        scoreLine.setContract(new Contract("3H"));
        scoreLine.setPlayedBy(ScoreLine.Direction.E);
        scoreLine.setTricks(11);
        scoreLine.scoreHand(false);
        assertEquals(0, scoreLine.getNSScore());
        assertEquals(200, scoreLine.getEWScore());

    }
    @Test
    void scoreHand2() {
        ScoreLine scoreLine = new ScoreLine();
        scoreLine.setNsPair(2);
        scoreLine.setEwPair(3);
        scoreLine.setContract(new Contract("4H*"));
        scoreLine.setPlayedBy(ScoreLine.Direction.N);
        scoreLine.setTricks(11);
        scoreLine.scoreHand(true);
        assertEquals(990, scoreLine.getNSScore());
        assertEquals(0, scoreLine.getEWScore());

    }
    @Test
    void scoreHand3() {
        ScoreLine scoreLine = new ScoreLine();
        scoreLine.setNsPair(2);
        scoreLine.setEwPair(3);
        scoreLine.setContract(new Contract("3H"));
        scoreLine.setPlayedBy(ScoreLine.Direction.W);
        scoreLine.setTricks(8);
        scoreLine.scoreHand(false);
        assertEquals(50, scoreLine.getNSScore());
        assertEquals(0, scoreLine.getEWScore());

    }
}