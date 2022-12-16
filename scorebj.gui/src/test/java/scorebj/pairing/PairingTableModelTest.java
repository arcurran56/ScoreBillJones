package scorebj.pairing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PairingTableModelTest {
    private final String[] PAIRINGS = {"A&B", "C&D", "E&F", "G&H"};
    private List<String> pairings;
    private PairingTableModel ptm;

    @AfterEach
    void tearDown() {
        ptm = null;
    }

    @BeforeEach
    void setUp() {
        pairings = Arrays.asList(PAIRINGS);
        noPairs = 5;
        ptm = new PairingTableModel();
        ptm.setNoPairs(noPairs);
        ptm.setPairings(pairings);
    }

    private int noPairs;

    @Test
    void getColumnClass() {
        Class<?> colClass = ptm.getColumnClass(1);
        assertEquals("class java.lang.String", colClass.toString());
    }

    @Test
    void isCellEditable() {
        assertTrue(ptm.isCellEditable(2, 1));
        assertFalse(ptm.isCellEditable(1, 0));
    }

    @Test
    void setValueAt() {
        ptm.setValueAt("I&J",3,1);
        ptm.setValueAt("G&H",4,1);
        assertEquals("I&J", ptm.getValueAt(3,1));
    }

    @Test
    void getColumnName() {
        assertEquals("Pair #", ptm.getColumnName(0));
    }

    @Test
    void getNoPairs() {
        assertEquals(5, ptm.getNoPairs());
    }

    @Test
    void setNoPairs() {
    }

    @Test
    void setPairings() {
    }

    @Test
    void getRowCount() {
       assertEquals(5, ptm.getRowCount());
    }

    @Test
    void getColumnCount() {
        assertEquals(2, ptm.getColumnCount());
    }

    @Test
    void getValueAt() {
        assertEquals(3, ptm.getValueAt(2,0));
        assertEquals("C&D", ptm.getValueAt(1,1));
        assertEquals("", ptm.getValueAt(4,1));
    }
    @Test
    public void testToString(){
        assertEquals("PairingTableModel: 5 (1 blanks).", ptm.toString());
    }
}