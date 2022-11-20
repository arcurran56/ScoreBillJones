package scorebj.model;

public class BoardId implements Cloneable{
    enum Vulnerability{NONE, NS, EW, ALL}
    private Vulnerability vulnerabilityStatus = Vulnerability.NONE;
    private final int noOfBoards;
    private final int noOfSets;

    private int serNo;

    public BoardId(){
        this(0, 0);
    }

    public BoardId(int noOfSets, int noOfBoards) {
        this.noOfSets = noOfSets;
        this.noOfBoards = noOfBoards;

    }

    public Integer getSet() {
        Integer set;

        if (noOfBoards > 0) {
            set = serNo / noOfBoards + 1;
        }
        else {
            set = 0;
        }
        return set;
    }

    public void setSet(Integer set) {
        if (noOfBoards>0) {
            int board0 = serNo % noOfBoards;
            this.serNo = (set - 1) * noOfBoards + board0;
        }
        else {
            serNo = 0;
        }
    }

    public Integer getBoard() {
        int board;
        if (noOfBoards>0){
            board = serNo % noOfBoards + 1;
        }
        else {
            board = 0;
        }
        return board;
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
        if (noOfBoards>0) {
            this.serNo = (serNo / noOfBoards) * noOfBoards + (board - 1);
            determineVulnerability();
        }
        else {
            serNo = 0;
        }
    }

    public BoardId next(){
        BoardId nextBoardId = this.clone();
        int newSerNo = this.serNo + 1;
        nextBoardId.setSerNo(newSerNo);
        nextBoardId.determineVulnerability();
        return nextBoardId;
    }
    public BoardId prev(){
        BoardId prevBoardId = this.clone();
        int newSerNo = this.serNo - 1;
        prevBoardId.setSerNo(newSerNo);
        prevBoardId.determineVulnerability();
        return prevBoardId;
    }
    public BoardId select(Integer set, Integer board){
        BoardId newBoardId = this.clone();
        newBoardId.setSet(set);
        newBoardId.setBoard(board);
        newBoardId.determineVulnerability();
        return newBoardId;
    }
    public boolean isVulnerable(ScoreLine.Direction direction) {
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
        int board = getBoard();
        int vulnerabilityOrd;
        if(board>0) {
            vulnerabilityOrd = (((board - 1) % 4) + ((board - 1) / 4)) % 4;
        }
        else {
            vulnerabilityOrd = 0;
        }
        vulnerabilityStatus = Vulnerability.values()[vulnerabilityOrd];

    }
    public String toString(){
        StringBuilder builder = new StringBuilder("Board ");
        builder.append(getBoard())
                .append("/")
                .append(noOfBoards)
                .append(", set")
                .append(getSet())
                .append("/")
                .append(noOfSets)
                .append(".  ");
        return builder.toString();

    }
    public Vulnerability getVulnerabilityStatus(){
        return vulnerabilityStatus;
    }
}
