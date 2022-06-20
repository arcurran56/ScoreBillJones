package scorebj.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class BoardIdTest {

    @Test
    void select() {
        BoardId boardId = new BoardId(5, 16);
        BoardId selection = boardId.select(4, 12);

        assertAll(() -> {
            assertEquals(4, selection.getSet());
            assertEquals(12, selection.getBoard());
        });
    }

    @Test
    void next() {
        assertAll("Next", () -> {
            BoardId boardId = new BoardId(5, 16);
            assertEquals(1, boardId.getSet());
            assertEquals(1, boardId.getBoard());

            BoardId next = boardId.next();
            assertEquals(1, next.getSet());
            assertEquals(2, next.getBoard());

            BoardId newBoardId = boardId.select(3, 4);
            next = newBoardId.next();
            assertEquals(3, next.getSet());
            assertEquals(5, next.getBoard());

            newBoardId = boardId.select(2, 16);
            next = newBoardId.next();
            assertEquals(3, next.getSet());
            assertEquals(1, next.getBoard());


            newBoardId = boardId.select(5, 16);
            next = newBoardId.next();
            assertEquals(1, next.getSet());
            assertEquals(1, next.getBoard());
        });
    }

    @Test
    void prev() {
        assertAll("Prev", () -> {
                    BoardId boardId = new BoardId(5, 16);
                    BoardId prevBoard = boardId.prev();
                    assertEquals(5, prevBoard.getSet());
                    assertEquals(16, prevBoard.getBoard());

                    BoardId selection = boardId.select(3, 4);
                    prevBoard = selection.prev();
                    assertEquals(3, prevBoard.getSet());
                    assertEquals(3, prevBoard.getBoard());

                    selection = boardId.select(2, 1);
                    prevBoard = selection.prev();
                    assertEquals(1, prevBoard.getSet());
                    assertEquals(16, prevBoard.getBoard());
                }
        );
    }

    @Test
    void getVulnerability() {
        assertAll("getVulnerability", () -> {
            BoardId boardId = new BoardId(5,16);
            BoardId selection = boardId.select(3,1);
            assertFalse(selection.getVulnerability(ScoreLine.Direction.N));
            assertFalse(selection.getVulnerability(ScoreLine.Direction.W));

            selection = boardId.select(4,3);
            assertFalse(selection.getVulnerability(ScoreLine.Direction.N));
            assertEquals(true,selection.getVulnerability(ScoreLine.Direction.E));

            selection = boardId.select(2,5);
            assertTrue(selection.getVulnerability(ScoreLine.Direction.S));
            assertFalse(selection.getVulnerability(ScoreLine.Direction.W));

            selection = boardId.select(1,8);
            assertFalse(selection.getVulnerability(ScoreLine.Direction.S));
            assertFalse(selection.getVulnerability(ScoreLine.Direction.W));

            selection = boardId.select(5,17);
            assertFalse(selection.getVulnerability(ScoreLine.Direction.N));
            assertFalse(selection.getVulnerability(ScoreLine.Direction.W));


        });
    }

    @Test
    void testClone() {
        BoardId board = new BoardId(5,3);
        board.setSet(2);
        board.setBoard(3);

        BoardId clonedBoard = board.clone();
        BoardId nextBoard = clonedBoard.next();
        boolean sameObj = board == clonedBoard;
        assertAll("Clone test", () -> {
            assertFalse(sameObj);
            assertEquals(2, board.getSet());
            assertEquals(3, board.getBoard());
            assertEquals(2, clonedBoard.getSet());
            assertEquals(3, clonedBoard.getBoard());
            assertEquals(3, nextBoard.getSet());
            assertEquals(1, nextBoard.getBoard());
                }
        );
    }
}