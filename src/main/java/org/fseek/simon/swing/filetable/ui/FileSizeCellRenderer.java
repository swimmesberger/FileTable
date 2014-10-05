
package org.fseek.simon.swing.filetable.ui;

import java.awt.Component;
import javax.swing.JTable;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Simon Wimmesberger
 */
public class FileSizeCellRenderer extends DefaultFileCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, boolean mouseOver, boolean lastSelected, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, mouseOver, lastSelected, row, column);
        long size = (long)value;
        if(size < 0){
            setValue(null);
        }else{
            setValue(FileUtils.byteCountToDisplaySize(size));
        }
        return this;
    }
}