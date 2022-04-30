package scorebj.model;

import scorebj.traveller.Contract;

public class ScoreLine {
    private Integer nsPair;
    private Integer ewPair;
    private Contract contract;
    private Integer NSScore;
    private Integer EWScore;

    public Integer getNsPair() {
        return nsPair;
    }

    public void setNsPair(Integer nsPair) {
        this.nsPair = nsPair;
    }

    public Integer getEwPair() {
        return ewPair;
    }

    public void setEwPair(Integer ewPair) {
        this.ewPair = ewPair;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Direction getPlayedBy() {
        return playedBy;
    }

    public void setPlayedBy(Direction playedBy) {
        this.playedBy = playedBy;
    }

    public Suit getBidSuit() {
        return bidSuit;
    }

    public void setBidSuit(Suit bidSuit) {
        this.bidSuit = bidSuit;
    }

    public Integer getTricks() {
        return tricks;
    }

    public void setTricks(Integer tricks) {
        this.tricks = tricks;
    }

    public Integer getNSScore() {
        return NSScore;
    }
    public Integer getEWScore() {
        return EWScore;
    }

    public Integer getNsMPs() {
        return nsMPs;
    }

    public void setNsMPs(Integer nsMPs) {
        this.nsMPs = nsMPs;
    }

    public Integer getEwMPs() {
        return ewMPs;
    }

    public void setEwMPs(Integer ewMPs) {
        this.ewMPs = ewMPs;
    }

    public void scoreHand(boolean vulnerable){
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
                            };
                            break;

                case E:
                case W:
                    if (score>0){
                        NSScore = 0;
                        EWScore = score; }
                    else {
                        NSScore = -score;
                        EWScore = 0;
                    };
                    break;

            }
        }
    }


    public enum Direction {N, S, E, W}
    private Direction playedBy;
    private enum Suit {CLUBS,DIAMONDS,HEARTS,SPADES,NOTRUMPS}
    private Suit bidSuit;
    private Integer tricks;
    private Integer NSscore;
    private Integer EWscore;
    private Integer nsMPs;
    private Integer ewMPs;

}
