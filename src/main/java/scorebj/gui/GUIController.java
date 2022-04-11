package scorebj.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUIController extends WindowAdapter {
    private static GUIController guiController;

    public GUIController(){
        //CompetitionForm competitionForm = new CompetitionForm();
        //competitionForm.display();
        //PairingForm pairingForm = new PairingForm();
        ScoresheetForm scoresheetForm = new ScoresheetForm();
        scoresheetForm.display();


    }
    @Override
    public void windowStateChanged(WindowEvent e) {
        super.windowStateChanged(e);
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        super.windowLostFocus(e);
    }

public static void main(String[] args){
        guiController = new GUIController();

}
}
