package scorebj.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scorebj.output.ResultSS;
import scorebj.output.SSRow;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Result {
    private final Logger logger = LogManager.getLogger();
    private final File persistenceLocation;
    private final int noPairs;
    private final int noSets;
    private final int noBoardsPerSet;
    private int[][] detailedResults;
    private int[][] summaryBySet;
    private int[] grandTotals;
    private int[][] opponents;
    private List<String> pairings;
    private int[][] pairingsMatrix;

    public Result(int noPairs, int noSets, int noBoardsPerSet) {
        DataStore dataStore = DataStore.create();
        this.persistenceLocation = dataStore.getPersistenceLocation();
        this.noPairs = noPairs;
        this.noSets = noSets;
        this.noBoardsPerSet = noBoardsPerSet;


    }

    public void collate(List<String> pairings, List<Traveller> travellers) {

        this.pairings = pairings;
        //noPairs = pairings.size();

        detailedResults = new int[this.noPairs][this.noSets * this.noBoardsPerSet];
        summaryBySet = new int[this.noPairs][this.noSets];
        grandTotals = new int[this.noPairs];
        opponents = new int[this.noPairs][this.noSets];
        pairingsMatrix = new int[this.noPairs][this.noPairs];

        Traveller traveller;
        int nsPair;
        int ewPair;
        int nsMPs;
        int ewMPs;
        BoardId boardId = new BoardId(noSets, noBoardsPerSet);

        StringBuilder logBuilder = new StringBuilder()
                .append(noPairs)
                .append(", ")
                .append(noSets)
                .append(", ")
                .append(noBoardsPerSet);
        logger.debug(logBuilder.toString());

        for (int index = 0; index < noSets * noBoardsPerSet; index++) {
            traveller = travellers.get(index);
            for (ScoreLine scoreLine : traveller.getScoreLines()) {
                if (scoreLine != null) {
                    if (scoreLine.getNsPair() != null
                            && scoreLine.getEwPair() != null
                            && scoreLine.getNsMPs() != null
                            && scoreLine.getEwMPs() != null) {
                        nsPair = scoreLine.getNsPair();
                        ewPair = scoreLine.getEwPair();
                        nsMPs = scoreLine.getNsMPs();
                        ewMPs = scoreLine.getEwMPs();

                        logBuilder = new StringBuilder()
                                .append(index)
                                .append(": NS ")
                                .append(nsPair)
                                .append("-")
                                .append(nsMPs)
                                .append("pts ")
                                .append(ewPair)
                                .append("-")
                                .append(ewMPs)
                                .append("pts.");
                        logger.debug(logBuilder.toString());
                        detailedResults[nsPair - 1][index] = nsMPs;
                        detailedResults[ewPair - 1][index] = ewMPs;

                        summaryBySet[nsPair - 1][boardId.getSet() - 1] += nsMPs;
                        summaryBySet[ewPair - 1][boardId.getSet() - 1] += ewMPs;

                        grandTotals[nsPair - 1] += nsMPs;
                        grandTotals[ewPair - 1] += ewMPs;

                        opponents[nsPair - 1][boardId.getSet() - 1] = ewPair;
                        opponents[ewPair - 1][boardId.getSet() - 1] = nsPair;

                        pairingsMatrix[nsPair - 1][ewPair - 1] += nsMPs;
                        pairingsMatrix[ewPair - 1][nsPair - 1] += ewMPs;

                    }

                }

            }
            boardId = boardId.next();

        }

    }

    public void printDetails() throws IOException {
        File outputFile = new File(persistenceLocation, "details.csv");
        PrintWriter output = new PrintWriter(outputFile);

        StringBuilder logLine = new StringBuilder()
                .append(noPairs)
                .append(",")
                .append(noSets)
                .append(",")
                .append(noBoardsPerSet);
        logger.debug(logLine.toString());

        StringBuilder outputLine1 = new StringBuilder(",");
        StringBuilder outputLine2 = new StringBuilder(",");
        BoardId boardId = new BoardId(noSets, noBoardsPerSet);
        int loopCount = 0;
        while (loopCount < noSets * noBoardsPerSet) {
            outputLine1.append(",").append(boardId.getSet());
            outputLine2.append(",").append(boardId.getBoard());
            boardId = boardId.next();
            loopCount++;
        }
        output.println(outputLine1);
        output.println(outputLine2);

        for (int pair = 1; pair < noPairs + 1; pair++) {
            outputLine1 = new StringBuilder(Integer.toString(pair));
            outputLine1.append(",").append(pairings.get(pair - 1));
            for (int id = 0; id < noSets * noBoardsPerSet; id++) {
                outputLine1.append(",").append(detailedResults[pair - 1][id]);
            }
            output.println(outputLine1);

        }
        output.close();
    }

    /**
     * printSummary
     * <p>
     * Create csv file, summary.csv, containing table of each pair's results for each
     * round, showing opponents.
     *
     * @throws IOException
     */
    public void printSummary() throws IOException {
        File outputFile = new File(persistenceLocation, "summary.csv");
        PrintWriter output = new PrintWriter(outputFile);

        StringBuilder logLine = new StringBuilder()
                .append(noPairs)
                .append(",")
                .append(noSets)
                .append(",")
                .append(noBoardsPerSet);
        logger.debug(logLine.toString());

        StringBuilder outputLine1 = new StringBuilder("Pair No,Names");
        for (int set = 1; set < noSets + 1; set++) {
            outputLine1
                    .append(",")
                    .append("Set ")
                    .append(set);
        }

        outputLine1.append(",TOTAL");
        logger.debug(outputLine1);
        output.println(outputLine1);

        for (int pair = 1; pair < noPairs + 1; pair++) {
            logLine = new StringBuilder()
                    .append(pair);
            logger.debug(logLine.toString());

            outputLine1 = new StringBuilder(Integer.toString(pair));
            outputLine1.append(",").append(pairings.get(pair - 1));
            for (int set = 1; set < noSets + 1; set++) {
                outputLine1.append(",\"")
                        .append(pairings.get(opponents[pair - 1][set - 1] - 1))
                        .append("\n")
                        .append(summaryBySet[pair - 1][set - 1])
                        .append("\"");
            }
            outputLine1.append(",")
                    .append(grandTotals[pair - 1]);
            output.println(outputLine1);

        }
        output.close();
    }

    public void printMatrix() throws IOException {
        File outputFile = new File(persistenceLocation, "matrix.csv");
        PrintWriter output = new PrintWriter(outputFile);

        StringBuilder logLine = new StringBuilder()
                .append(noPairs)
                .append(",")
                .append(noSets)
                .append(",")
                .append(noBoardsPerSet);
        logger.debug(logLine.toString());

        StringBuilder outputLine1 = new StringBuilder(",");
        for (int pair = 1; pair < noPairs + 1; pair++) {
            outputLine1.append(",").append(pair);
        }
        outputLine1.append(",TOTAL");
        output.println(outputLine1);

        for (int pair1 = 1; pair1 < noPairs + 1; pair1++) {
            outputLine1 = new StringBuilder(Integer.toString(pair1));
            outputLine1.append(",").append(pairings.get(pair1 - 1));
            for (int pair2 = 1; pair2 < noPairs + 1; pair2++) {
                outputLine1.append(",")
                        .append(pairingsMatrix[pair1 - 1][pair2 - 1]);
            }
            outputLine1.append(",").append(grandTotals[pair1 - 1]);
            output.println(outputLine1);

        }
        output.close();
    }

    /**
     * createSummaryTable
     *
     * Creates a table of results
     */
    public ResultSS createSummaryTable() {
        //File outputFile = new File(persistenceLocation, "summary.csv");
        //PrintWriter output = new PrintWriter(outputFile);

        StringBuilder logLine = new StringBuilder("Creating summary table for spreadsheet...")
                .append(noPairs)
                .append(",")
                .append(noSets)
                .append(",")
                .append(noBoardsPerSet);
        logger.debug(logLine.toString());

        ResultSS resultSS = new ResultSS();

        List<String> ssHeader = resultSS.getSsHeaderRow();
        ssHeader.add("Rank");
        ssHeader.add("Names");

        String colHeader;
        for (int set = 1; set < noSets + 1; set++) {
            colHeader = "Set " + Integer.toString(set);
            ssHeader.add(colHeader);
        }
        ssHeader.add("TOTAL");

        StringBuilder line = new StringBuilder("Header: ")    ;
        ssHeader.forEach(str -> line.append("|").append(str));
        logger.debug(line);

        SSRow ssRow;
        StringBuilder cellContent;
        List<String> setResult;
        for (int pair = 1; pair < noPairs + 1; pair++) {
            ssRow = new SSRow();
            ssRow.setPair(pairings.get(pair - 1));
            setResult = ssRow.getSetResult();

            for (int set = 1; set < noSets + 1; set++) {
                cellContent = new StringBuilder();
                cellContent.append(pairings.get(opponents[pair - 1][set - 1] - 1))
                        .append("\n")
                        .append(summaryBySet[pair - 1][set - 1])
                        .append("\"");
                setResult.add(cellContent.toString());
            }
            ssRow.setTotal(grandTotals[pair - 1]);
            resultSS.getSsRows().add(ssRow);
        }
        resultSS.getSsHeaderRow().sort(null);
        logger.debug(resultSS.getSsRows().size() + " lines in table.");

        //Log debug lines.
        StringBuilder bodyLine;
        for(SSRow r: resultSS.getSsRows()) {
            bodyLine = new StringBuilder("Body: ");
            bodyLine.append("|").append(r.getPair());
            StringBuilder sb = new StringBuilder();
            r.getSetResult().forEach(str -> sb.append("|").append(str.replace("\n", ",")));
            bodyLine.append(sb);
            bodyLine.append(r.getTotal());
            logger.debug(bodyLine);
        }
        return resultSS;
    }
}
