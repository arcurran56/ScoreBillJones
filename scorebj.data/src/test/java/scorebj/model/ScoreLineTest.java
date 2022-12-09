package scorebj.model;

import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;

class ScoreLineTest {
    PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {

        }
    };

    @Test
    void scoreHand1() {
        ScoreLine scoreLine = new ScoreLine();
        scoreLine.activate(propertyChangeListener);
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
        scoreLine.activate(propertyChangeListener);
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
        scoreLine.activate(propertyChangeListener);
        scoreLine.setVulnerability(BoardId.Vulnerability.NS);
        scoreLine.setNsPair(2);
        scoreLine.setEwPair(3);
        scoreLine.setContract(new Contract("3H"));
        scoreLine.setPlayedBy(ScoreLine.Direction.W);
        scoreLine.setTricks(8);
        assertEquals(50, scoreLine.getNSScore());
        assertEquals(0, scoreLine.getEWScore());

    }

    @Test
    void isEmpty() {
        ScoreLine scoreLine = new ScoreLine();
        scoreLine.activate(propertyChangeListener);
        scoreLine.setVulnerability(BoardId.Vulnerability.NS);
        scoreLine.setNsPair(2);
        scoreLine.setEwPair(3);
        scoreLine.setContract(new Contract("3H"));
        scoreLine.setPlayedBy(ScoreLine.Direction.W);
        scoreLine.setTricks(8);
        boolean resultNotEmpty = scoreLine.isEmpty();


        scoreLine = new ScoreLine();
        boolean resultEmpty = scoreLine.isEmpty();

        assertAll("isEmpty",
                () -> {
                    assertFalse(resultNotEmpty);
                    assertTrue(resultEmpty);
                });
    }

    @Test
    void activate() {
    }

    @Test
    void isComplete() {
        ScoreLine scoreLine = new ScoreLine();
        scoreLine.activate(propertyChangeListener);
        scoreLine.setVulnerability(BoardId.Vulnerability.NS);
        scoreLine.setNsPair(2);
        scoreLine.setEwPair(3);
        scoreLine.setContract(new Contract("3H"));
        scoreLine.setPlayedBy(ScoreLine.Direction.W);
        scoreLine.setTricks(8);
        boolean resultNotComplete = scoreLine.isComplete();
        scoreLine.setNsMPs(6);
        scoreLine.setEwMPs(2);
        boolean resultComplete = scoreLine.isComplete();

        assertAll("isComplete", () -> {
            assertEquals(50, scoreLine.getNSScore());
            assertFalse(resultNotComplete);
            assertTrue((resultComplete));
        });

        scoreLine.setTricks(null);
        boolean resultNotComplete2 = scoreLine.isComplete();

        assertFalse(resultNotComplete2);
    }

    @Test
    void blankScores() {
        ScoreLine scoreLine = new ScoreLine();
        scoreLine.activate(propertyChangeListener);
        scoreLine.setVulnerability(BoardId.Vulnerability.NONE);
        scoreLine.setNsPair(2);
        scoreLine.setEwPair(3);
        scoreLine.setContract(new Contract("3H"));
        scoreLine.setPlayedBy(ScoreLine.Direction.E);
        scoreLine.setTricks(11);
        scoreLine.setNsMPs(2);
        scoreLine.setEwMPs(6);

        assertAll(() -> {
            assertEquals(0, scoreLine.getNSScore());
            assertEquals(200, scoreLine.getEWScore());
            assertEquals(6, scoreLine.getEwMPs());
        });
        scoreLine.setTricks(null);

        assertAll(() -> {
            assertNull(scoreLine.getNSScore());
            assertNull(scoreLine.getEWScore());
            assertNull(scoreLine.getEwMPs());
        });

    }
}