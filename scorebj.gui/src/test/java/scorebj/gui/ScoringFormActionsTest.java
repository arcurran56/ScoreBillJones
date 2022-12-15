package scorebj.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import scorebj.model.Competition;
import scorebj.model.DataStore;
import scorebj.model.DataStoreException;
import scorebj.pairing.PairingTableModel;
import scorebj.traveller.TravellerTableModel;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ScoringFormActionsTest {

    DataStore mockDataStore = mock(DataStore.class);

    private class ScoringFormActionsStub extends ScoringFormActions{
        @Override
        void createDataStore(){
            dataStore = mockDataStore;
        }
    }
    private final ScoringFormActions actions = new ScoringFormActions();
    private final CurrentTravellerBean travellerBean = new CurrentTravellerBean();
    private final CurrentCompetitionBean compBean = new CurrentCompetitionBean();

    Competition mockedCompetition1 = mock(Competition.class);
    Competition mockedCompetition2 = mock(Competition.class);
    TravellerTableModel mockedTTM = mock(TravellerTableModel.class);
    PairingTableModel mockedPTM = mock(PairingTableModel.class);
    DefaultComboBoxModel<String> mockedCBM = (DefaultComboBoxModel<String>) mock(DefaultComboBoxModel.class);

    @BeforeEach
    void setUp() throws DataStoreException {
        actions.init(mockedTTM,mockedPTM,mockedCBM);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Disabled
    void backButtonActionPerformed() {
        compBean.setSelectedCompetitionName("Test Competition 1");
        compBean.setCurrentSets("5");
        compBean.setCurrentBoardsPerSet("16");
        compBean.setCurrentNoPairs("10");
        travellerBean.setBoard("3");
        travellerBean.setSet("4");

        actions.backButtonActionPerformed(travellerBean);

        assertAll("back", () -> {
            assertEquals(2, travellerBean.getBoard());
        });

    }

    @Test
    void forwardButtonActionPerformed() {
    }

    @Test
    void goButtonActionPerformed() {
    }

    @Test
    void travellerTableChangedAction() {
    }

    @Test
    void pairingTableChangedAction() {
    }

    @Test
    void saveButtonActionPerformed() {
    }

    @Test
    void compComboBoxActionPerformed() {
    }

    @Test
    void addCompActionPerformed() {
    }

    @Test
    void deleteCompActionPerformed() {
    }
}