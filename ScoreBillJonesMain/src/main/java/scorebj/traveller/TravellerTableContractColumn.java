package scorebj.traveller;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class TravellerTableContractColumn extends TableColumn {

    JTextField textField = new JTextField();

    public TravellerTableContractColumn(){
        this(0);
    }

    public TravellerTableContractColumn(int modelIndex){
        this(modelIndex,75);
    }

    public TravellerTableContractColumn(int modelIndex, int width){
        this(modelIndex,
                width,
                null,
                null);
        TableCellRenderer tableCellRenderer = new DefaultTableCellRenderer();
        TableCellRenderer tableCellHeaderRenderer = new DefaultTableCellRenderer();

        setCellRenderer(tableCellRenderer);
        setHeaderRenderer(tableCellHeaderRenderer);
        setCellEditor(new DefaultCellEditor(textField));

    }
    public TravellerTableContractColumn(int modelIndex,
                                      int width,
                                      TableCellRenderer tableCellRenderer,
                                      TableCellEditor tableCellEditor){
        super(modelIndex,width,tableCellRenderer,tableCellEditor);
    }
}
