package scorebj.output;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scorebj.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Results {
    Logger logger = LogManager.getLogger();

    public Results() {
    }

    public void save(Competition competition) throws DataStoreException {
        logger.debug("Publishing results...");
        int noPairs = competition.getNoPairs();
        int noSets = competition.getNoSets();
        int noBoardsPerSet = competition.getNoBoardsPerSet();

        Result result = new Result(noPairs, noSets, noBoardsPerSet);

        List<String> pairings = competition.getPairings();
        List<Traveller> travellers = competition.getTravellers();

        result.collate(pairings, travellers);

        List<String> allTravellerLines = competition.travellersToText();
        String modifiedName = competition.getCompetitionName().replace(' ', '-');

        File detailsFile = new File(DataStore.getPersistenceLocation(), modifiedName + "-details.csv");
        File summaryFile = new File(DataStore.getPersistenceLocation(), modifiedName + "-summary.csv");
        File matrixFile = new File(DataStore.getPersistenceLocation(), modifiedName + "-matrix.csv");
        File travellersFile = new File(DataStore.getPersistenceLocation(), modifiedName + "-travellers.csv");
        File outputXlsxFile = new File(DataStore.getPersistenceLocation(), modifiedName + "-results.xlsx");

        try {
            result.printDetails(detailsFile);
            result.printSummary(summaryFile);
            result.printMatrix(matrixFile);
            result.createResultsSpreadsheet(outputXlsxFile);
            PrintWriter travellersOutput = new PrintWriter(travellersFile);

            for (String line : allTravellerLines) {
                travellersOutput.println(line);
            }
            travellersOutput.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
