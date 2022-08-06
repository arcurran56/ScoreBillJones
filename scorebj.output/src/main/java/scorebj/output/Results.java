package scorebj.output;

import scorebj.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Results {

    public Results(){};

    public void save(Competition competition) throws DataStoreException {
            File outputXlsxFile = new File( DataStore.getPersistenceLocation(), "summary.xlsx");

            int noPairs = competition.getNoPairs();
            int noSets = competition.getNoPairs();
            int noBoardsPerSet = competition.getNoBoardsPerSet();

            Result result = new Result(noPairs, noSets, noBoardsPerSet);

            List<String> pairings = competition.getPairings();
            List<Traveller> travellers = competition.getTravellers();

            result.collate(pairings, travellers);
            try {
                result.printDetails();
                result.printSummary();
                result.printMatrix();
                ResultSS ss = result.createSummaryTable();
                ss.createSpreadsheet(new FileOutputStream(outputXlsxFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
