package scorebj.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scorebj.traveller.Contract;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TravellerTest {
    Traveller completeTraveller;
    Traveller incompleteTraveller;

    BoardId boardId1;
    BoardId boardId2;
    BoardId boardId3;


    @Test
    void scoreHand() {
        incompleteTraveller.scoreHand(1, true);//NS 200; 4-0
        incompleteTraveller.scoreHand(2, true);//NS 100; 2-2
        incompleteTraveller.scoreHand(3, true);//EW 730; MP 0-4
        List<ScoreLine> list = incompleteTraveller.getScoreLines();

        assertAll("scoreHand", () -> {
            assertNull(list.get(4).getNSScore());
            assertNull(list.get(4).getNsMPs());
            assertNull(list.get(4).getEwMPs());

            assertEquals(200, list.get(1).getNSScore());
            assertEquals(730, list.get(3).getEWScore());
            assertEquals(2, list.get(2).getNsMPs());
            assertEquals(2, list.get(2).getEwMPs());
            assertEquals(0, list.get(3).getNsMPs());
            assertEquals(4, list.get(3).getEwMPs());
        });

    }

    @Test
    void isComplete() {
        //List<ScoreLine> list = completeTraveller.getScoreLines();

/*        list.get(0).setNsPair(7);
        list.get(0).setEwPair(8);
        list.get(0).setContract(new Contract("3H*"));
        list.get(0).setPlayedBy(ScoreLine.Direction.E);
        list.get(0).setTricks(8);
 */
        completeTraveller.scoreHand(0,true);//NS 200; MP 5-1

/*        list.get(1).setNsPair(1);
        list.get(1).setEwPair(2);
        list.get(1).setContract(new Contract("3H*"));
        list.get(1).setPlayedBy(ScoreLine.Direction.E);
        list.get(1).setTricks(8);
 */
        completeTraveller.scoreHand(1, true);//NS 200; MP 5-1


/*        list.get(2).setNsPair(3);
        list.get(2).setEwPair(4);
        list.get(2).setContract(new Contract("3H"));
        list.get(2).setPlayedBy(ScoreLine.Direction.E);
        list.get(2).setTricks(8);*/
        completeTraveller.scoreHand(2, true);//NS 100; 2-4

/*        list.get(3).setNsPair(5);
        list.get(3).setEwPair(6);
        list.get(3).setContract(new Contract("3H*"));
        list.get(3).setPlayedBy(ScoreLine.Direction.E);
        list.get(3).setTricks(9);*/
        completeTraveller.scoreHand(3, true);//EW 730; MP 0-6

/*        list.get(4).setNsPair(3);
        list.get(4).setEwPair(4);
        list.get(4).setContract(new Contract("3H"));
        list.get(4).setPlayedBy(ScoreLine.Direction.E);
        list.get(4).setTricks(8);*/
        completeTraveller.scoreHand(4, true);//NS 100; 2-4
        assertTrue(completeTraveller.isComplete());

        incompleteTraveller.scoreHand(0,true);
        assertFalse(incompleteTraveller.isComplete());
    }

    @Test
    void generatePrefilled() {
        Traveller notPre1 = incompleteTraveller.generatePrefilled(boardId1);

        Traveller pre = completeTraveller.generatePrefilled(boardId2);


        Traveller notPre2 = completeTraveller.generatePrefilled(boardId3);

        List<ScoreLine> scoreLines1;
        List<ScoreLine> scoreLines2;
        List<ScoreLine> scoreLines3;


        scoreLines1 = notPre1.getScoreLines();
        scoreLines2 = pre.getScoreLines();
        scoreLines3 = notPre2.getScoreLines();

        assertAll( "generatePrefilled", () -> {
                    assertNull(scoreLines1.get(0).getNsPair());
                    assertEquals(1, scoreLines2.get(1).getNsPair());
                    assertEquals(4, scoreLines2.get(2).getEwPair());
                    assertNull(scoreLines3.get(0).getNsPair());
                });

    }

    @BeforeEach
    void setUp() {
        BoardId boardId1c = new BoardId(7,3); //Set 1, Board 1
        BoardId boardId1i = new BoardId(7,3); //Set 1, Board 1
        boardId1 = new BoardId(7,3); //Set 1, Board 1
        boardId2 = new BoardId(7,3); //Set 1, Board 3
        boardId2.setBoard(3);
        boardId3 = new BoardId(7,3); //Set 2, Board 1
        boardId3.setSet(2);
        boardId3.setBoard(1);

        completeTraveller = new Traveller(5);
        completeTraveller.setBoardId(boardId1c);
        List<ScoreLine> list = completeTraveller.getScoreLines();

        list.get(0).setNsPair(7);
        list.get(0).setEwPair(8);
        list.get(0).setContract(new Contract("3H*"));
        list.get(0).setPlayedBy(ScoreLine.Direction.E);
        list.get(0).setTricks(8);
        //completeTraveller.scoreHand(0,true);//NS 200; MP 5-1

        list.get(1).setNsPair(1);
        list.get(1).setEwPair(2);
        list.get(1).setContract(new Contract("3H*"));
        list.get(1).setPlayedBy(ScoreLine.Direction.E);
        list.get(1).setTricks(8);
        //completeTraveller.scoreHand(1, true);//NS 200; MP 5-1

        list.get(2).setNsPair(3);
        list.get(2).setEwPair(4);
        list.get(2).setContract(new Contract("3H"));
        list.get(2).setPlayedBy(ScoreLine.Direction.E);
        list.get(2).setTricks(8);
        //completeTraveller.scoreHand(2, true);//NS 100; 2-4

        list.get(3).setNsPair(5);
        list.get(3).setEwPair(6);
        list.get(3).setContract(new Contract("3H*"));
        list.get(3).setPlayedBy(ScoreLine.Direction.E);
        list.get(3).setTricks(9);

        list.get(4).setNsPair(3);
        list.get(4).setEwPair(4);
        list.get(4).setContract(new Contract("3H"));
        list.get(4).setPlayedBy(ScoreLine.Direction.E);
        list.get(4).setTricks(8);

        for(int i=0; i<5; i++) {
            completeTraveller.scoreHand(i, true);
        }

        incompleteTraveller = new Traveller(5);
        incompleteTraveller.setBoardId(boardId1i);

        list = incompleteTraveller.getScoreLines();
        list.get(0).setNsPair(7);
        list.get(0).setEwPair(8);
        list.get(0).setContract(new Contract("3H*"));
        list.get(0).setPlayedBy(ScoreLine.Direction.E);
        list.get(0).setTricks(8);
        //completeTraveller.scoreHand(0,true);//NS 200; MP 5-1

        list.get(1).setNsPair(1);
        list.get(1).setEwPair(2);
        list.get(1).setContract(new Contract("3H*"));
        list.get(1).setPlayedBy(ScoreLine.Direction.E);
        list.get(1).setTricks(8);

        list.get(2).setNsPair(3);
        list.get(2).setEwPair(4);
        list.get(2).setContract(new Contract("3H"));
        list.get(2).setPlayedBy(ScoreLine.Direction.E);
        list.get(2).setTricks(8);
        //completeTraveller.scoreHand(2, true);//NS 100; 2-4

        list.get(3).setNsPair(5);
        list.get(3).setEwPair(6);
        list.get(3).setContract(new Contract("3H*"));
        list.get(3).setPlayedBy(ScoreLine.Direction.E);
        list.get(3).setTricks(9);

    }

    @AfterEach
    void tearDown() {

        completeTraveller = null;
        incompleteTraveller = null;
    }

    @Test
    void isEmpty() {
        Traveller emptyTraveller = new Traveller(5);
        assertTrue(emptyTraveller.isEmpty());
        assertFalse(incompleteTraveller.isEmpty());
    }
}