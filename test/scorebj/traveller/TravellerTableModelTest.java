package scorebj.traveller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Assert.*;
import scorebj.model.ScoreLine;
import scorebj.model.Traveller;

import javax.swing.table.AbstractTableModel;

class TravellerTableModelTest {

    @Test
    void getRowCount() {
        TravellerTableModel t = new TravellerTableModel(10);
        assertEquals(5,t.getRowCount());
        t = new TravellerTableModel(13);
        assertEquals(6,t.getRowCount());
        t = new TravellerTableModel(0);
        assertEquals(0,t.getRowCount());
    }

    @Test
    void getColumnName1() {
        TravellerTableModel t = new TravellerTableModel(10);
        assertEquals("NS Score", t.getColumnName(5));
    }
    @Test
    void findColumn() {
        TravellerTableModel t = new TravellerTableModel(10);
        assertEquals(7, t.findColumn("NS MP"));
    }

    @Test
    void getColumnClass() {
        TravellerTableModel t = new TravellerTableModel(10);

    }

    @Test
    void isCellEditable() {
        TravellerTableModel t = new TravellerTableModel(10);
        assertTrue(t.isCellEditable(5,2));
        assertFalse(t.isCellEditable(0, 7));

    }

    @Test
    void setValueAt() {
        TravellerTableModel t = new TravellerTableModel(10);

    }

    @Test
    void getColumnCount() {
        TravellerTableModel t = new TravellerTableModel(10);
            assertEquals(9,t.getColumnCount());
    }

    @Test
    void getValueAt() {
        TravellerTableModel t = new TravellerTableModel(10);
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
        TravellerTableModel t = new TravellerTableModel(12);
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
        TravellerTableModel t = new TravellerTableModel(12);
        Traveller traveller = new Traveller(5);
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
}