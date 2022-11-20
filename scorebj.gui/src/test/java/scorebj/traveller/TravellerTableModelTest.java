package scorebj.traveller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import scorebj.model.BoardId;
import scorebj.model.Contract;
import scorebj.model.ScoreLine;
import scorebj.model.Traveller;

import java.util.List;

class TravellerTableModelTest {

    @Test
    void getRowCount() {
        TravellerTableModel t = new TravellerTableModel();
        assertEquals(0,t.getRowCount());
        Traveller tr = new Traveller(new BoardId(6,12), 6);

        t.setTraveller(tr);
        assertEquals(6,t.getRowCount());
    }

    @Test
    void getColumnName1() {
        TravellerTableModel t = new TravellerTableModel();
        assertEquals("NS Score", t.getColumnName(5));
    }
    @Test
    void findColumn() {
        TravellerTableModel t = new TravellerTableModel();
        assertEquals(7, t.findColumn("NS MP"));
    }

    @Test
    void getColumnClass() {
        TravellerTableModel t = new TravellerTableModel();

    }

    @Test
    void isCellEditable() {
        TravellerTableModel t = new TravellerTableModel();
        assertTrue(t.isCellEditable(5,2));
        assertFalse(t.isCellEditable(0, 7));

    }

    @Test
    void setValueAt() {
        TravellerTableModel t = new TravellerTableModel();

    }

    @Test
    void getColumnCount() {
        TravellerTableModel t = new TravellerTableModel();
            assertEquals(9,t.getColumnCount());
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

        assertNull(t.getValueAt(4,4));
        assertEquals(2, t.getValueAt(3,0));
        assertEquals("3H", t.getValueAt(3,2).toString());
        assertEquals("N",t.getValueAt(4,3).toString());
        assertEquals(10,t.getValueAt(2,4));
    }

    @Test
    void getTraveller() {
        TravellerTableModel t = new TravellerTableModel();
        BoardId boardId = new BoardId(5,16);;
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
        assertEquals("N",scoreLine.getPlayedBy().toString());
        assertEquals(10,scoreLine.getTricks());
    }

    @Test
    void setTraveller() {
        TravellerTableModel t = new TravellerTableModel();
        BoardId boardId = new BoardId(3,4);
        Traveller traveller = new Traveller(boardId, 5);
        boardId.setSet(2);
        boardId.setBoard(3);

        ScoreLine scoreLine = traveller.getScoreLines().get(2);
        scoreLine.setNsPair(2);
        scoreLine.setContract(new Contract("3H"));
        scoreLine.setPlayedBy(ScoreLine.Direction.N);
        scoreLine.setTricks(10);

        t.setTraveller(traveller);

        assertEquals(2, t.getValueAt(2,0));
        assertEquals("3H", t.getValueAt(2,2).toString());
        assertEquals("N",t.getValueAt(2,3).toString());
        assertEquals(10,t.getValueAt(2,4));
    }

    @Test
    void testGetTraveller() {
        TravellerTableModel model = new TravellerTableModel();
        BoardId boardId = new BoardId(5, 3);
        boardId.setSet(3);
        boardId.setBoard(2);
        Traveller blank = new Traveller(boardId, 3);

        model.setTraveller(blank);
        model.setValueAt("4",0,1);
        model.setValueAt("5",2,0);

        Traveller res = model.getTraveller();
        List<ScoreLine> table = res.getScoreLines();
        assertAll( "testTraveller", () -> {
            assertEquals(3,table.size());
            assertEquals(3, model.getRowCount());
            assertEquals(4, table.get(0).getEwPair());
            assertEquals(5, table.get(2).getNsPair());
            assertNull(table.get(1).getNsPair());
            assertEquals(3,res.getBoardId().getSet());
        });
    }
}