package scorebj.traveller;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumnModel;

public class TravellerJTable extends JTable {
    String[] travellerColumnHeaders;
    TableColumnModel editableIntegerColumnModel = new DefaultTableColumnModel();
    TableColumnModel uneditableItegerColumnModel;
    TableColumnModel contractColumnModel;


    {
        travellerColumnHeaders = new String[]
                {"NS Pair",
                        "EW Pair",
                        "Contract",
                        "By",
                        "Tricks",
                        "NS Score",
                        "EW Score",
                        "NS MP",
                        "EW MP"};
    }

}
