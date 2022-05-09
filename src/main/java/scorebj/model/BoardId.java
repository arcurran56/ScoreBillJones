package scorebj.model;

import java.util.Enumeration;

public class BoardId {
    enum Vulnerability{NONE, NS, EW, ALL}
    private Vulnerability vulnerabilityStatus = Vulnerability.NONE;
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

    public int getSerNo() {
        return serNo;
    }

    public void setBoard(int board) {
        this.serNo = (serNo / noOfBoards) * noOfBoards + (board - 1) ;
        determineVulnerability();
    }

    public BoardId next(){
        serNo++;
        if (serNo >= noOfBoards*noOfSets) serNo = serNo - noOfBoards*noOfSets;
        determineVulnerability();
        return this;
    }
    public BoardId prev(){
        serNo--;
        if (serNo < 0) serNo = serNo + noOfBoards*noOfSets;
        determineVulnerability();
        return this;
    }
    public void select(Integer set, Integer board){
        serNo = (set - 1) * noOfBoards + board -1;
        determineVulnerability();
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

     private void determineVulnerability(){
        int board = serNo % noOfBoards;
        int vulnerabilityOrd = ( (board % 4) + (board / 4) ) % 4;
        vulnerabilityStatus = Vulnerability.values()[vulnerabilityOrd];

    }
}
