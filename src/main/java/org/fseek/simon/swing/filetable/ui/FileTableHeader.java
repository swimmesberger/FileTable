package org.fseek.simon.swing.filetable.ui;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Simon Wimmesberger
 */
public class FileTableHeader extends JTableHeader {
    public FileTableHeader(TableColumnModel columnModel){
        super(columnModel);
        this.setOpaque(false);
        this.setDefaultRenderer(new FileTableHeaderCellRenderer(getDefaultRenderer()));
    }
}
