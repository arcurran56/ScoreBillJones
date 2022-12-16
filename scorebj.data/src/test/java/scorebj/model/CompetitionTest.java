package scorebj.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CompetitionTest {

    private Competition competition = new Competition();

    @BeforeEach
    void setUp() {
        competition.setCompetitionName("CompName");
        competition.setNoSets(2);
        competition.setNoBoardsPerSet(3);
        competition.setNoPairs(5);

        competition.initialise();

        String[] pairingValues = {"Pair1", "Pair2", "Pair3", "Pair4", "Pair5"};
        List<String> pairings = new ArrayList<>(List.of(pairingValues));
        competition.setPairings(pairings);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCompetitionName() {
        assertEquals("CompName", competition.getCompetitionName());
    }

    @Test
    void setCompetitionName() {
    }

    @Test
    void getNoSets() {
    }

    @Test
    void setNoSets() {
    }

    @Test
    void getNoBoardsPerSet() {
    }

    @Test
    void setNoBoardsPerSet() {
    }

    @Test
    void getPairings() {
        List<String> pairings = competition.getPairings();
        assertEquals(5,pairings.size());
        assertEquals("Pair5", pairings.get(4));
    }

    @Test
    void setPairings() {
    }

    @Test
    void getTravellers() {
    }

    @Test
    void getTraveller() {
    }

    @Test
    void travellersToText() {
    }

    @Test
    void getNoPairs() {
    }

    @Test
    void setNoPairs() {
    }

    @Test
    void initialise() {
    }

    @Test
    void getProgress() {
    }

    @Test
    void testToString() {
    }
}