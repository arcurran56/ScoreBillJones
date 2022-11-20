package scorebj.gui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.mockito.Mock;
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
    private final ScoringBean bean = new ScoringBean();

    Competition mockedCompetition1 = mock(Competition.class);
    Competition mockedCompetition2 = mock(Competition.class);
    TravellerTableModel mockedTTM = mock(TravellerTableModel.class);
    PairingTableModel mockedPTM = mock(PairingTableModel.class);
    DefaultComboBoxModel<String> mockedCBM = (DefaultComboBoxModel<String>) mock(DefaultComboBoxModel.class);

    @BeforeEach
    void setUp() throws DataStoreException {
        actions.init(bean,mockedTTM,mockedPTM,mockedCBM);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Disabled
    void backButtonActionPerformed() {
        bean.setNewCompetitionName("Test Competition 1");
        bean.setNewSets("5");
        bean.setNewBoardsPerSet("16");
        bean.setNewNoPairs("10");
        bean.setNewBoard("3");
        bean.setNewSet("4");

        actions.backButtonActionPerformed(bean);

        assertAll("back", () -> {
            assertEquals(2,bean.getNewBoard());
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