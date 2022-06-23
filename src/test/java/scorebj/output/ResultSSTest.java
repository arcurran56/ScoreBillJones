package scorebj.output;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scorebj.model.DataStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResultSSTest {

    private final String[] HEADERS = {"Rank", "Pair", "Set 1", "Set 2", "Set 3", "TOTAL"};
    private final String[][] BODY = {{"David & Salette", "Caroline & Andrew\n45", "Alan & Diane\n56",     "Liz B & Clare\n42"},
            {"Caroline & Andrew",           "David & Salette\n32",  "Liz B & Clare\n72",     "Alan & Diane\n41"},
            {"Alan & Diane",                "Liz B & Clare\n84",    "David & Salette\n76",  "Caroline & Andrew\n82"},
            {"Liz B & Clare",               "Alan & Diane\n25",     "Caroline & Andrew\n41", "David & Salette\n31"}};
    private final int[] TOTALS = {178, 240, 120, 178};

    private DataStore dataStore;
    private final ResultSS resultSS = new ResultSS();

    @BeforeEach
    void setUp() {
        List<String> headerList = resultSS.getSsHeaderRow();
        headerList.addAll(Arrays.asList(HEADERS));

        List<SSRow> rowSet = resultSS.getSsRows();

        SSRow r;
        for (int i = 0; i < 4; i++) {
            r = new SSRow();
            r.setPair(BODY[i][0]);
            r.getSetResult().add(BODY[i][1]);
            r.getSetResult().add(BODY[i][2]);
            r.getSetResult().add(BODY[i][3]);
            r.setTotal(TOTALS[i]);
            rowSet.add(r);
        }

        dataStore = DataStore.create();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void createSpreadsheet() {

        File folder = DataStore.getPersistenceLocation();
        String filename = "test-ss.xlsx";
        File outputFile = new File(folder, filename);
        try {
            OutputStream outputStream = new FileOutputStream(outputFile);
            resultSS.createSpreadsheet(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void sortAndRank() {
        resultSS.sortAndRank();

        List<SSRow> ssRows = resultSS.getSsRows();

        assertEquals("1)", ssRows.get(0).getRank());
        assertEquals("2)", ssRows.get(1).getRank());
        assertEquals("2=", ssRows.get(2).getRank());
        assertEquals("4)", ssRows.get(3).getRank());
        assertEquals(178, ssRows.get(1).getTotal());
        assertEquals(178, ssRows.get(2).getTotal());
    }
}