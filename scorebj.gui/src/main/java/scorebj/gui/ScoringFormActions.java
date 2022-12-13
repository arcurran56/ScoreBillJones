package scorebj.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scorebj.model.*;
import scorebj.output.Results;
import scorebj.pairing.PairingTableModel;
import scorebj.traveller.TravellerTableModel;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import java.util.List;
import java.util.*;
import java.util.function.Function;

public class ScoringFormActions {

    private static final Logger logger = LogManager.getLogger();
    private static boolean testMode;
    static DataStore dataStore;
    private DefaultComboBoxModel<String> compComboBoxModel;

    public static void setTestMode(boolean mode) {
        testMode = mode;
    }

    public final PairingTableModel getPairingTableModel() {
        return pairingTableModel;
    }


    private TravellerTableModel travellerTableModel;
    private Competition competition;
    private BoardId currentBoardId;

    private PairingTableModel pairingTableModel;


    public ScoringFormActions() {
    }

    public void init(TravellerTableModel travellerTableModel,
                     PairingTableModel pairingTableModel,
                     DefaultComboBoxModel<String> compComboBoxModel) throws DataStoreException {

        logger.debug("Initialisation...");
        this.travellerTableModel = travellerTableModel;
        this.pairingTableModel = pairingTableModel;
        this.compComboBoxModel = compComboBoxModel;

        createDataStore();

    }

    void createDataStore() {
        try {
            DataStore.setTestMode(testMode);
            dataStore = DataStore.create();
        } catch (DataStoreException e) {
            throw new RuntimeException(e);
        }
    }

    private void displaySelectedCompetition(ScoringBean scoringBean) {
        String competitionName = scoringBean.getSelectedCompetitionName();

        logger.debug("Fetching " + competitionName);

        competition = dataStore.getCompetition(competitionName);

        displayCurrentCompetition(scoringBean);
    }


    private void displayCurrentCompetition(ScoringBean scoringBean) {
        if (competition != null) {
           List<String> pairings = null;

            int noSets = competition.getNoSets();
            int noPairs = competition.getNoPairs();
            int noBoardsPerSet = competition.getNoBoardsPerSet();

            logger.debug("Displaying " + competition);

            scoringBean.setCurrentSets(Integer.toString(noSets));
            scoringBean.setCurrentNoPairs(Integer.toString(noPairs));
            scoringBean.setCurrentBoardsPerSet(Integer.toString(noBoardsPerSet));

            BoardId boardId = new BoardId(noSets, noBoardsPerSet);

            displayTraveller(boardId);
            
            displayPairings();

            scoringBean.setNewSet("1");
            scoringBean.setNewBoard("1");

            //Blank new Competition fields.
            scoringBean.setNewCompetitionName("");
            scoringBean.setNewSets("");
            scoringBean.setNewBoardsPerSet("");
            scoringBean.setNewNoPairs("");

            String completionStatus = travellerTableModel.getCompletionStatus();
            scoringBean.setCompletionStatus(completionStatus);

            logger.debug("Completion status: " + completionStatus);

            String progress = competition.getProgress();
            scoringBean.setProgress(progress);

            logger.debug("Progress: " + progress);

            String logLine = "Selected: " + competition;

            logger.debug(logLine);

        } else {
            logger.error("Attempt to display null Competition.");
        }

    }

    private void displayPairings() {
        List<String> pairings;
        pairings = competition.getPairings();
        logger.debug("Displaying " + pairings.size() + " pairings.");
        this.pairingTableModel.setNoPairs(competition.getNoPairs());
        this.pairingTableModel.setPairings(pairings);
    }

    public void backButtonActionPerformed(ScoringBean scoringBean) {
        logger.debug("back");
        navigateTraveller(scoringBean, BoardId::prev);

    }

    public void forwardButtonActionPerformed(ScoringBean scoringBean) {
        logger.debug("fwd");
        navigateTraveller(scoringBean, BoardId::next);

    }


    public void goButtonActionPerformed(ScoringBean scoringBean) {
        logger.debug("go");

        navigateTraveller(scoringBean, (bd) -> bd.select(
                parseInt(scoringBean.getNewSet()),
                parseInt(scoringBean.getNewBoard())));

    }

    public void travellerTableChangedAction(ScoringBean scoringBean,
                                            TravellerTableModel travellerTableModel,
                                            int firstRow, int column) {
        logger.debug("travellerTableChangedAction method: " + travellerTableModel);

        scoringBean.setCompletionStatus(travellerTableModel.getCompletionStatus());
        scoringBean.setProgress(competition.getProgress());


    }

    public void pairingTableChangedAction(TableModelEvent e) {
        PairingTableModel pairingTableModel = (PairingTableModel) e.getSource();
        logger.debug("Pairing table changed from " + e.getSource());
        List<String> pairings = pairingTableModel.getPairings();
        competition.setPairings(pairings);

        dataStore.persist(competition);

    }

    public void publishButtonActionPerformed() {
        logger.debug("Publish...");
        dataStore.persist(competition);
        Results results;
        try {
            results = new Results();
            results.save(competition);
        } catch (DataStoreException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void compComboBoxActionPerformed(ScoringBean scoringBean) {
        logger.debug("compComboBox selection set to " + compComboBoxModel.getSelectedItem());
        //Get selected Competition name and save key for later...

        //String currentCompetitionName;
        String newCompetitionSelection = (String) compComboBoxModel.getSelectedItem();
        logger.debug(newCompetitionSelection + " selected...");

        displaySelectedCompetition(scoringBean);

        //Fetch newly chosen Competition.
        logger.debug("...fetching " + newCompetitionSelection);
        competition = dataStore.getCompetition(newCompetitionSelection);


    }

    public void addCompActionPerformed(ScoringBean scoringBean) {
        logger.debug("Add...");

        String newCompetitionName = scoringBean.getNewCompetitionName();

        //Create new empty Competition and save.
        logger.debug("...adding " + newCompetitionName);

        Competition newCompetition = new Competition();
        newCompetition.setCompetitionName(scoringBean.getNewCompetitionName());
        newCompetition.setNoSets(parseInt(scoringBean.getNewSets()));
        newCompetition.setNoBoardsPerSet(parseInt(scoringBean.getNewBoardsPerSet()));
        newCompetition.setNoPairs(parseInt(scoringBean.getNewNoPairs()));
        newCompetition.initialise();

        dataStore.persist(newCompetition);

        compComboBoxModel.addElement(newCompetitionName);

        StringBuilder logLine = new StringBuilder()
                .append("Competition: ")
                .append(newCompetition)
                .append(" created.");


        logger.debug(logLine);
    }

    public void deleteCompActionPerformed(ScoringBean scoringBean) {
        logger.debug("Deleting..." + scoringBean.getSelectedCompetitionName());

        dataStore.delete(scoringBean.getSelectedCompetitionName());
        competition = new Competition();

        compComboBoxModel.removeElement(scoringBean.getSelectedCompetitionName());

    }

    public void clearButtonActionPerformed(ScoringBean scoringBean) {
        logger.debug("Clear");

        Traveller displayedTraveller = travellerTableModel.getTraveller();
        displayedTraveller.clear();
        travellerTableModel.setTraveller(displayedTraveller);
        BoardId board = displayedTraveller.getBoardId();

        if (competition != null) {
            competition.getTraveller(board).copy(displayedTraveller);
            dataStore.persist(competition);
        }
        //travellerTableModel.setTraveller(traveller);
        scoringBean.setCompletionStatus(travellerTableModel.getCompletionStatus());
        scoringBean.setProgress(competition.getProgress());
    }

    private void navigateTraveller(ScoringBean scoringBean, Function<BoardId, BoardId> navigateTo) {
        //Save current Traveller.
        saveTraveller();

        //Fetch specified traveller
        BoardId boardId = travellerTableModel.getBoardId();
        BoardId newBoardId = navigateTo.apply(boardId);
        logger.debug("Displaying Traveller for board: " + boardId);

        displayTraveller(newBoardId);

        scoringBean.setNewSet(Integer.toString(newBoardId.getSet()));
        scoringBean.setNewBoard(Integer.toString(newBoardId.getBoard()));
        scoringBean.setCompletionStatus(travellerTableModel.getCompletionStatus());
        scoringBean.setProgress(competition.getProgress());

    }

    private int parseInt(String string) {
        int result;
        try {
            result = Integer.parseInt(string);
        } catch (NumberFormatException e) {
            logger.debug("Can't parse string: " + string);
            result = 0;
        }
        return result;
    }

    private void saveTraveller() {
        Traveller formTraveller = travellerTableModel.getTraveller();
        BoardId boardId = formTraveller.getBoardId();

        if (competition!=null) {
            logger.debug("Saving " + boardId + " to " + competition.getCompetitionName());

            if (boardId != null) {
                Traveller savedTraveller = competition.getTraveller(boardId);
                savedTraveller.copy(formTraveller);
                dataStore.persist(competition);
            }
        }
    }

    private void displayTraveller(BoardId boardId) {
        logger.debug("displayTraveller: " + boardId);
        Traveller traveller = competition.getTraveller(boardId);
        travellerTableModel.setTraveller(traveller);
    }

    public Set<String> getCompetitionNames() {
        return dataStore.getCompetitionNames();
    }

    public void saveButtonActionPerformed() {
        logger.debug("Save...");
        saveTraveller();
    }
}



