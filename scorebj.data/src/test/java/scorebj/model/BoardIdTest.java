package scorebj.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


class BoardIdTest {

    @Test
    void select() {
        assertAll(() -> {
            BoardId boardId = new BoardId(5, 16);
            BoardId selection = boardId.select(4, 12);
            assertEquals(4, selection.getSet());
            assertEquals(12, selection.getBoard());

            boardId = new BoardId(0, 0);
            selection = boardId.select(0, 0);
            assertEquals(0, selection.getSet());
            assertEquals(0, selection.getBoard());
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

            boardId = new BoardId(0, 0);
            assertEquals(0, boardId.getSet());
            assertEquals(0, boardId.getBoard());

            next = boardId.next();
            assertEquals(0, next.getSet());
            assertEquals(0, next.getBoard());

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
            BoardId boardId = new BoardId(5, 16);
            BoardId selection = boardId.select(3, 1);
            assertEquals(BoardId.Vulnerability.NONE, selection.getVulnerabilityStatus());

            selection = boardId.select(4, 3);
            assertEquals(BoardId.Vulnerability.EW, selection.getVulnerabilityStatus());

            selection = boardId.select(2, 5);
            assertEquals(BoardId.Vulnerability.NS, selection.getVulnerabilityStatus());

            selection = boardId.select(1, 8);
            assertEquals(BoardId.Vulnerability.NONE, selection.getVulnerabilityStatus());

            selection = boardId.select(3, 7);
            assertEquals(BoardId.Vulnerability.ALL, selection.getVulnerabilityStatus());

            selection = boardId.select(5, 17);
            assertEquals(BoardId.Vulnerability.NONE, selection.getVulnerabilityStatus());
            assertEquals("NONE", selection.getVulnerabilityStatus().toString());

            boardId = new BoardId(0, 0);
            selection = boardId.select(0, 0);
            assertEquals(BoardId.Vulnerability.NONE, selection.getVulnerabilityStatus());


        });
    }

    @Test
    void testClone() {
        BoardId board = new BoardId(5, 3);
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

    @Test
    void cycleVulnerabilities() {
        boolean[] expectationN = {false, true, false, true,
                true, false, true, false,
                false, true, false, true,
                true, false, true, false,
                false, true, false, true
        };
        boolean[] expectationE = {false, false, true, true,
                false, true, true, false,
                true, true, false, false,
                true, false, false, true,
                false, false, true, true,
        };

        BoardId boardId = new BoardId(5, 16);
        boardId.setSet(4);
        boardId.setBoard(1);
        BoardId nextBoardId = boardId;
        for (int i = 1; i <= 20; i++) {
            boardId = nextBoardId;
            assertEquals((i-1)%16+1,boardId.getBoard(),"Iteration " + i);
            assertEquals(expectationN[i-1], isNSvulnerable(boardId.getVulnerabilityStatus()), i +"N");
            assertEquals(expectationE[i-1], isEWvulnerable(boardId.getVulnerabilityStatus()), i +"E");
            nextBoardId = boardId.next();
        }
    }
    private boolean isNSvulnerable(BoardId.Vulnerability v){
        return BoardId.Vulnerability.NS.equals(v)||BoardId.Vulnerability.ALL.equals(v);

        }
    private boolean isEWvulnerable(BoardId.Vulnerability v){
        return BoardId.Vulnerability.EW.equals(v)||BoardId.Vulnerability.ALL.equals(v);

    }
}
