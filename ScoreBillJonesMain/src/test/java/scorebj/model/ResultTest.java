package scorebj.model;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

class ResultTest {
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
    void collate() throws IOException {
        DataStore dataStore = DataStore.create();
        Competition competition = dataStore.getCompetition(0);
       // competition.setCompetitionName("Test");
       // competition.setNoSets(5);
        //competition.setNoBoardsPerSet(16);
        List<String> pairings = Arrays.asList(DEFAULT_PAIRINGS);

        Result result = new Result(10, 5, 16);
        result.collate(pairings, competition.getTravellers());
        result.printDetails();
        result.printSummary();
        result.printMatrix();
    }

    @Test
    void printTravellers() throws FileNotFoundException {
        DataStore dataStore = DataStore.create();
        Competition competition = dataStore.getCompetition(0);
        List<String> strings = competition.travellersToText();
        File persistenceLocation = dataStore.getPersistenceLocation();
        File outputFile = new File(persistenceLocation, "travellers.csv");
        PrintWriter output = new PrintWriter(outputFile);

        for (String line: strings){
            output.println(line);
        }
        output.close();
    }

    @Test
    void createSummaryTable() {
        DataStore dataStore = DataStore.create();
        Competition competition = dataStore.getCompetition(0);
        // competition.setCompetitionName("Test");
        // competition.setNoSets(5);
        //competition.setNoBoardsPerSet(16);
        List<String> pairings = Arrays.asList(DEFAULT_PAIRINGS);

        Result result = new Result((competition.getNoPairs()),
                competition.getNoSets(),
                competition.getNoBoardsPerSet());
        result.collate(pairings, competition.getTravellers());
        result.createSummaryTable();
    }
}