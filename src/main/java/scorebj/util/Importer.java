package scorebj.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scorebj.model.*;
import scorebj.traveller.Contract;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Importer {
    private final static Logger logger = LogManager.getLogger();

    enum ReadingState {LINE_COUNT, SCORE_LINE}

    private final static int NS_PAIR = 1;
    private final static int EW_PAIR = 2;
    private final static int CONTRACT = 12;
    private final static int PLAYED_BY = 13;
    private final static int TRICKS = 14;

    public Importer() {

    }

    public static void main(String[] args) throws IOException {
        String competitionName = args[0];
        int noSets = Integer.parseUnsignedInt(args[1]);
        int noBoardsPerSet = Integer.parseUnsignedInt(args[2]);
        int noPairs = Integer.parseUnsignedInt(args[3]);

        DataStore dataStore = DataStore.create();
        Competition competition = new Competition();
        competition.setCompetitionName(competitionName);
        competition.setNoSets(noSets);
        competition.setNoBoardsPerSet(noBoardsPerSet);
        competition.setNoPairs(noPairs);
        competition.initialise();

        StringBuilder builder = (new StringBuilder())
                .append(competitionName)
                .append(", ")
                .append(noSets)
                .append(", ")
                .append(noBoardsPerSet)
                .append(", ")
                .append(noPairs);
        logger.info(builder.toString());

        BoardId boardId = new BoardId(competition.getNoSets(), competition.getNoBoardsPerSet());
        File dataFolder = new File(System.getProperty("user.home"), "scorebj");
        File importFile = new File(dataFolder, "import-file.csv");
        BufferedReader reader = new BufferedReader(new FileReader(importFile));

        String importLine;
        int lineIndex = 0;
        int lineCount = 0;
        int travellerIndex = 0;

        importLine = reader.readLine();
        ReadingState readingState = ReadingState.LINE_COUNT;


        Traveller traveller = null;
        ScoreLine scoreLine;

        String[] importLineValues;

        while (importLine != null) {
            switch (readingState) {
                case LINE_COUNT:
                    lineCount = Integer.parseInt(importLine);
                    lineIndex = 0;
                    readingState = ReadingState.SCORE_LINE;
                    traveller = competition.getTraveller(boardId);
                    break;
                case SCORE_LINE:
                    importLineValues = importLine.split(",");

                    scoreLine = traveller.getScoreLine(lineIndex);
                    scoreLine.setNsPair(Integer.parseInt(importLineValues[NS_PAIR]));
                    scoreLine.setEwPair(Integer.parseInt(importLineValues[EW_PAIR]));
                    scoreLine.setContract(new Contract(importLineValues[CONTRACT].replace("\"", "")));
                    scoreLine.setPlayedBy(ScoreLine.Direction.valueOf(importLineValues[PLAYED_BY].replace("\"", "")));
                    scoreLine.setTricks(Integer.parseInt(importLineValues[TRICKS]));

                    traveller.scoreHand(lineIndex, boardId.getVulnerability(scoreLine.getPlayedBy()));

                    lineIndex++;
                    if (lineIndex == lineCount) {
                        readingState = ReadingState.LINE_COUNT;
                        boardId = boardId.next();
                    }
                    break;
            }
            importLine = reader.readLine();
        }
        dataStore.persist(competition);
        reader.close();
    }

}
