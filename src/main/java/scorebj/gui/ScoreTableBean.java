package scorebj.gui;

import scorebj.model.BoardId;

public class ScoreTableBean {
    public BoardId getBoardId() {
        return boardId;
    }

    public void setBoardId(BoardId boardId) {
        this.boardId = boardId;
    }

    private BoardId boardId = new BoardId(5,16);

    public String getSet() {
        return Integer.valueOf(boardId.getSet()).toString();
    }

    public void setSet(String set) {
        try {
            int parsedSet = Integer.parseUnsignedInt(set);
            this.boardId.setSet(parsedSet);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public String getBoard() {
        return Integer.valueOf(boardId.getBoard()).toString();
    }

    public void setBoard(String board) {
        try {
            int parsedBoard = Integer.parseUnsignedInt(board);
            this.boardId.setBoard(parsedBoard);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public ScoreTableBean() {
    }
    public BoardId next(){
        return boardId.next();
    }
    public BoardId prev(){
        return boardId.prev();
    }
}