package scorebj.model;

import scorebj.traveller.Contract;

public class ScoreLine {

    enum Columns {NS_PAIR, EW_PAIR, CONTRACT, PLAYED_BY, TRICKS, NS_SCORE, EW_SCORE, NS_MPS, EW_MPS}
    private final Object[] entry = new Object[9];

    public enum Direction {N, S, E, W}
    private enum Suit {CLUBS,DIAMONDS,HEARTS,SPADES,NOTRUMPS}

    public Object get(int index) {
        return entry[index];
    }
     public void set(int index, Object value){
        entry[index] = value;
     }

     public Integer getNsPair() {
        return (Integer) entry[Columns.NS_PAIR.ordinal()];
    }

    public void setNsPair(Integer nsPair) {
        entry[Columns.NS_PAIR.ordinal()] = nsPair;
    }

    public Integer getEwPair() {
        return (Integer) entry[Columns.EW_PAIR.ordinal()];
    }

    public void setEwPair(Integer ewPair) {
        entry[Columns.EW_PAIR.ordinal()] = ewPair;
    }

    public Contract getContract() {
        return (Contract) entry[Columns.CONTRACT.ordinal()];
    }

    public void setContract(Contract contract) {
        entry[Columns.CONTRACT.ordinal()] = contract;
    }

    public Direction getPlayedBy() {
        return (Direction) entry[Columns.PLAYED_BY.ordinal()];
    }

    public void setPlayedBy(Direction playedBy) {
        entry[Columns.PLAYED_BY.ordinal()] = playedBy;
    }

    public Integer getTricks() {
        return (Integer) entry[Columns.TRICKS.ordinal()];
    }

    public void setTricks(Integer tricks) {
        entry[Columns.TRICKS.ordinal()] = tricks;
    }

    public Integer getNSScore() {
        return (Integer) entry[Columns.NS_SCORE.ordinal()];
    }
    public Integer getEWScore() {
        return (Integer) entry[Columns.EW_SCORE.ordinal()];
    }

    public Integer getNsMPs() {
        return (Integer) entry[Columns.NS_MPS.ordinal()];
    }

    public void setNsMPs(Integer nsMPs) {
        entry[Columns.NS_MPS.ordinal()] = nsMPs;
    }

    public Integer getEwMPs() {
        return (Integer) entry[Columns.EW_MPS.ordinal()];
    }

    public void setEwMPs(Integer ewMPs) {
        entry[Columns.EW_MPS.ordinal()] = ewMPs;
    }

    public void scoreHand(boolean vulnerable){
        Integer nsPair = getNsPair();
        Integer ewPair = getEwPair();
        Contract contract = getContract();
        Direction playedBy = getPlayedBy();
        Integer tricks = getTricks();

        Integer NSScore = 0;
        Integer EWScore = 0;

        if (nsPair != null
        && ewPair !=null
        && contract != null
        && playedBy != null
        && tricks != null){
            int score = contract.getScore(tricks,vulnerable);
            switch (playedBy){
                case N:
                    case S:
                            if (score>0){ NSScore = score;
                            EWScore = 0;}
                            else {
                                NSScore = 0;
                                EWScore = -score;
                            }
                            break;

                case E:
                case W:
                    if (score>0){
                        NSScore = 0;
                        EWScore = score; }
                    else {
                        NSScore = -score;
                        EWScore = 0;
                    }
                    break;
            }
            entry[Columns.NS_SCORE.ordinal()] = NSScore;
            entry[Columns.EW_SCORE.ordinal()] = EWScore;
        }
    }




}
