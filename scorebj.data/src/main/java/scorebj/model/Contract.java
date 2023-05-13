package scorebj.model;

public class Contract {
    enum Suit {CLUBS("C",20,20),
        DIAMONDS("D",20,20),
        HEARTS("H",30,30),
        SPADES("S",30,30),
        NO_TRUMPS("N",40,30);
        final String abbreviation;
        final int firstTrick;
        final int subsequentTricks;

        Suit(String abbreviation, int firstTrick, int subsequentTricks){
            this.abbreviation = abbreviation;
            this.firstTrick = firstTrick;
            this.subsequentTricks = subsequentTricks;
        }
        static Suit lookUp(String abbreviation){
            Suit result = null;
            for (Suit s: Suit.values()){
               if( abbreviation.equals(s.abbreviation) ) {
                   result = s;
               }
            }
        return result;
    }

    }
    enum DoubledStatus {UNDOUBLED, DOUBLED, REDOUBLED}

    public int getContractedTricks() {
        return contractedTricks;
    }

    public Suit getTrumpSuit() {
        return trumpSuit;
    }

    public DoubledStatus getDoubledStatus() {
        return doubledStatus;
    }

    private final String contractString;
    private final int contractedTricks;
    private final Suit trumpSuit;
    private DoubledStatus doubledStatus;

    private boolean passedOut = false;

    private boolean skipped = false;

    public boolean isSkipped() {
        return skipped;
    }

    public boolean isPassedOut() {
        return passedOut;
    }

    public Contract(){
        this("");
    }
    public Contract(String contractString) {
        this.contractString = contractString.toUpperCase();
        int length = contractString.length();
        if ("AP".equals(this.contractString) ) {
            contractedTricks = 0;
            trumpSuit = null;
            doubledStatus = DoubledStatus.UNDOUBLED;
            passedOut = true;
        }
        else {
            if ("X".equals(this.contractString) ) {
                contractedTricks = 0;
                trumpSuit = null;
                doubledStatus = DoubledStatus.UNDOUBLED;
                skipped = true;
            }
            else {
                contractedTricks = Integer.parseUnsignedInt(String.valueOf(contractString.charAt(0))) + 6;
                trumpSuit = Suit.lookUp(String.valueOf(contractString.charAt(1)));

                doubledStatus = DoubledStatus.UNDOUBLED;
                if (length == 3 && "*".equals(String.valueOf(contractString.charAt(2))))
                    doubledStatus = DoubledStatus.DOUBLED;
                if (length == 4 && "*".equals(String.valueOf(contractString.charAt(2)))
                        && "*".equals(String.valueOf(contractString.charAt(3))))
                    doubledStatus = DoubledStatus.REDOUBLED;
                }
            }
    }


    public int getScore(int tricksWon, boolean vulnerable){

        boolean made;
        int overTricks = 0;
        int underTricks = 0;
        int score = 0;

        if (passedOut) return score;
        if (skipped) return score;

        if (tricksWon>=contractedTricks) {

            //Contract made.
            overTricks = tricksWon - contractedTricks;

            score = trumpSuit.firstTrick
                    + Math.max(contractedTricks - 7, 0) * trumpSuit.subsequentTricks;

            switch (doubledStatus) {
                case UNDOUBLED: break;
                case DOUBLED: {
                    score = score * 2;
                    break;
                }
                case REDOUBLED:
                    score = score * 4;
                    break;

            }

            //Add bonuses.
            //Add part score bonus.
            if (score<100) {
                score = score + 50; //part score bonus;
            }
            else {
                //Add game bonus.
                if(!vulnerable){
                    score = score + 300;
                }
                else {
                    score = score + 500;
                }
                //Add slam bonus
                if (contractedTricks==12){
                    if (!vulnerable) {
                        score = score + 500;
                    }
                    else {
                        score = score + 750;
                    }
                }
                if (contractedTricks==13){
                    if (!vulnerable) {
                        score = score + 1000;
                    }
                    else {
                        score = score + 1500;
                    }
                }
            }
            //Now account for overtricks...
            switch (doubledStatus) {
                case UNDOUBLED: {
                    score = score + overTricks * trumpSuit.subsequentTricks;
                    break;
                }
                case DOUBLED: {
                    if (!vulnerable) {
                        score = score + overTricks * 100 + 50;
                    } else {
                        score = score + overTricks * 200 + 50;

                    }
                    break;
                }
                case REDOUBLED: {
                    if (!vulnerable) {
                        score = score + overTricks * 200 + 100;
                    } else {
                        score = score + overTricks * 400 + 100;

                    }
                    break;
                }
            }
        }
        else {
            //Contract down.
            made = false;
            underTricks = contractedTricks - tricksWon;

            switch (doubledStatus) {
                case UNDOUBLED: {
                    if (!vulnerable) {
                        score = - underTricks * 50;
                    } else {
                        score = - underTricks * 100;
                    }
                    break;
                }
                case DOUBLED: {
                    if (!vulnerable) {
                        score = -(100 + Math.min(underTricks - 1, 2)*200 + Math.max(underTricks - 3, 0)*300 ) ;
                    } else {
                        score = -(200 + Math.max(underTricks - 1, 0)*300) ;
                    }
                    break;
                }
                case REDOUBLED: {
                    score = -2 * (100 + Math.min(underTricks - 1, 2)*200 + Math.max(underTricks - 3, 0)*300 ) ;
                    break;
                }
            }

        }
        return score;
    }
    public String toString() {
        return contractString;
    }
}
