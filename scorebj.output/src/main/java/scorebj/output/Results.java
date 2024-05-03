package scorebj.output;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scorebj.model.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import scorebj.model.DataStore;

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
            logger.info(detailsFile.getAbsolutePath() + " created.");
            result.printSummary(summaryFile);
            logger.info(summaryFile.getAbsolutePath() + " created.");
            result.printMatrix(matrixFile);
            logger.info(matrixFile.getAbsolutePath() + " created.");
            result.createResultsSpreadsheet(outputXlsxFile);
            logger.info(outputXlsxFile + " created.");
            PrintWriter travellersOutput = new PrintWriter(travellersFile);
            logger.info(travellersFile + " created.");



            for (String line : allTravellerLines) {
                travellersOutput.println(line);
            }
            travellersOutput.close();
            logger.info(travellersFile.getAbsolutePath());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
