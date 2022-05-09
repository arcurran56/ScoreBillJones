package scorebj.model;

import org.junit.jupiter.api.Test;
import scorebj.traveller.Contract;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TravellerTest {

    @Test
    void scoreHand() {
        Traveller traveller = new Traveller(5);
        List<ScoreLine> list = traveller.getScoreLines();

        list.get(0).setNsPair(7);
        list.get(0).setEwPair(8);
        list.get(0).setContract(new Contract("3H*"));
        list.get(0).setPlayedBy(ScoreLine.Direction.E);
        list.get(0).setTricks(8);
        traveller.scoreHand(0,true);//NS 200; MP 5-1

        list.get(1).setNsPair(1);
        list.get(1).setEwPair(2);
        list.get(1).setContract(new Contract("3H*"));
        list.get(1).setPlayedBy(ScoreLine.Direction.E);
        list.get(1).setTricks(8);
        traveller.scoreHand(1, true);//NS 200; MP 5-1


        list.get(2).setNsPair(3);
        list.get(2).setEwPair(4);
        list.get(2).setContract(new Contract("3H"));
        list.get(2).setPlayedBy(ScoreLine.Direction.E);
        list.get(2).setTricks(8);
        traveller.scoreHand(2, true);//NS 100; 2-4

        list.get(3).setNsPair(5);
        list.get(3).setEwPair(6);
        list.get(3).setContract(new Contract("3H*"));
        list.get(3).setPlayedBy(ScoreLine.Direction.E);
        list.get(3).setTricks(9);
        traveller.scoreHand(3, true);//EW 730; MP 0-6

        assertAll("scoreHand", () -> {
            assertNull(list.get(4).getNSScore());
            assertNull(list.get(4).getNsMPs());
            assertNull(list.get(4).getEwMPs());

            assertEquals(200, list.get(1).getNSScore());
            assertEquals(730, list.get(3).getEWScore());
            assertEquals(2, list.get(2).getNsMPs());
            assertEquals(4, list.get(2).getEwMPs());
            assertEquals(0, list.get(3).getNsMPs());
            assertEquals(6, list.get(3).getEwMPs());
        });

    }

    @Test
    void isComplete() {
        Traveller traveller = new Traveller(5);
        List<ScoreLine> list = traveller.getScoreLines();

        list.get(0).setNsPair(7);
        list.get(0).setEwPair(8);
        list.get(0).setContract(new Contract("3H*"));
        list.get(0).setPlayedBy(ScoreLine.Direction.E);
        list.get(0).setTricks(8);
        traveller.scoreHand(0,true);//NS 200; MP 5-1

        list.get(1).setNsPair(1);
        list.get(1).setEwPair(2);
        list.get(1).setContract(new Contract("3H*"));
        list.get(1).setPlayedBy(ScoreLine.Direction.E);
        list.get(1).setTricks(8);
        traveller.scoreHand(1, true);//NS 200; MP 5-1


        list.get(2).setNsPair(3);
        list.get(2).setEwPair(4);
        list.get(2).setContract(new Contract("3H"));
        list.get(2).setPlayedBy(ScoreLine.Direction.E);
        list.get(2).setTricks(8);
        traveller.scoreHand(2, true);//NS 100; 2-4

        list.get(3).setNsPair(5);
        list.get(3).setEwPair(6);
        list.get(3).setContract(new Contract("3H*"));
        list.get(3).setPlayedBy(ScoreLine.Direction.E);
        list.get(3).setTricks(9);
        traveller.scoreHand(3, true);//EW 730; MP 0-6

        list.get(4).setNsPair(3);
        list.get(4).setEwPair(4);
        list.get(4).setContract(new Contract("3H"));
        list.get(4).setPlayedBy(ScoreLine.Direction.E);
        list.get(4).setTricks(8);
        traveller.scoreHand(4, true);//NS 100; 2-4
        assertTrue(traveller.isComplete());
    }
}