package scorebj.traveller;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class TravellerTableIntegerColumn extends TableColumn {

    JTextField textField = new JTextField();

    public TravellerTableIntegerColumn(){
        this(0);
    }
    public TravellerTableIntegerColumn(int modelIndex){
        this(modelIndex,75);
    }

    public TravellerTableIntegerColumn(int modelIndex, int width){

        this(modelIndex,width,null,null);
        TableCellRenderer tableCellRenderer = new DefaultTableCellRenderer();
        setCellRenderer(tableCellRenderer);
        setHeaderRenderer(tableCellRenderer);
        setCellEditor(new DefaultCellEditor(textField));

    }
    public TravellerTableIntegerColumn(int modelIndex,
                                       int width,
                                       TableCellRenderer tableCellRenderer,
                                       TableCellEditor tableCellEditor){
        super(modelIndex,width,tableCellRenderer,tableCellEditor);
    }
}
