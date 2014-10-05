package org.fseek.simon.swing.filetable.ui.publish;

import java.awt.Component;
import java.util.EventObject;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Simon Wimmesberger
 */
public interface XTableCellRenderer extends TableCellRenderer {
    Component getTableCellRendererComponent(JTable table, Object value,
                                            boolean isSelected, boolean hasFocus, 
                                            boolean mouseOver, boolean lastSelected,
                                            int row, int column);
    
    boolean isEditAllowed(EventObject event);
}
