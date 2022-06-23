package scorebj.traveller;

import org.junit.Assert;
import org.junit.Test;

public class ContractTest {
    @Test
    public void toString2DD() {
        Contract contract = new Contract( "2D*");
        Assert.assertEquals("2D*", contract.toString());
    }
    @Test
    public void createContract3H() {
        Contract contract = new Contract("3H");
        Assert.assertEquals(Contract.Suit.HEARTS, contract.getTrumpSuit());
        Assert.assertEquals(9, contract.getContractedTricks());
        Assert.assertEquals(Contract.DoubledStatus.UNDOUBLED, contract.getDoubledStatus());
    }

    @Test
    public void createContract6ND() {
        Contract contract = new Contract("6N*");
        Assert.assertEquals(Contract.Suit.NO_TRUMPS, contract.getTrumpSuit());
        Assert.assertEquals(12, contract.getContractedTricks());
        Assert.assertEquals(Contract.DoubledStatus.DOUBLED, contract.getDoubledStatus());
    }

    @Test
    public void createContract2CR() {
        Contract contract = new Contract("2C**");
        Assert.assertEquals(Contract.Suit.CLUBS,contract.getTrumpSuit());
        Assert.assertEquals(8,contract.getContractedTricks());
        Assert.assertEquals(Contract.DoubledStatus.REDOUBLED,contract.getDoubledStatus());
    }
    @Test
    public void score6HNVmadePlus1() {
        Contract contract = new Contract("6H");
        int score =contract.getScore(13,false);
        Assert.assertEquals(1010, score);

    }    @Test
    public void score7NDVmade() {
        Contract contract = new Contract("7N*");
        int score =contract.getScore(13,true);
        Assert.assertEquals(2490, score);

    }

    @Test
    public void score1NV2off() {
        Contract contract = new Contract("1N");
        int score =contract.getScore(5,true);
        Assert.assertEquals(-200, score);

    }
    @Test
    public void score3NNV1over(){
        Contract contract = new Contract("3N");
        int score =contract.getScore(10,false);
        Assert.assertEquals(430, score);

    }
    @Test
    public void score2DNVmade(){
        Contract contract = new Contract("2D");
        int score =contract.getScore(8,false);
        Assert.assertEquals(90, score);

    }
    @Test
    public void score5SRNV8off(){
        Contract contract = new Contract("5S**");
        int score =contract.getScore(3,false);
        Assert.assertEquals(-4000, score);

    }
    @Test
    public void score2HDVplus1(){
        Contract contract = new Contract("2H*");
        int score =contract.getScore(9,true);
        Assert.assertEquals(870, score);

    }
    @Test
    public void score5SV2off(){
        Contract contract = new Contract("5S");
        int score =contract.getScore(9,true);
        Assert.assertEquals(-200, score);

    }
}
