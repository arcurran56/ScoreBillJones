package scorebj.traveller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import scorebj.model.BoardId;
import scorebj.model.Contract;
import scorebj.model.ScoreLine;
import scorebj.model.Traveller;

import java.util.ArrayList;
import java.util.List;

class TravellerTableModelTest {
    Traveller emptyTravellerFill1;
    Traveller completeTraveller;
    Traveller incompleteTraveller;
    private Traveller emptyTravellerFill2;


    BoardId boardIds1b2;
    BoardId boardIds2b3;

    TravellerTableModel travellerTableModelEmpty;
    TravellerTableModel travellerTableModelIncomplete;
    TravellerTableModel travellerTableModelComplete;
    private BoardId boardIds1b3;
    private BoardId boardIds1b1;

    @Test
    void getRowCount() {
        TravellerTableModel t = new TravellerTableModel();
        assertEquals(0, t.getRowCount());
        Traveller tr = new Traveller(new BoardId(6, 12), 6);

        t.setTraveller(tr);
        assertEquals(6, t.getRowCount());
    }

    @Test
    void getColumnName1() {
        TravellerTableModel t = new TravellerTableModel();
        assertEquals("NS Score", t.getColumnName(5));
    }

    @Test
    void findColumn() {
        TravellerTableModel t = new TravellerTableModel();

        assertAll( ()-> {
            assertEquals(7, t.findColumn("NS MP"));
            assertEquals(9, t.findColumn("NS OR"));
            assertEquals(10, t.findColumn("EW OR"));
        });

    }

    @Test
    void getColumnClass() {
        TravellerTableModel t = new TravellerTableModel();
        assertEquals(Integer.class, t.getColumnClass(9));

    }

    @Test
    void isCellEditable() {
        TravellerTableModel t = new TravellerTableModel();
        assertTrue(t.isCellEditable(5, 2));
        assertFalse(t.isCellEditable(0, 7));

    }

    @Test
    void setValueAt() {
        TravellerTableModel t = new TravellerTableModel();

    }

    @Test
    void getColumnCount() {
        TravellerTableModel t = new TravellerTableModel();
        assertEquals(11, t.getColumnCount());
    }

    @Test
    void getValueAt() {
        TravellerTableModel t = new TravellerTableModel();
        Traveller tr = new Traveller(new BoardId(10, 5), 6);

        t.setTraveller(tr);
        t.setValueAt("2", 3, 0);
        t.setValueAt("3H", 3, 2);
        t.setValueAt("N", 4, 3);
        t.setValueAt("10", 2, 4);

        assertNull(t.getValueAt(4, 4));
        assertEquals(2, t.getValueAt(3, 0));
        assertEquals("3H", t.getValueAt(3, 2).toString());
        assertEquals("N", t.getValueAt(4, 3).toString());
        assertEquals(10, t.getValueAt(2, 4));
    }

    @Test
    void getTraveller() {
        TravellerTableModel t = new TravellerTableModel();
        BoardId boardId = new BoardId(5, 16);
        Traveller tr = new Traveller(boardId, 5);

        t.setTraveller(tr);
        t.setValueAt("2", 3, 0);
        t.setValueAt("3H", 3, 2);
        t.setValueAt("N", 3, 3);
        t.setValueAt("10", 3, 4);

        Traveller traveller = t.getTraveller();
        ScoreLine scoreLine = traveller.getScoreLine(3);
        assertEquals(2, scoreLine.getNsPair());
        assertEquals("3H", scoreLine.getContract().toString());
        assertEquals("N", scoreLine.getPlayedBy().toString());
        assertEquals(10, scoreLine.getTricks());
    }

    @Test
    void setTraveller() {
        TravellerTableModel t = new TravellerTableModel();
        BoardId boardId = new BoardId(3, 4);
        Traveller traveller = new Traveller(boardId, 5);
        boardId.setSet(2);
        boardId.setBoard(3);

        t.setTraveller(traveller);

        t.setValueAt("2", 2, 0);
        t.setValueAt("5", 2, 1);
        t.setValueAt("3H", 2, 2);
        t.setValueAt("N", 2, 3);
        t.setValueAt("10", 2, 4);

        assertAll("setTraveller", () -> {

            assertEquals(2, t.getValueAt(2, 0));
            assertEquals("3H", t.getValueAt(2, 2).toString());
            assertEquals("N", t.getValueAt(2, 3).toString());
            assertEquals(10, t.getValueAt(2, 4));
            assertEquals(170, t.getValueAt(2, 5));
        });
    }

    @Test
    void setTraveller2() {
        TravellerTableModel ttm = new TravellerTableModel();

        BoardId mockBoardId = mock(BoardId.class);
        Traveller mockTraveller = mock(Traveller.class);
        List<ScoreLine> scoreLines = new ArrayList<>();

        ScoreLine scoreLineFilled = new ScoreLine();
        scoreLineFilled.setNsPair(5);
        scoreLineFilled.setEwPair(4);
        scoreLineFilled.setContract(new Contract("3H"));
        scoreLineFilled.setPlayedBy(ScoreLine.Direction.E);
        scoreLineFilled.setTricks(9);
        scoreLineFilled.setNsMPs(4);
        scoreLineFilled.setEwMPs(2);
        //scoreLineFilled.setComplete(true);

        ScoreLine scoreLineUnfilled = new ScoreLine();
        scoreLines.add(scoreLineFilled);

        //Set traveller to create autofill cache
        when(mockBoardId.getSet()).thenReturn(1);
        when(mockBoardId.getBoard()).thenReturn(1);
        when(mockTraveller.getSize()).thenReturn(1);
        when(mockTraveller.getBoardId()).thenReturn(mockBoardId);
        when(mockTraveller.isComplete()).thenReturn(true);
        when(mockTraveller.getScoreLines()).thenReturn(scoreLines);

        ttm.setTraveller(mockTraveller);

        scoreLines.clear();
        scoreLines.add(scoreLineUnfilled);

        when(mockBoardId.getSet()).thenReturn(1);
        when(mockBoardId.getBoard()).thenReturn(2);
        when(mockTraveller.getBoardId()).thenReturn(mockBoardId);
        when(mockTraveller.isEmpty()).thenReturn(true);

        ttm.setTraveller(mockTraveller);

        assertEquals(5,scoreLineUnfilled.getNsPair());

        scoreLineUnfilled = new ScoreLine();
        scoreLines.clear();
        scoreLines.add(scoreLineUnfilled);

        when(mockBoardId.getSet()).thenReturn(2);
        when(mockBoardId.getBoard()).thenReturn(1);

        ttm.setTraveller(mockTraveller);

        assertNull(scoreLineUnfilled.getNsPair());


    }

    @Test
    void testGetTraveller() {
        TravellerTableModel model = new TravellerTableModel();
        BoardId boardId = new BoardId(5, 3);
        boardId.setSet(3);
        boardId.setBoard(2);
        Traveller blank = new Traveller(boardId, 3);

        model.setTraveller(blank);
        model.setValueAt("4", 0, 1);
        model.setValueAt("5", 2, 0);

        Traveller res = model.getTraveller();
        List<ScoreLine> table = res.getScoreLines();
        assertAll("testTraveller", () -> {
            assertEquals(3, table.size());
            assertEquals(3, model.getRowCount());
            assertEquals(4, table.get(0).getEwPair());
            assertEquals(5, table.get(2).getNsPair());
            assertNull(table.get(1).getNsPair());
            assertEquals(3, res.getBoardId().getSet());
        });
    }

    @Test
    void isComplete() {
        TravellerTableModel travellerTableModel = new TravellerTableModel();

        travellerTableModel.setTraveller(emptyTravellerFill1);
        assertFalse(travellerTableModel.isComplete());

        travellerTableModel.setTraveller(incompleteTraveller);
        assertFalse(travellerTableModel.isComplete());

        travellerTableModel.setTraveller(completeTraveller);

        assertTrue(travellerTableModel.isComplete());
    }

    @Test
    void isEmpty() {
        TravellerTableModel travellerTableModel = new TravellerTableModel();
        Traveller emptyTraveller = new Traveller(boardIds1b1, 5);

        travellerTableModel.setTraveller(emptyTraveller);
        assertTrue(travellerTableModel.isEmpty());

        travellerTableModel.setTraveller(incompleteTraveller);
        assertFalse(travellerTableModel.isEmpty());

        travellerTableModel.setTraveller(completeTraveller);
        assertFalse(travellerTableModel.isEmpty());
    }

    @Test
    void autoFill() {

        travellerTableModelComplete.setTraveller(emptyTravellerFill1);

        Traveller pre = travellerTableModelComplete.getTraveller();

        Traveller notPre = travellerTableModelIncomplete.getTraveller();

        //Traveller pre = travellerTableModelEmpty.setTraveller();


        List<ScoreLine> scoreLines1;
        List<ScoreLine> scoreLines2;
        List<ScoreLine> scoreLines3;

        scoreLines1 = pre.getScoreLines();
        scoreLines2 = notPre.getScoreLines();
        //scoreLines3 = notPre2.getScoreLines();

        assertAll("pre-filled pairs", () -> {
            assertEquals(1, scoreLines1.get(1).getNsPair());
            assertEquals(4, scoreLines1.get(2).getEwPair());
        });
        travellerTableModelComplete.setTraveller(emptyTravellerFill2);
        assertAll("not pre-filled", () -> {
            assertEquals(3, scoreLines2.get(2).getNsPair());
            assertNull(scoreLines2.get(4).getEwPair());
        });
    }

    @BeforeEach
    void setUp() {
        travellerTableModelEmpty = new TravellerTableModel();
        travellerTableModelIncomplete = new TravellerTableModel();
        travellerTableModelComplete = new TravellerTableModel();

        boardIds1b3 = new BoardId(7, 3);
        boardIds1b3.setBoard(3); //Set 1, Board 3, EW Vuln

        boardIds1b1 = new BoardId(7, 3); //Set 1, Board 1, None Vuln

        //boardId1 = new BoardId(7,3); //Set 1, Board 1

        boardIds1b2 = new BoardId(7, 3); //Set 1, Board 4
        boardIds1b2.setBoard(2);

        boardIds2b3 = new BoardId(7, 3); //Set 2, Board 3
        boardIds2b3.setSet(2);
        boardIds2b3.setBoard(3);

        emptyTravellerFill1 = new Traveller(boardIds1b2, 5);
        travellerTableModelEmpty.setTraveller(emptyTravellerFill1);

        emptyTravellerFill2 = new Traveller(boardIds2b3, 5);
        travellerTableModelEmpty.setTraveller(emptyTravellerFill1);

        completeTraveller = new Traveller(boardIds1b1, 5);
        travellerTableModelComplete.setTraveller(completeTraveller);

        List<ScoreLine> list = completeTraveller.getScoreLines();

        list.get(0).setNsPair(7);
        list.get(0).setEwPair(8);
        list.get(0).setContract(new Contract("3H*"));
        list.get(0).setPlayedBy(ScoreLine.Direction.E);
        list.get(0).setTricks(8);
        //NS 100; MP 6-2

        list.get(1).setNsPair(1);
        list.get(1).setEwPair(2);
        list.get(1).setContract(new Contract("3H*"));
        list.get(1).setPlayedBy(ScoreLine.Direction.E);
        list.get(1).setTricks(8);
        //NS 100; MP 6-2

        list.get(2).setNsPair(3);
        list.get(2).setEwPair(4);
        list.get(2).setContract(new Contract("3H"));
        list.get(2).setPlayedBy(ScoreLine.Direction.E);
        list.get(2).setTricks(8);
        //NS 50; MP 3-5

        list.get(3).setNsPair(5);
        list.get(3).setEwPair(6);
        list.get(3).setContract(new Contract("3H*"));
        list.get(3).setPlayedBy(ScoreLine.Direction.E);
        list.get(3).setTricks(9);
        //EW 530 MP: 1-7

        list.get(4).setNsPair(9);
        list.get(4).setEwPair(10);
        list.get(4).setContract(new Contract("X"));
        list.get(4).setPlayedBy(ScoreLine.Direction.N);
        list.get(4).setTricks(8);
        //EW 0, MP: 4-4


        incompleteTraveller = new Traveller(boardIds1b1, 5);
        travellerTableModelIncomplete.setTraveller(incompleteTraveller);

        list = incompleteTraveller.getScoreLines();

        list.get(0).setNsPair(7);
        list.get(0).setEwPair(8);
        list.get(0).setContract(new Contract("3H*"));
        list.get(0).setPlayedBy(ScoreLine.Direction.E);
        list.get(0).setTricks(8);
        //NS 200

        list.get(1).setNsPair(1);
        list.get(1).setEwPair(2);
        list.get(1).setContract(new Contract("3H*"));
        list.get(1).setPlayedBy(ScoreLine.Direction.E);
        list.get(1).setTricks(8);
        //NS 200

        list.get(2).setNsPair(3);
        list.get(2).setEwPair(4);
        list.get(2).setContract(new Contract("3H"));
        list.get(2).setPlayedBy(ScoreLine.Direction.E);
        list.get(2).setTricks(8);
        //NS 100

        list.get(3).setNsPair(5);
        list.get(3).setEwPair(6);
        list.get(3).setContract(new Contract("3H"));
        list.get(3).setPlayedBy(ScoreLine.Direction.E);
        list.get(3).setTricks(9);

    }

    @AfterEach
    void tearDown() {

        emptyTravellerFill1 = null;
        completeTraveller = null;
        incompleteTraveller = null;
    }

    @Test
    void getColumnName() {
    }

   @Test
    void testGetColumnClass() {
    }

    @Test
    @Disabled
    void allocateMPs1(){
        List<ScoreLine> list = completeTraveller.getScoreLines();
        assertAll("Check results", () -> {
            assertEquals(100, list.get(0).getNSScore());
            assertEquals(6, list.get(0).getNsMPs());
            assertEquals(2, list.get(0).getEwMPs());

            assertEquals(530, list.get(3).getEWScore());
            assertEquals(1, list.get(3).getNsMPs());
            assertEquals(7, list.get(3).getEwMPs());

            assertEquals(4, list.get(4).getNsMPs());
            assertEquals(4, list.get(4).getEwMPs());
        } );
    }

    @Test
    void allocateMPs2(){
        List<ScoreLine> list = completeTraveller.getScoreLines();
        assertAll("Check results", () -> {
            assertEquals(100, list.get(0).getNSScore());
            assertEquals(5, list.get(0).getNsMPs());
            assertEquals(1, list.get(0).getEwMPs());

            assertEquals(530, list.get(3).getEWScore());
            assertEquals(0, list.get(3).getNsMPs());
            assertEquals(6, list.get(3).getEwMPs());

            assertEquals(3, list.get(4).getNsMPs());
            assertEquals(3, list.get(4).getEwMPs());
        } );
    }
}