package scorebj.traveller;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

public class TravellerTableColumnModel extends DefaultTableColumnModel {
    private final String[] travellerColumnHeaders = new String[]
            {"NS Pair",
                    "EW Pair",
                    "Contract",
                    "By",
                    "Tricks",
                    "NS Score",
                    "EW Score",
                    "NS MP",
                    "EW MP",
                    "NS OVR",
                    "EW OVR"};

    public TravellerTableColumnModel() {
        TableColumn nsPairColumn = new TravellerTableIntegerColumn(0, 150);
        nsPairColumn.setPreferredWidth(150);
        nsPairColumn.setIdentifier(travellerColumnHeaders[0]);
        nsPairColumn.setHeaderValue(travellerColumnHeaders[0]);
        addColumn(nsPairColumn);

        TableColumn ewPairColumn = new TravellerTableIntegerColumn(1, 150);
        ewPairColumn.setPreferredWidth(150);
        ewPairColumn.setIdentifier(travellerColumnHeaders[1]);
        ewPairColumn.setHeaderValue(travellerColumnHeaders[1]);
        addColumn(ewPairColumn);

        TableColumn contractColumn = new TravellerTableContractColumn(2, 100);
        contractColumn.setPreferredWidth(100);
        contractColumn.setIdentifier(travellerColumnHeaders[2]);
        contractColumn.setHeaderValue((travellerColumnHeaders[2]));
        addColumn(contractColumn);

        TableColumn byColumn = new TravellerTableStringColumn(3, 100);
        byColumn.setPreferredWidth(100);
        byColumn.setIdentifier(travellerColumnHeaders[3]);
        byColumn.setHeaderValue(travellerColumnHeaders[3]);
        addColumn(byColumn);

        TableColumn tricksColumn = new TravellerTableStringColumn(4, 100);
        tricksColumn.setPreferredWidth(100);
        tricksColumn.setIdentifier(travellerColumnHeaders[4]);
        tricksColumn.setHeaderValue(travellerColumnHeaders[4]);
        addColumn(tricksColumn);

        TableColumn nsScoreColumn = new TravellerTableStringColumn(5, 100);
        nsScoreColumn.setPreferredWidth(100);
        nsScoreColumn.setIdentifier(travellerColumnHeaders[5]);
        nsScoreColumn.setHeaderValue(travellerColumnHeaders[5]);
        addColumn(nsScoreColumn);

        TableColumn ewScoreColumn = new TravellerTableStringColumn(6, 100);
        ewScoreColumn.setPreferredWidth(100);
        ewScoreColumn.setIdentifier(travellerColumnHeaders[6]);
        ewScoreColumn.setHeaderValue(travellerColumnHeaders[6]);
        addColumn(ewScoreColumn);

        TableColumn nsMPColumn = new TravellerTableStringColumn(7, 100);
        nsMPColumn.setPreferredWidth(100);
        nsMPColumn.setIdentifier(travellerColumnHeaders[7]);
        nsMPColumn.setHeaderValue(travellerColumnHeaders[7]);
        addColumn(nsMPColumn);

        TableColumn ewMPColumn = new TravellerTableStringColumn(8, 100);
        ewMPColumn.setPreferredWidth(100);
        ewMPColumn.setIdentifier(travellerColumnHeaders[8]);
        ewMPColumn.setHeaderValue(travellerColumnHeaders[8]);
        addColumn(ewMPColumn);

        TableColumn nsORColumn = new TravellerTableStringColumn(9, 100);
        nsORColumn.setIdentifier(travellerColumnHeaders[9]);
        nsORColumn.setHeaderValue(travellerColumnHeaders[9]);
        addColumn(nsORColumn);

        TableColumn ewORColumn = new TravellerTableStringColumn(10, 100);
        ewORColumn.setPreferredWidth(100);
        ewORColumn.setIdentifier(travellerColumnHeaders[10]);
        ewORColumn.setHeaderValue(travellerColumnHeaders[10]);
        addColumn(ewORColumn);
    }
}
