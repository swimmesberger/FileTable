
package org.fseek.simon.swing.filetable.ui;

import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTable;

/**
 *
 * @author Simon Wimmesberger
 */
public class DateCellRenderer extends DefaultFileCellRenderer {
    private static final DateFormat dateFormat = SimpleDateFormat.getDateInstance();
    
    private final Date date = new Date();

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, boolean mouseOver, boolean lastSelected, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, mouseOver, lastSelected, row, column);
        long time = (long)value;
        date.setTime(time);
        setValue(dateFormat.format(date));
        return this;    
    }
}
