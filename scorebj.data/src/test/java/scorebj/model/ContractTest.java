package scorebj.model;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContractTest {
    @Test
    public void toString2DD() {
        Contract contract = new Contract( "2D*");
        assertEquals("2D*", contract.toString());
    }
    @Test
    public void createContract3H() {
        Contract contract = new Contract("3H");
        assertEquals(Contract.Suit.HEARTS, contract.getTrumpSuit());
        assertEquals(9, contract.getContractedTricks());
        assertEquals(Contract.DoubledStatus.UNDOUBLED, contract.getDoubledStatus());
    }

    @Test
    public void createContract6ND() {
        Contract contract = new Contract("6N*");
        assertEquals(Contract.Suit.NO_TRUMPS, contract.getTrumpSuit());
        assertEquals(12, contract.getContractedTricks());
        assertEquals(Contract.DoubledStatus.DOUBLED, contract.getDoubledStatus());
    }

    @Test
    public void createContract2CR() {
        Contract contract = new Contract("2C**");
        assertEquals(Contract.Suit.CLUBS,contract.getTrumpSuit());
        assertEquals(8,contract.getContractedTricks());
        assertEquals(Contract.DoubledStatus.REDOUBLED,contract.getDoubledStatus());
    }
    @Test
    public void score6HNVmadePlus1() {
        Contract contract = new Contract("6H");
        int score =contract.getScore(13,false);
        assertEquals(1010, score);

    }    @Test
    public void score7NDVmade() {
        Contract contract = new Contract("7N*");
        int score =contract.getScore(13,true);
        assertEquals(2490, score);

    }

    @Test
    public void score1NV2off() {
        Contract contract = new Contract("1N");
        int score =contract.getScore(5,true);
        assertEquals(-200, score);

    }
    @Test
    public void score3NNV1over(){
        Contract contract = new Contract("3N");
        int score =contract.getScore(10,false);
        assertEquals(430, score);

    }
    @Test
    public void score2DNVmade(){
        Contract contract = new Contract("2D");
        int score =contract.getScore(8,false);
        assertEquals(90, score);

    }
    @Test
    public void score5SRNV8off(){
        Contract contract = new Contract("5S**");
        int score =contract.getScore(3,false);
        assertEquals(-4000, score);

    }
    @Test
    public void score2HDVplus1(){
        Contract contract = new Contract("2H*");
        int score =contract.getScore(9,true);
        assertEquals(870, score);

    }
    @Test
    public void score5SV2off(){
        Contract contract = new Contract("5S");
        int score =contract.getScore(9,true);
        assertEquals(-200, score);

    }
    @Test
    public void score5SV2offLC(){
        Contract contract = new Contract("5s*");
        int score =contract.getScore(9,true);
        assertEquals(-500, score);
        assertEquals("5S*", contract.toString());

    }
    @Test
    public void scoreAllPass(){
        Contract contract = new Contract("AP");
        int score =contract.getScore(9,true);
        assertEquals(0, score);
        assertTrue(contract.isPassedOut());

    }
    @Test
    public void scoreAllPassLC(){
        Contract contract = new Contract("aP");
        int score =contract.getScore(9,true);
        assertEquals(0, score);
        assertTrue(contract.isPassedOut());

    }    @Test
    public void scoreSkipped(){
        Contract contract = new Contract("X");
        int score =contract.getScore(9,true);
        assertEquals(0, score);
        assertTrue(contract.isSkipped());

    }

}
