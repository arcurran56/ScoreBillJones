package scorebj.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


class BoardIdTest {

    @Test
    void select() {
        BoardId boardId = new BoardId(5, 16);
        boardId.select(4, 12);

        assertAll(() -> {
            assertEquals(4, boardId.getSet());
            assertEquals(12, boardId.getBoard());
        });
    }

    @Test
    void next() {
        assertAll("Next", () -> {
            BoardId boardId = new BoardId(5, 16);
            assertEquals(1, boardId.getSet());
            assertEquals(1, boardId.getBoard());

            boardId.next();
            assertEquals(1, boardId.getSet());
            assertEquals(2, boardId.getBoard());

            boardId.select(3, 4);
            boardId.next();
            assertEquals(3, boardId.getSet());
            assertEquals(5, boardId.getBoard());

            boardId.select(2, 16);
            boardId.next();
            assertEquals(3, boardId.getSet());
            assertEquals(1, boardId.getBoard());


            boardId.select(5, 16);
            boardId.next();
            assertEquals(1, boardId.getSet());
            assertEquals(1, boardId.getBoard());
        });
    }

    @Test
    void prev() {
        assertAll("Prev", () -> {
                    BoardId boardId = new BoardId(5, 16);
                    boardId.prev();
                    assertEquals(5, boardId.getSet());
                    assertEquals(16, boardId.getBoard());

                    boardId.select(3, 4);
                    boardId.prev();
                    assertEquals(3, boardId.getSet());
                    assertEquals(3, boardId.getBoard());

                    boardId.select(2, 1);
                    boardId.prev();
                    assertEquals(1, boardId.getSet());
                    assertEquals(16, boardId.getBoard());
                }
        );
    }

    @Test
    void getVulnerability() {
        assertAll("getVulnerability", () -> {
            BoardId boardId = new BoardId(5,16);
            boardId.select(3,1);
            assertFalse(boardId.getVulnerability(ScoreLine.Direction.N));
            assertFalse(boardId.getVulnerability(ScoreLine.Direction.W));

            boardId.select(4,3);
            assertFalse(boardId.getVulnerability(ScoreLine.Direction.N));
            assertEquals(true,boardId.getVulnerability(ScoreLine.Direction.E));

            boardId.select(2,5);
            assertEquals(true,boardId.getVulnerability(ScoreLine.Direction.S));
            assertFalse(boardId.getVulnerability(ScoreLine.Direction.W));

            boardId.select(1,8);
            assertFalse(boardId.getVulnerability(ScoreLine.Direction.S));
            assertFalse(boardId.getVulnerability(ScoreLine.Direction.W));

            boardId.select(5,17);
            assertFalse(boardId.getVulnerability(ScoreLine.Direction.N));
            assertFalse(boardId.getVulnerability(ScoreLine.Direction.W));


        });
    }
}