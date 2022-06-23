package scorebj.traveller;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class TravellerTableStringColumn extends TableColumn {
    JTextField textField = new JTextField();
    public TravellerTableStringColumn(){
        this(0);
    }
    public TravellerTableStringColumn(int modelIndex){
        this(modelIndex, 75);
    }
    public TravellerTableStringColumn(int modelIndex, int width){
        this(modelIndex,width,null,null);
        TableCellRenderer tableCellRenderer = new DefaultTableCellRenderer();
        setCellRenderer(tableCellRenderer);
        setHeaderRenderer(tableCellRenderer);
        setCellEditor(new DefaultCellEditor(textField));
    }
    public TravellerTableStringColumn(int modelIndex,
                                       int width,
                                       TableCellRenderer tableCellRenderer,
                                       TableCellEditor tableCellEditor){
        super(modelIndex,width,tableCellRenderer,tableCellEditor);
    }
}
