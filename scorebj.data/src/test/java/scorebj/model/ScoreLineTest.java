package scorebj.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;

class ScoreLineTest {

    private class ScoreLineChangeListener implements PropertyChangeListener {
        public boolean isChangeDetected() {
            return changeDetected;
        }

        private boolean changeDetected = false;

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("score".equals(evt.getPropertyName())) {
                changeDetected = true;
            }
        }
    };
    private Logger logger = LogManager.getLogger();

    @Test
    void scoreHand1() {
        ScoreLine scoreLine = new ScoreLine();
        ScoreLineChangeListener scl = new ScoreLineChangeListener();
        scoreLine.activate(scl);
        scoreLine.setVulnerability(BoardId.Vulnerability.NONE);
        scoreLine.setNsPair(2);
        scoreLine.setEwPair(3);
        scoreLine.setContract(new Contract("3H"));
        scoreLine.setPlayedBy(ScoreLine.Direction.E);
        scoreLine.setTricks(11);

        assertEquals(0, scoreLine.getNSScore());
        assertEquals(200, scoreLine.getEWScore());
        assertTrue( scl.isChangeDetected() );

    }

    @Test
    void scoreHand2() {
        ScoreLine scoreLine = new ScoreLine();
        ScoreLineChangeListener scl = new ScoreLineChangeListener();
        scoreLine.activate(scl);
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
        ScoreLineChangeListener scl = new ScoreLineChangeListener();
        scoreLine.activate(scl);
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
        ScoreLineChangeListener scl = new ScoreLineChangeListener();
        scoreLine.activate(scl);
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
    void isComplete_normal() {
        ScoreLine scoreLine = new ScoreLine();
        ScoreLineChangeListener scl = new ScoreLineChangeListener();
        scoreLine.activate(scl);
        scoreLine.setVulnerability(BoardId.Vulnerability.NS);
        scoreLine.setNsPair(2);
        scoreLine.setEwPair(3);
        scoreLine.setContract(new Contract("3H"));
        scoreLine.setPlayedBy(ScoreLine.Direction.W);
        scoreLine.setTricks(8);
        boolean resultComplete = scoreLine.isComplete();



        assertAll("isComplete", () -> {
            assertEquals(50, scoreLine.getNSScore());
            assertTrue((resultComplete));
        });

        scoreLine.setTricks(null);
        boolean resultNotComplete = scoreLine.isComplete();

        assertFalse(resultNotComplete);
    }
    void isComplete_AllPass() {
        ScoreLine scoreLine = new ScoreLine();
        ScoreLineChangeListener scl = new ScoreLineChangeListener();
        scoreLine.activate(scl);
        scoreLine.setVulnerability(BoardId.Vulnerability.EW);
        scoreLine.setNsPair(2);
        scoreLine.setEwPair(3);
        scoreLine.setContract(new Contract("AP"));
        boolean resultComplete = scoreLine.isComplete();

        assertAll("isComplete", () -> {
            assertEquals(50, scoreLine.getNSScore());
            assertTrue((resultComplete));
        });

        scoreLine.setTricks(null);
        boolean resultNotComplete2 = scoreLine.isComplete();

        assertFalse(resultNotComplete2);
    }
    @Test
    void blankScores() {
        ScoreLine scoreLine = new ScoreLine();
        ScoreLineChangeListener scl = new ScoreLineChangeListener();
        scoreLine.activate(scl);
        scoreLine.setVulnerability(BoardId.Vulnerability.NONE);
        scoreLine.setNsPair(2);
        scoreLine.setEwPair(3);
        scoreLine.setContract(new Contract("3H"));
        scoreLine.setPlayedBy(ScoreLine.Direction.E);
        scoreLine.setTricks(11);


        assertAll(() -> {
            assertEquals(0, scoreLine.getNSScore());
            assertEquals(200, scoreLine.getEWScore());
        });
        scoreLine.setTricks(null);

        assertAll(() -> {
            assertNull(scoreLine.getNSScore());
            assertNull(scoreLine.getEWScore());
        });

    }

    @Test
    void allPass() {
        ScoreLine scoreLine = new ScoreLine();
        ScoreLineChangeListener scl = new ScoreLineChangeListener();
        scoreLine.activate(scl);
        scoreLine.setVulnerability(BoardId.Vulnerability.NONE);
        scoreLine.setNsPair(2);
        scoreLine.setEwPair(3);
        scoreLine.setContract(new Contract("AP"));
        scoreLine.setPlayedBy(ScoreLine.Direction.E);
        scoreLine.setTricks(11);
        scoreLine.setNsMPs(2);
        scoreLine.setEwMPs(6);

        logger.debug(scoreLine);
        assertEquals(0,scoreLine.getNSScore());
        assertEquals(0,scoreLine.getEWScore());
        assertEquals(6,scoreLine.getEwMPs());


    }

    @Test
    void skipped() {
        ScoreLine scoreLine = new ScoreLine();
        ScoreLineChangeListener scl = new ScoreLineChangeListener();
        scoreLine.activate(scl);
        scoreLine.setVulnerability(BoardId.Vulnerability.NONE);
        scoreLine.setNsPair(2);
        scoreLine.setEwPair(3);
        scoreLine.setContract(new Contract("X"));
        scoreLine.setTricks(11);

        logger.debug(scoreLine);
        assertEquals(null, scoreLine.getNSScore());
        assertEquals(null, scoreLine.getEWScore());
        assertTrue(scoreLine.isSkipped());


    }

    @Test
    void clear() {
        ScoreLine scoreLine = new ScoreLine();
        ScoreLineChangeListener scl = new ScoreLineChangeListener();
        scoreLine.activate(scl);
        scoreLine.setVulnerability(BoardId.Vulnerability.NS);
        scoreLine.setNsPair(2);
        scoreLine.setEwPair(3);
        scoreLine.setContract(new Contract("3H"));
        scoreLine.setPlayedBy(ScoreLine.Direction.W);
        scoreLine.setTricks(8);

        scoreLine.clear();

        assertAll(() -> {
            assertNull(scoreLine.getNSScore());
            assertNull(scoreLine.getEWScore());
            assertNull(scoreLine.getEwMPs());
        });
    }
}