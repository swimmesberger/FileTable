
package org.fseek.simon.swing.filetable.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import org.fseek.thedeath.os.icons.FileIconWrapper;

/**
 *
 * @author Simon Wimmesberger
 */
public class FileNameCellRenderer extends DefaultFileCellRenderer {
    private final JPanel componentHolder;
    private final JLabel iconLbl;
    
    public FileNameCellRenderer() {
        super();
        this.componentHolder = new JPanel();
        
        this.componentHolder.setBorder(BorderFactory.createEmptyBorder());
        BorderLayout borderLayout = new BorderLayout(5, 5);
        this.componentHolder.setLayout(borderLayout);
        this.iconLbl = new JLabel();
        this.componentHolder.add(iconLbl, BorderLayout.WEST);
        this.componentHolder.add(this, BorderLayout.CENTER);
        
        //do not paint the background of the subcomponents, but paint the background of the whole panel
        this.componentHolder.setOpaque(true);
        this.iconLbl.setOpaque(false);
        this.setOpaque(false);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, boolean mouseOver, boolean lastSelected, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, mouseOver, lastSelected, row, column);
        FileIconWrapper wrapper = (FileIconWrapper)value;
        setValue(wrapper.getFile().getName());
        iconLbl.setIcon(wrapper.getIcon());
        componentHolder.setBackground(this.getBackground());
        componentHolder.setBorder(this.getBorder());
        this.setBorder(BorderFactory.createEmptyBorder());
        return this.componentHolder;
    }
    
    @Override
    public boolean isEditAllowed(JTable table, int row, int column, EventObject event) {
        if(event instanceof MouseEvent){
            MouseEvent mEvt = (MouseEvent)event;
            Rectangle cellRect = table.getCellRect(row, column, true);
            Rectangle subRect = new Rectangle(cellRect.x + this.getX(), cellRect.y + this.getY(), this.getWidth(), this.getHeight());
            return subRect.contains(mEvt.getPoint());
        }
        return true;
    }
}
