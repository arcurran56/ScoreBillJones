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
    static DataStore dataStore;
    private DefaultComboBoxModel<String> compComboBoxModel;

    public final PairingTableModel getPairingTableModel() {
        return pairingTableModel;
    }


    private TravellerTableModel travellerTableModel;
    private Competition competition;
    private BoardId currentBoardId;

    private PairingTableModel pairingTableModel;


    public ScoringFormActions() {
    }

    public void init(ScoringBean scoringBean,
                     TravellerTableModel travellerTableModel,
                     PairingTableModel pairingTableModel,
                     DefaultComboBoxModel<String> compComboBoxModel) throws DataStoreException {

        logger.debug("Initialisation...");
        this.travellerTableModel = travellerTableModel;
        this.pairingTableModel = pairingTableModel;
        this.compComboBoxModel = compComboBoxModel;

        createDataStore();

        //Get first Competition for display...
        if (!("".equals(scoringBean.getCurrentCompetitionName()))) {
            logger.debug("initial competition: " + scoringBean.getCurrentCompetitionName());
            competition = dataStore.getCompetition(scoringBean.getCurrentCompetitionName());

            scoringBean.setCurrentSets(Integer.toString(competition.getNoSets()));
            scoringBean.setCurrentBoardsPerSet(Integer.toString(competition.getNoBoardsPerSet()));
            scoringBean.setCurrentNoPairs(Integer.toString(competition.getNoPairs()));

            displaySelectedCompetition(scoringBean);
        }
    }

    void createDataStore() {
        try {
            dataStore = DataStore.create();
        } catch (DataStoreException e) {
            throw new RuntimeException(e);
        }
    }

    private void displaySelectedCompetition(ScoringBean scoringBean) {
        String competitionName = scoringBean.getCurrentCompetitionName();

        logger.debug("Displaying " + competitionName);

        int noSets = 0;
        int noPairs = 0;
        int noBoardsPerSet = 0;
        List<String> pairings = null;
        StringBuilder builder = new StringBuilder("Displaying ");

        if (competition != null) {

            noSets = competition.getNoSets();
            noPairs = competition.getNoPairs();
            noBoardsPerSet = competition.getNoBoardsPerSet();

            builder.append(competitionName)
                    .append(", ")
                    .append(noSets)
                    .append(" sets of ")
                    .append(noBoardsPerSet)
                    .append(" boards, ")
                    .append(noPairs)
                    .append(" pairs");
            logger.debug(builder);

            scoringBean.setCurrentSets(Integer.toString(noSets));
            scoringBean.setCurrentNoPairs(Integer.toString(noPairs));
            scoringBean.setCurrentBoardsPerSet(Integer.toString(noBoardsPerSet));

            BoardId boardId = new BoardId(noSets, noBoardsPerSet);

            Traveller traveller = competition.getTraveller(boardId);
            //travellerTableModel.setNoPairs(noPairs);
            this.travellerTableModel.setTraveller(traveller);

            pairings = competition.getPairings();
            this.pairingTableModel.setNoPairs(noPairs);
            this.pairingTableModel.setPairings(pairings);

            scoringBean.setNewSet("1");
            scoringBean.setNewBoard("1");

            //Blank new Competition fields.
            scoringBean.setNewCompetitionName("");
            scoringBean.setNewSets("");
            scoringBean.setNewBoardsPerSet("");
            scoringBean.setNewNoPairs("");

            String completionStatus = traveller.getCompetionStatus();
            scoringBean.setTravellerComplete(completionStatus);
            logger.debug("Completion status: " + completionStatus);

            String progress = competition.getProgress();
            scoringBean.setProgress(progress);
            logger.debug("Progress: " + progress);

            //compComboBoxModel.addElement(competitionName));
            assert pairings != null;
            StringBuilder logLine = new StringBuilder()
                    .append("Selected Competition: ")
                    .append(competition.getCompetitionName())
                    .append(", pairs: ")
                    .append(noPairs)
                    .append("(")
                    .append(pairings.size())
                    .append(")");

            logger.debug(logLine);

        } else {
            logger.error("Attempt to display null Competition.");
        }

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

        dataStore.persist(competition);

    }

    public void pairingTableChangedAction(TableModelEvent e) {
        PairingTableModel pairingTableModel = (PairingTableModel) e.getSource();
        logger.debug("Pairing table changed from " + e.getSource());
        List<String> pairings = pairingTableModel.getPairings();
        competition.setPairings(pairings);

        dataStore.persist(competition);

    }

    public void saveButtonActionPerformed() {
        logger.debug("Save...");
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
        //Save currently displayed traveller.

        //Get selected Competition name and save key for later...
        String currentCompetitionName = (String) competition.getCompetitionName();
        String newCompetitionName = (String) compComboBoxModel.getSelectedItem();

        logger.debug(currentCompetitionName + " selected...");

        //Save current Traveller in old Competition.
        Traveller savedTraveller;
        Traveller newTraveller = travellerTableModel.getTraveller();

        BoardId boardId = newTraveller.getBoardId();
        if (competition != null && boardId != null) {

            savedTraveller = competition.getTraveller(boardId);
            savedTraveller.copy(newTraveller);

            //...and pairings
            competition.setPairings(pairingTableModel.getPairings());

            //...and persist.
            dataStore.persist(competition);
        } else {
            logger.debug("...traveller not saved.");
        }

        //Fetch newly chosen Competition.
        logger.debug("Fetching " + newCompetitionName);
        competition = dataStore.getCompetition(newCompetitionName);

        displaySelectedCompetition(scoringBean);


    }

    public void addCompActionPerformed(ScoringBean scoringBean) {
        logger.debug("Add...");

        String newCompetitionName = scoringBean.getNewCompetitionName();

        //Terminate current edit.

        //Create new empty Competition and save.
        logger.debug("...adding " + newCompetitionName);
        competition = new Competition();
        competition.setCompetitionName(scoringBean.getNewCompetitionName());

        int noPairs = parseInt(scoringBean.getNewNoPairs());

        competition.setNoSets(parseInt(scoringBean.getNewSets()));
        competition.setNoBoardsPerSet(parseInt(scoringBean.getNewBoardsPerSet()));
        competition.setNoPairs(parseInt(scoringBean.getNewNoPairs()));
        competition.initialise();

        dataStore.persist(competition);

        displaySelectedCompetition(scoringBean);

        //Display traveller for first board.
        BoardId boardId = new BoardId(competition.getNoSets(), competition.getNoBoardsPerSet());
        scoringBean.setNewSet(Integer.toString(boardId.getSet()));
        scoringBean.setNewBoard(Integer.toString(boardId.getBoard()));

        //Blank new Competition fields...
        scoringBean.setCurrentSets(scoringBean.getNewSets());
        scoringBean.setCurrentBoardsPerSet(scoringBean.getNewBoardsPerSet());
        scoringBean.setCurrentNoPairs(scoringBean.getNewBoardsPerSet());

        List<String> pairings = competition.getPairings();
        pairingTableModel.setPairings(pairings);

        StringBuilder logLine = new StringBuilder()
                .append("Competition:")
                .append(scoringBean.getNewCompetitionName())
                .append(", pairs: ")
                .append(noPairs)
                .append("(")
                .append(pairings.size())
                .append(")");

        logger.debug(logLine);

        //Update view.
        Traveller traveller = competition.getTraveller(boardId);
        travellerTableModel.setTraveller(traveller);

        scoringBean.setProgress(competition.getProgress());
        scoringBean.setTravellerComplete(traveller.getCompetionStatus());

        //travellerTableModel.setRowCount(savedTraveller.getScoreLines().size());

    }

    public void deleteCompActionPerformed(ScoringBean scoringBean) {
        logger.debug("Delete...");

        dataStore.delete(scoringBean.getCurrentCompetitionName());

        compComboBoxModel.removeElement(scoringBean.getCurrentCompetitionName());

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
        scoringBean.setTravellerComplete(displayedTraveller.getCompetionStatus());
        scoringBean.setProgress(competition.getProgress());
    }

    private void navigateTraveller(ScoringBean scoringBean, Function<BoardId, BoardId> navigateTo) {
        //Save current Traveller.
        saveTraveller();

        //Fetch specified traveller
        Traveller formTraveller = travellerTableModel.getTraveller();
        BoardId boardId = formTraveller.getBoardId();
        BoardId newBoardId = navigateTo.apply(boardId);
        logger.debug("New Traveller: " + boardId);
        scoringBean.setNewSet(Integer.toString(newBoardId.getSet()));
        scoringBean.setNewBoard(Integer.toString(newBoardId.getBoard()));

        //Update view.
        Traveller newTraveller = competition.getTraveller(newBoardId);
        if (newTraveller.isEmpty() && Objects.equals(boardId.getSet(), newBoardId.getSet())) {
            logger.debug("Traveller " + newBoardId.getSet() + "-" + newBoardId.getBoard() + " is blank.");
            newTraveller = formTraveller.generatePrefilled(newBoardId);
        }
        travellerTableModel.setTraveller(newTraveller);

        scoringBean.setTravellerComplete(newTraveller.getCompetionStatus());
        scoringBean.setProgress(competition.getProgress());

        dataStore.persist(competition);
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

    public void saveTraveller() {
        Traveller formTraveller = travellerTableModel.getTraveller();
        BoardId boardId = formTraveller.getBoardId();
        logger.debug("Saving " + boardId + " to " + competition.getCompetitionName());
        Traveller savedTraveller = competition.getTraveller(boardId);
        savedTraveller.copy(formTraveller);
        dataStore.persist(competition);

    }

    public Set<String> getCompetitionNames() {
        createDataStore();
        return dataStore.getCompetitionNames();
    }

}



