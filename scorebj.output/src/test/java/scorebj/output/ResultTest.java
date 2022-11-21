package scorebj.output;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import scorebj.model.Competition;
import scorebj.model.DataStoreException;
import scorebj.output.DataStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

class ResultTest {
    Logger logger = LogManager.getLogger();
    private final String[] DEFAULT_PAIRINGS = {"David Sherry & Salette Avison",
            "Liz Buckland & Jane Edge",
            "Jill Ryan & Ron Eyre-Brook",
            "Jill Hogge & Jane Durgan",
            "Wendy Goddin & Diane Jemmett",
            "Minnie Mouse & Mickey Mouse",
            "Julia Ellis & Sue Whitfield",
            "Clare Baker & Jill Aspinall",
            "Lisbeth Mousell & Vicky Wheeler",
            "Caroline Jones & Andrew Goodwin"};

    @Test
    void collate() throws IOException, DataStoreException {
        logger.debug("Collatimg...");
        DataStore dataStore = DataStore.create();
        Competition competition = dataStore.getCompetition("TestComp");
        List<String> pairings = Arrays.asList(DEFAULT_PAIRINGS);

        Result result = new Result(10, 5, 16);
        result.collate(pairings, competition.getTravellers());

        File detailsFile = new File(DataStore.getPersistenceLocation(), "TEST-details.csv");
        File summaryFile = new File(DataStore.getPersistenceLocation(), "TEST-summary.csv");
        File matrixFile = new File(DataStore.getPersistenceLocation(), "TEST-matrix.csv");
        File outputXlsxFile = new File( DataStore.getPersistenceLocation(), "TEST-results.xlsx");

        result.printDetails(detailsFile);
        result.printSummary(summaryFile);
        result.printMatrix(matrixFile);
    }

    @Test
    void printTravellers() throws FileNotFoundException, DataStoreException {
        logger.debug("Writing travellers...");
        DataStore dataStore = DataStore.create();
        Competition competition = dataStore.getCompetition("TestComp");
        List<String> strings = competition.travellersToText();
        File persistenceLocation = DataStore.getPersistenceLocation();
        File outputFile = new File(persistenceLocation, "TEST-travellers.csv");
        PrintWriter output = new PrintWriter(outputFile);

        for (String line: strings){
            output.println(line);
        }
        output.close();
    }

    @Test
    void createSummaryTable() throws DataStoreException {
        logger.debug("Creating summary table...");
        DataStore.setTestMode(true);
        DataStore dataStore = DataStore.create();
        Competition competition = dataStore.getCompetition("TestComp");
        List<String> pairings = Arrays.asList(DEFAULT_PAIRINGS);

        Result result = new Result((competition.getNoPairs()),
                competition.getNoSets(),
                competition.getNoBoardsPerSet());
        result.collate(pairings, competition.getTravellers());
        result.createSummaryTable();
    }

    @BeforeEach
    void setUp() {
        DataStore.setTestMode(true);
    }
}