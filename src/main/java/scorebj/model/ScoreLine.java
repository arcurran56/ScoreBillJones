package scorebj.model;

import scorebj.traveller.Contract;

public class ScoreLine {
    private int nsPair;
    private int ewPair;
    private Contract contract;
    enum Direction {NORTH, SOUTH, EAST, WEST}
    private Direction playedBy;
    private enum Suit {CLUBS,DIAMONDS,HEARTS,SPADES,NOTRUMPS}
    private Suit bidSuit;
    private int tricks;
    private int score;
    private int nsMPs;
    private int ewMPs;

}
