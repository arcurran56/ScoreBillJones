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

    private void displaySelectedCompetition(CurrentCompetitionBean currentCompetitionBean) {
        CurrentTravellerBean currentTravellerBean = new CurrentTravellerBean();
        displaySelectedCompetition(currentCompetitionBean,currentTravellerBean);
    }
    private void displaySelectedCompetition(CurrentCompetitionBean currentCompetitionBean,
                                            CurrentTravellerBean currentTravellerBean) {

        String competitionName = currentCompetitionBean.getSelectedCompetitionName();
        logger.debug("Fetching " + competitionName);

        competition = dataStore.getCompetition(competitionName);

        if (competition == null) {
            competition = new Competition();
        }
        displayCurrentCompetition(currentCompetitionBean);

        int noSets = competition.getNoSets();
        int noBoardsPerSet = competition.getNoBoardsPerSet();
        BoardId boardId = new BoardId(noSets, noBoardsPerSet);

        displayTraveller(boardId);

        displayPairings();

        String completionStatus = competition.getTraveller(boardId).getCompletionStatus();
        logger.debug("Completion status: " + completionStatus);

        String progress = competition.getProgress();
        currentTravellerBean.setProgress(progress);

        logger.debug("Progress: " + progress);

        String logLine = "Selected: " + competition;
        logger.debug(logLine);

    }

    private void displayCurrentCompetition(CurrentCompetitionBean currentCompetitionBean) {
        if (competition != null) {
            List<String> pairings = null;

            int noSets = competition.getNoSets();
            int noPairs = competition.getNoPairs();
            int noBoardsPerSet = competition.getNoBoardsPerSet();

            logger.debug("Displaying " + competition);

            currentCompetitionBean.setCurrentSets(Integer.toString(noSets));
            currentCompetitionBean.setCurrentNoPairs(Integer.toString(noPairs));
            currentCompetitionBean.setCurrentBoardsPerSet(Integer.toString(noBoardsPerSet));
        }

    }

    private void displayPairings() {
        List<String> pairings;
        pairings = competition.getPairings();
        logger.debug("Displaying " + pairings.size() + " pairings.");
        this.pairingTableModel.setNoPairs(competition.getNoPairs());
        this.pairingTableModel.setPairings(pairings);
    }

    public void backButtonActionPerformed(CurrentTravellerBean bean) {
        logger.debug("back");
        navigateTraveller(bean, BoardId::prev);

    }

    public void forwardButtonActionPerformed(CurrentTravellerBean bean) {
        logger.debug("fwd");
        navigateTraveller(bean, BoardId::next);

    }


    public void goButtonActionPerformed(CurrentTravellerBean bean) {
        logger.debug("go");

        navigateTraveller(bean, (bd) -> bd.select(
                parseInt(bean.getSet()),
                parseInt(bean.getBoard())));

    }

    public void travellerTableChangedAction(CurrentTravellerBean bean,
                                            TravellerTableModel travellerTableModel,
                                            int firstRow, int column) {
        logger.debug("travellerTableChangedAction method: " + travellerTableModel);

        bean.setCompletionStatus(travellerTableModel.getCompletionStatus());
        bean.setProgress(competition.getProgress());


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

    public void compComboBoxActionPerformed(CurrentCompetitionBean currentCompetitionBean) {
        logger.debug("compComboBox selection set to " + compComboBoxModel.getSelectedItem());

        //String currentCompetitionName;
        String newCompetitionSelection = (String) compComboBoxModel.getSelectedItem();
        logger.debug(newCompetitionSelection + " selected...");

        currentCompetitionBean.setSelectedCompetitionName(newCompetitionSelection);

        if (newCompetitionSelection==null){
            competition = new Competition();
        }
        /*scoringBean.setCurrentNoPairs(competition.getNoPairs());
        scoringBean.setCurrentSets(competition.getNoSets());
        scoringBean.setNewBoardsPerSet(competition.getNoBoardsPerSet());*/
        displaySelectedCompetition(currentCompetitionBean);

    }

    public void addCompActionPerformed(NewCompetitionBean currentTravellerBean) {
        logger.debug("Add...");

        String newCompetitionName = currentTravellerBean.getNewCompetitionName();

        //Create new empty Competition and save.
        logger.debug("...adding " + newCompetitionName);

        Competition newCompetition = new Competition();
        newCompetition.setCompetitionName(currentTravellerBean.getNewCompetitionName());
        newCompetition.setNoSets(parseInt(currentTravellerBean.getNewSets()));
        newCompetition.setNoBoardsPerSet(parseInt(currentTravellerBean.getNewBoardsPerSet()));
        newCompetition.setNoPairs(parseInt(currentTravellerBean.getNewNoPairs()));
        newCompetition.initialise();

        //scoringBean.setCurrentSets(newCompetition.getNoSets());


        dataStore.persist(newCompetition);

        compComboBoxModel.addElement(newCompetitionName);

        StringBuilder logLine = new StringBuilder()
                .append("Competition: ")
                .append(newCompetition)
                .append(" created.");


        logger.debug(logLine);
    }

    public void deleteCompActionPerformed(String name) {
        logger.debug("Deleting..." + name);

        dataStore.delete(name);
        competition = new Competition();

        compComboBoxModel.removeElement(name);

    }

    public void clearButtonActionPerformed(CurrentTravellerBean currentTravellerBean) {
        logger.debug("Clear");

        travellerTableModel.clear();
        Traveller displayedTraveller = travellerTableModel.getTraveller();
        BoardId board = displayedTraveller.getBoardId();

        if (competition != null) {
            competition.getTraveller(board).copy(displayedTraveller);
            dataStore.persist(competition);
        }
        //travellerTableModel.setTraveller(traveller);
        currentTravellerBean.setCompletionStatus(travellerTableModel.getCompletionStatus());
        currentTravellerBean.setProgress(competition.getProgress());
    }

    private void navigateTraveller(CurrentTravellerBean currentTravellerBean, Function<BoardId, BoardId> navigateTo) {
        //Save current Traveller.
        saveTraveller();

        //Fetch specified traveller
        BoardId boardId = travellerTableModel.getBoardId();
        BoardId newBoardId = navigateTo.apply(boardId);
        logger.debug("Displaying Traveller for board: " + boardId);

        displayTraveller(newBoardId);

        currentTravellerBean.setSet(Integer.toString(newBoardId.getSet()));
        currentTravellerBean.setBoard(Integer.toString(newBoardId.getBoard()));
        currentTravellerBean.setCompletionStatus(travellerTableModel.getCompletionStatus());
        currentTravellerBean.setProgress(competition.getProgress());

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



