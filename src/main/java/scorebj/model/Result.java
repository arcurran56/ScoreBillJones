package scorebj.model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Result {
    private final File persistenceLocation;
    private int noPairs;
    private int noSets;
    private int noBoardsPerSet;
    private final int[][] detailedResults;
    private final int[][] summaryBySet;
    private final int[] grandTotals;
    private final int[][] opponents;
    private List<String> pairings;
    private final int[][] pairingsMatrix;

    public Result(int noPairs, int noSets, int noBoardsPerSet) {
        this.persistenceLocation = DataStore.getPersistenceLocation();
        this.noPairs = noPairs;
        this.noSets = noSets;
        this.noBoardsPerSet = noBoardsPerSet;

        detailedResults = new int[this.noPairs][this.noSets * this.noBoardsPerSet];
        summaryBySet = new int[this.noPairs][this.noSets];
        grandTotals = new int[this.noPairs];
        opponents = new int[this.noPairs][this.noSets];
        pairingsMatrix = new int[noPairs][noPairs];
    }

    public void collate(List<String> pairings, Traveller[] travellers) {

        this.pairings = pairings;

        Traveller traveller;
        int nsPair;
        int ewPair;
        int nsMPs;
        int ewMPs;
        BoardId boardId = new BoardId(noSets, noBoardsPerSet);

        for (int index = 0; index < noSets * noBoardsPerSet; index++) {
            traveller = travellers[index];
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

                        detailedResults[nsPair - 1][index] = nsMPs;
                        detailedResults[ewPair - 1][index] = ewMPs;

                        summaryBySet[nsPair - 1][boardId.getSet() - 1] += nsMPs;
                        summaryBySet[ewPair - 1][boardId.getSet() - 1] += ewMPs;

                        grandTotals[nsPair - 1] += nsMPs;
                        grandTotals[ewPair - 1] += ewMPs;

                        opponents[nsPair - 1][boardId.getSet() - 1] = ewPair;
                        opponents[ewPair - 1][boardId.getSet() - 1] = nsPair;

                        pairingsMatrix[nsPair-1][ewPair-1] += nsMPs;
                        pairingsMatrix[ewPair-1][nsPair-1] += ewMPs;

                    }

                }

            }
            boardId.next();

        }

    }

    public void printDetails() throws IOException {
        File outputFile = new File(persistenceLocation, "details.csv");
        PrintWriter output = new PrintWriter(outputFile);

        StringBuilder outputLine1 = new StringBuilder(",");
        StringBuilder outputLine2 = new StringBuilder(",");
        BoardId boardId = new BoardId(noSets, noBoardsPerSet);
        int loopCount = 0;
        while (loopCount<noSets*noBoardsPerSet) {
            outputLine1.append(",").append(boardId.getSet());
            outputLine2.append(",").append(boardId.getBoard());
            boardId.next();
            loopCount++;
        }
        output.println(outputLine1);
        output.println(outputLine2);

        for (int pair = 1; pair < noPairs + 1; pair++) {
            outputLine1 = new StringBuilder(Integer.toString(pair));
            outputLine1.append(",").append(pairings.get(pair-1));
            for (int id = 0; id < noSets * noBoardsPerSet; id++) {
                outputLine1.append(",").append(detailedResults[pair - 1][id]);
            }
            output.println(outputLine1);

        }
        output.close();
    }

    public void printSummary() throws IOException {
        File outputFile = new File(persistenceLocation, "summary.csv");
        PrintWriter output = new PrintWriter(outputFile);

        StringBuilder outputLine1 = new StringBuilder(",");
        for(int set=1;set<noSets+1;set++){
            outputLine1.append(",").append(set);
        }
        outputLine1.append(",TOTAL");
        output.println(outputLine1);

        for (int pair = 1; pair < noPairs + 1; pair++) {
            outputLine1 = new StringBuilder(Integer.toString(pair));
            outputLine1.append(",").append(pairings.get(pair-1));
            for (int set = 1; set < noSets + 1; set++) {
                outputLine1.append(",")
                        .append(summaryBySet[pair - 1][set - 1])
                        .append(" (v")
                        .append(opponents[pair - 1][set - 1])
                        .append(")");
            }
            outputLine1.append(",").append(grandTotals[pair - 1]);
            output.println(outputLine1);

        }
        output.close();
    }
    public void printMatrix() throws IOException {
        File outputFile = new File(persistenceLocation, "matrix.csv");
        PrintWriter output = new PrintWriter(outputFile);

        StringBuilder outputLine1 = new StringBuilder(",");
        for(int pair=1;pair<noPairs+1;pair++){
            outputLine1.append(",").append(pair);
        }
        outputLine1.append(",TOTAL");
        output.println(outputLine1);

        for (int pair1 = 1; pair1 < noPairs + 1; pair1++) {
            outputLine1 = new StringBuilder(Integer.toString(pair1));
            outputLine1.append(",").append(pairings.get(pair1-1));
            for (int pair2 = 1; pair2 < noPairs + 1; pair2++) {
                outputLine1.append(",")
                        .append(pairingsMatrix[pair1 - 1][pair2 - 1]);

            }
            outputLine1.append(",").append(grandTotals[pair1 - 1]);
            output.println(outputLine1);

        }
        output.close();
    }

}
