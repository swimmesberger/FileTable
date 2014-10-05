package org.fseek.simon.swing.filetable.ui;

import java.awt.Color;
import java.awt.Component;
import java.util.EventObject;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import org.fseek.simon.swing.filetable.ui.publish.XTableCellRenderer;

/**
 *
 * @author Simon Wimmesberger
 */
public class DefaultFileCellRenderer extends DefaultTableCellRenderer implements XTableCellRenderer {

    private static final Color MOUSE_OVER_BACKGROUND_COLOR = new Color(229, 243, 251);
    private static final Color MOUSE_OVER_BORDER_COLOR = new Color(112, 192, 231);

    private static final int BORDER_SIZE = 1;
    
    private static final Border MOUSE_OVER_LEFT_BORDER = BorderUtil.createLeftBorder(BORDER_SIZE, MOUSE_OVER_BORDER_COLOR);
    private static final Border MOUSE_OVER_INNER_BORDER = BorderUtil.createInnerBorder(BORDER_SIZE, MOUSE_OVER_BORDER_COLOR); 
    private static final Border MOUSE_OVER_RIGHT_BORDER = BorderUtil.createRightBorder(BORDER_SIZE, MOUSE_OVER_BORDER_COLOR); 
    
    private static final Border LEFT_BORDER = BorderUtil.createLeftBorder(BORDER_SIZE);
    private static final Border INNER_BORDER = BorderUtil.createInnerBorder(BORDER_SIZE); 
    private static final Border RIGHT_BORDER = BorderUtil.createRightBorder(BORDER_SIZE); 

    public DefaultFileCellRenderer() {
    }

    @Override
    public final Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return getTableCellRendererComponentImpl(table, value, isSelected, hasFocus, false, false, row, column);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, boolean mouseOver, boolean lastSelected, int row, int column) {
        return getTableCellRendererComponentImpl(table, value, isSelected, hasFocus, mouseOver, lastSelected, row, column);
    }

    private Component getTableCellRendererComponentImpl(JTable table, Object value, boolean isSelected, boolean hasFocus, boolean mouseOver, boolean lastSelected, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (mouseOver && !isSelected) {
            setBackground(MOUSE_OVER_BACKGROUND_COLOR);
            setBorder(getMouseOverBorder(column, table));
        } else if(!isSelected){
            setBorder(getBorder(column, table));
            setBackground(table.getBackground());
        }
        return this;
    }
    
    protected Border getBorder(int column, JTable table){
        Border border;
        if(column == 0){
            border = LEFT_BORDER;
        }else if(column == table.getColumnCount()-1){
            border = RIGHT_BORDER;
        }else{
            border = INNER_BORDER;
        }
        return border;
    }
    
    protected Border getMouseOverBorder(int column, JTable table){
        Border border;
        if(column == 0){
            border = MOUSE_OVER_LEFT_BORDER;
        }else if(column == table.getColumnCount()-1){
            border = MOUSE_OVER_RIGHT_BORDER;
        }else{
            border = MOUSE_OVER_INNER_BORDER;
        }
        return border;
    }

    @Override
    public boolean isEditAllowed(EventObject event) {
        return true;
    }
}
