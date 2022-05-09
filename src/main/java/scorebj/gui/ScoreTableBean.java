package scorebj.gui;

import scorebj.model.BoardId;

public class ScoreTableBean {
    private String competitionName;

    public BoardId getBoardId() {
        return boardId;
    }

    public void setBoardId(BoardId boardId) {
        this.boardId = boardId;
    }

    private BoardId boardId = new BoardId(5,16);

    public String getSet() {
        return boardId.getSet().toString();
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
        return boardId.getBoard().toString();
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

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(final String competitionName) {
        this.competitionName = competitionName;
    }
}