package scorebj.model;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TravellerTest {
    Logger logger = org.apache.logging.log4j.LogManager.getLogger();
    Traveller completeTraveller;
    Traveller completeTraveller2;
    Traveller incompleteTraveller;

    BoardId boardId1;
    BoardId boardId2;
    BoardId boardId3;


    @Test
    void isComplete() {
        assertAll("isComplete", () -> {
            assertTrue(completeTraveller.isComplete());
            assertFalse(incompleteTraveller.isComplete());
        });
    }

    @BeforeEach
    void setUp() {
        BoardId boardId1cv = new BoardId(7, 3);
        boardId1cv.setBoard(3); //Set 1, Board 3, EW Vuln

        BoardId boardId1inv = new BoardId(7, 3); //Set 1, Board 1, None Vuln

        boardId1 = new BoardId(7, 3); //Set 1, Board 1

        boardId2 = new BoardId(7, 3); //Set 1, Board 3
        boardId2.setBoard(3);

        boardId3 = new BoardId(7, 3); //Set 2, Board 1
        boardId3.setSet(2);
        boardId3.setBoard(2);

        completeTraveller = new Traveller(boardId1cv, 6);

        List<ScoreLine> list = completeTraveller.getScoreLines();
        for (ScoreLine sl : list) {
            sl.activate(new PropertyChangeListener() {
                String msg;
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    msg = "Property, " + evt.getPropertyName()
                            + " changed in " + evt.getSource().toString();
                    logger.debug(msg);
                    ScoreLine scoreLine = (ScoreLine) evt.getSource();
                    scoreLine.setNsMPs(6);
                    scoreLine.setEwMPs(2);
                }
            });
        }

        list.get(0).setNsPair(7);
        list.get(0).setEwPair(8);
        list.get(0).setContract(new Contract("3H*"));
        list.get(0).setPlayedBy(ScoreLine.Direction.E);
        list.get(0).setTricks(8);
        //NS 200; MP 9-1

        list.get(1).setNsPair(1);
        list.get(1).setEwPair(2);
        list.get(1).setContract(new Contract("3H*"));
        list.get(1).setPlayedBy(ScoreLine.Direction.E);
        list.get(1).setTricks(8);
        //NS 200; MP 9-1

        list.get(2).setNsPair(3);
        list.get(2).setEwPair(4);
        list.get(2).setContract(new Contract("3H"));
        list.get(2).setPlayedBy(ScoreLine.Direction.E);
        list.get(2).setTricks(8);
        //NS 100; MP 6-4

        list.get(3).setNsPair(5);
        list.get(3).setEwPair(6);
        list.get(3).setContract(new Contract("3H*"));
        list.get(3).setPlayedBy(ScoreLine.Direction.E);
        list.get(3).setTricks(9);
        //EW 730 MP: 0-10

        list.get(4).setNsPair(3);
        list.get(4).setEwPair(4);
        list.get(4).setContract(new Contract("3S"));
        list.get(4).setPlayedBy(ScoreLine.Direction.N);
        list.get(4).setTricks(8);
        //EW 50, MP: 4-6

        list.get(5).setNsPair(5);
        list.get(5).setEwPair(6);
        list.get(5).setContract(new Contract("X"));
        //EW 56 MP: 4-4

        incompleteTraveller = new Traveller(boardId1inv, 4);

        list = incompleteTraveller.getScoreLines();
        for (ScoreLine sl : list) {
            sl.activate(new PropertyChangeListener() {
                String msg;
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    msg = "Property, " + evt.getPropertyName()
                            + " changed in incomplete: " + evt.getSource().toString();
                    logger.debug(msg);
                }
            });
        }

        list.get(0).setNsPair(7);
        list.get(0).setEwPair(8);
        list.get(0).setContract(new Contract("3H*"));
        list.get(0).setPlayedBy(ScoreLine.Direction.E);
        list.get(0).setTricks(8);
        //NS 200

        list.get(1).setNsPair(1);
        list.get(1).setEwPair(2);
        list.get(1).setContract(new Contract("3H*"));
        list.get(1).setPlayedBy(ScoreLine.Direction.E);
        list.get(1).setTricks(8);
        //NS 200

        list.get(2).setNsPair(3);
        list.get(2).setEwPair(4);
        list.get(2).setContract(new Contract("3H"));
        list.get(2).setPlayedBy(ScoreLine.Direction.E);
        list.get(2).setTricks(8);
        //NS 100

        list.get(3).setNsPair(5);
        list.get(3).setEwPair(6);
        list.get(3).setContract(new Contract("3H*"));
        list.get(3).setPlayedBy(ScoreLine.Direction.E);
        //list.get(3).setTricks(9);
        //NS 630

        completeTraveller2 = new Traveller(boardId1cv, 6);
        list = completeTraveller2.getScoreLines();
        for (ScoreLine sl : list) {
            sl.activate(new PropertyChangeListener() {
                String msg;
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    msg = "Property, " + evt.getPropertyName()
                            + " changed in " + evt.getSource().toString();
                    logger.debug(msg);
                    ScoreLine scoreLine = (ScoreLine) evt.getSource();
                    scoreLine.setNsMPs(6);
                    scoreLine.setEwMPs(2);
                }
            });
        }

        list.get(0).setNsPair(7);
        list.get(0).setEwPair(8);
        list.get(0).setContract(new Contract("3H*"));
        list.get(0).setPlayedBy(ScoreLine.Direction.E);
        list.get(0).setTricks(8);
        //NS 200; MP 9-1

        list.get(1).setNsPair(1);
        list.get(1).setEwPair(2);
        list.get(1).setContract(new Contract("3H*"));
        list.get(1).setPlayedBy(ScoreLine.Direction.E);
        list.get(1).setTricks(8);
        //NS 200; MP 9-1

        list.get(2).setNsPair(3);
        list.get(2).setEwPair(4);
        list.get(2).setContract(new Contract("X"));
        //NS 100; MP 6-4

        list.get(3).setNsPair(5);
        list.get(3).setEwPair(6);
        list.get(3).setContract(new Contract("3H*"));
        list.get(3).setPlayedBy(ScoreLine.Direction.E);
        list.get(3).setTricks(9);
        //EW 730 MP: 0-10

        list.get(4).setNsPair(3);
        list.get(4).setEwPair(4);
        list.get(4).setContract(new Contract("3S"));
        list.get(4).setPlayedBy(ScoreLine.Direction.N);
        list.get(4).setTricks(8);
        //EW 50, MP: 4-6

        list.get(5).setNsPair(5);
        list.get(5).setEwPair(6);
        list.get(5).setContract(new Contract("X"));
        //EW 56 MP: 4-4
    }

    @AfterEach
    void tearDown() {

        completeTraveller = null;
        incompleteTraveller = null;
        completeTraveller2 = null;

    }

    @Test
    void isEmpty() {
        Traveller emptyTraveller = new Traveller(boardId1, 5);
        assertTrue(emptyTraveller.isEmpty());
        assertFalse(incompleteTraveller.isEmpty());
    }

    @Test
    void copy() {
        BoardId boardId = new BoardId(5,4);
        boardId.setSet(4);
        boardId.setBoard(3);
        Traveller traveller = new Traveller(boardId,5);
        traveller.copy(completeTraveller);

        assertAll("copy", () -> {
            assertEquals("3H*", traveller.getScoreLines().get(1).getContract().toString());
            assertEquals("8", traveller.getScoreLines().get(4).getTricks().toString());
            assertTrue(traveller.isComplete());
            assertEquals(5, traveller.getSize());
        });
    }

    @Test
    void clear() {
        completeTraveller.clear();
        assertNull(completeTraveller.getScoreLine(1).getNSScore());
    }


    @Test
    void getCompletionStatus() {
        assertAll( "Completion Status", () -> {
        assertEquals("Done",completeTraveller.getCompletionStatus());
        assertEquals("Done",completeTraveller2.getCompletionStatus());
            assertEquals("Incomplete", incompleteTraveller.getCompletionStatus());
        });
    }
}