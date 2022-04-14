package scorebj.gui;

import scorebj.model.BoardId;

public class ScoreTableBean {
    private BoardId boardId = new BoardId(5,16);

    private Integer set;

    public String getSet() {
        return Integer.valueOf(boardId.getSet()).toString();
    }

    public void setSet(String set) {
        this.boardId.setSet(Integer.parseUnsignedInt(set));
    }

    public String getBoard() {
        return Integer.valueOf(boardId.getBoard()).toString();
    }

    public void setBoard(String board) {
        this.boardId.setSet(Integer.parseUnsignedInt(board));
    }

    private Integer board;
    public ScoreTableBean() {
    }
    public void next(){
        boardId.next();
    }
    public void prev(){
        boardId.prev();
    }
}