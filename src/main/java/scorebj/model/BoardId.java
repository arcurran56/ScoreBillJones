package scorebj.model;

public class BoardId {
    private final int noOfBoards;
    private final int noOfSets;

    private int serNo = 0;

    public BoardId(int noOfSets, int noOfBoards) {
        this.noOfSets = noOfSets;
        this.noOfBoards = noOfBoards;

    }

    public Integer getSet() {

        return serNo / noOfBoards + 1;
    }

    public void setSet(Integer set) {
        int board0 = serNo % noOfBoards;
        this.serNo = (set - 1) * noOfBoards + board0;
    }

    public Integer getBoard() {
        return serNo % noOfBoards + 1;
    }

    public void setBoard(int board) {
        this.serNo = (serNo / noOfBoards) * noOfBoards + (board - 1) ;
    }

    public BoardId next(){
        serNo++;
        if (serNo >= noOfBoards*noOfSets) serNo = serNo - noOfBoards*noOfSets;
        return this;
    }
    public BoardId prev(){
        serNo--;
        if (serNo < 0) serNo = serNo + noOfBoards*noOfSets;
        return this;
    }
    public void select(Integer set, Integer board){
        serNo = (set - 1) * noOfBoards + board -1;
    }
}
