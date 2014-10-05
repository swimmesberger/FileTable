/*
 * Copyright (C) 2014 Simon Wimmesberger
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fseek.simon.swing.filetable.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.nio.file.Path;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import org.fseek.thedeath.os.icons.FileIconWrapper;

/**
 *
 * @author sWimmesberger
 */
public class FileTableCellEditor extends DefaultCellEditor implements TableCellEditor
{
    private final JPanel componentHolder;
    private final JLabel iconLbl;
    private FileIconWrapper object;
    private FileIconWrapper newObject;
    private ImageIcon fileIcon;

    public FileTableCellEditor() {
        super(new JTextField());
        setClickCountToStart(1);
        this.componentHolder = new JPanel();
        this.componentHolder.setOpaque(false);
        this.componentHolder.setBorder(BorderFactory.createEmptyBorder());
        BorderLayout borderLayout = new BorderLayout(5,5);
        this.componentHolder.setLayout(borderLayout);
        this.iconLbl = new JLabel();
        this.componentHolder.add(iconLbl, BorderLayout.WEST);
        this.componentHolder.add(editorComponent, BorderLayout.CENTER);
    }

    @Override
    public FileIconWrapper getCellEditorValue()
    {
        return newObject != null ? newObject: object;
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
    {
        if(this.object != value || newObject != null){
            object = (FileIconWrapper)value;
            this.fileIcon = object.getIcon();
            iconLbl.setIcon(fileIcon);
            ((JTextField)editorComponent).setText(object.getFile().getName());
            newObject = null;
        }
        editorComponent.requestFocus();
        return componentHolder;
    }

    @Override
    public boolean stopCellEditing() {
        JTextField txtField = (JTextField)editorComponent;
        Path resolveSibling = object.getFile().toPath().resolveSibling(txtField.getText());
        File newFile = resolveSibling.toFile();
        if(newFile.exists() == true){
            if(object.getFile().toPath().equals(newFile.toPath()) == false){
                return false;
            }else{
                return super.stopCellEditing();
            }
        }
        this.newObject = new FileIconWrapper(newFile, this.object.getIcon());
        return super.stopCellEditing();
    }
}
