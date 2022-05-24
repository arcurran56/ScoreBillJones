package scorebj.model;

public class BoardId implements Cloneable{
    enum Vulnerability{NONE, NS, EW, ALL}
    private Vulnerability vulnerabilityStatus = Vulnerability.NONE;
    private final int noOfBoards;
    private final int noOfSets;

    private int serNo;

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

    private int getSerNo() {
        return serNo;
    }

    private void setSerNo(int serNo) {
        if (serNo < 0) serNo = serNo + noOfBoards*noOfSets;
        if ( serNo >= noOfBoards*noOfSets) serNo = serNo - noOfBoards*noOfSets;
        this.serNo = serNo;
        determineVulnerability();
    }

    public void setBoard(int board) {
        this.serNo = (serNo / noOfBoards) * noOfBoards + (board - 1) ;
        determineVulnerability();
    }

    public BoardId next(){
        BoardId nextBoardId = this.clone();
        int newSerNo = this.serNo + 1;
        nextBoardId.setSerNo(newSerNo);
        return nextBoardId;
    }
    public BoardId prev(){
        BoardId prevBoardId = this.clone();
        int newSerNo = this.serNo - 1;
        prevBoardId.setSerNo(newSerNo);
        return prevBoardId;
    }
    public BoardId select(Integer set, Integer board){
        BoardId newBoardId = this.clone();
        newBoardId.setSet(set);
        newBoardId.setBoard(board);
        return newBoardId;
    }
    public boolean getVulnerability(ScoreLine.Direction direction) {
        boolean vulnerability = false;
        switch (vulnerabilityStatus){
            case NONE: break;
            case NS: if (ScoreLine.Direction.N.equals(direction) || ScoreLine.Direction.S.equals(direction)){
                vulnerability = true;
            }
            break;
            case EW: if (ScoreLine.Direction.E.equals(direction) || ScoreLine.Direction.W.equals(direction)) {
                vulnerability = true;
            }
            break;
            case ALL: vulnerability = true;
            break;
        }
        return vulnerability;
    }
    @Override
    public BoardId clone() {
        BoardId clone = null;
        try {
            clone = (BoardId) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
     private void determineVulnerability(){
        int board = serNo % noOfBoards;
        int vulnerabilityOrd = ( (board % 4) + (board / 4) ) % 4;
        vulnerabilityStatus = Vulnerability.values()[vulnerabilityOrd];

    }

}
