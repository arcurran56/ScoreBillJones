package scorebj.traveller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Assert.*;

import javax.swing.table.AbstractTableModel;

class TravellerTableModelTest {

    @Test
    void getRowCount() {
        TravellerTableModel t = new TravellerTableModel();
        assertEquals(10,t.getRowCount());
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
        t.setValueAt(2, 9, 0);
        t.setValueAt("3H", 3, 3);
        t.setValueAt("N", 5, 4);
        t.setValueAt(10, 2, 5);

        assertNull(t.getValueAt(4,4));
        assertEquals(2, t.getValueAt(9,0));
        assertEquals("3H", t.getValueAt(3,3));
        assertEquals("N",t.getValueAt(5,4));
        assertEquals(10,t.getValueAt(2,5));
    }
}