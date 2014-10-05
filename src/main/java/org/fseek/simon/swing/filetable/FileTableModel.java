/*
 * Copyright (C) 2011 Simon Wimmesberger<www.fseek.org>
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
package org.fseek.simon.swing.filetable;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.io.FilenameUtils;
import org.fseek.simon.swing.filetable.i18n.I18nData;
import org.fseek.thedeath.os.icons.FileIconWrapper;
import org.fseek.thedeath.os.util.OSUtil;

public class FileTableModel extends DefaultTableModel implements Serializable
{
    public static final int NAME_COLUMN_IDX = 0;
    public static final int MODIFY_DATE_COLUMN_IDX = 1;
    public static final int TYPE_COLUMN_IDX = 2;
    public static final int SIZE_COLUMN_IDX = 3;
    
    private final String[] columNames = new String[]{
        I18nData.get("fileTable.tableHeader.name"), 
        I18nData.get("fileTable.tableHeader.modifyDate"), 
        I18nData.get("fileTable.tableHeader.type"), 
        I18nData.get("fileTable.tableHeader.size")
    };
    
    private final ArrayList<FileIconWrapper> fileList;
 
    public FileTableModel()
    {
        fileList = new ArrayList<>();
    }

    public FileTableModel(File directory){
        this();
        this.loadDirectory(directory);
    }

    @Override
    public int getColumnCount()
    {
        return columNames.length;
    }

    @Override
    public int getRowCount()
    {
        if(fileList != null)
        {
            return this.fileList.size();
        }
        return 0;
    }

    @Override
    public String getColumnName(int column)
    {
        return columNames[column];
    }

    @Override
    public Object getValueAt(int row, int column)
    {
        try
        {
            FileIconWrapper iconWrapper = this.fileList.get(row);
            File f = iconWrapper.getFile();
            String fileDesc = OSUtil.getFileSystemView().getSystemTypeDescription(f);
            if(fileDesc == null){
                fileDesc = FilenameUtils.getExtension(f.getName());
            }
            
            switch(column)
            {
                case 0:
                    return iconWrapper;
                case 1:
                    return f.lastModified();
                case 2:
                    return fileDesc;
                case 3:
                    return f.isDirectory() ? -1 : f.length();
            }
        }catch(IndexOutOfBoundsException ex)
        {
            //ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column)
    {
        FileIconWrapper newValue = (FileIconWrapper)aValue;
        File newFile = newValue.getFile();
        FileIconWrapper get = fileList.get(row);
        File f = get.getFile();
        if(!newFile.toString().equals(f.toString()))
        {
            switch(column)
            {
                case 0:
                    boolean setFileName = f.renameTo(newFile);
                    if(setFileName){
                        newValue.updateIcon();
                        fileList.set(row, newValue);
                        fireTableCellUpdated(row, column);
                    }
                    break;
            }
        }
    }
    
    @Override
    public void removeRow(int row)
    {
        this.fileList.remove(row);
        fireTableRowsDeleted(row, row);
    }
    
    public String[] getColumNames()
    {
        return columNames;
    }
    
    @Override
    public boolean isCellEditable(int row, int column) 
    {
        return column == 0;
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        switch(columnIndex)
        {
            case 0:
                return FileIconWrapper.class;
            case 1:
                return Long.class;
            case 2:
                return String.class;
            case 3:
                return Long.class;
        }
        return Object.class;
    }

    public FileIconWrapper addFile(File file)
    {
        FileIconWrapper fileIconWrapper = addFileImpl(file);
        int rowCount = this.getRowCount();
        fireTableRowsInserted(rowCount-1, rowCount);
        return fileIconWrapper;
    }
    
    protected FileIconWrapper addFileImpl(File file)
    {
        FileIconWrapper fileIconWrapper = new FileIconWrapper(file);
        this.fileList.add(fileIconWrapper);
        return fileIconWrapper;
    }
    
    public File getFile(int row)
    {
        return this.fileList.get(row).getFile();
    }

    public void clear()
    {
        int rowCount = this.getRowCount();
        this.fileList.clear();
        fireTableRowsDeleted(0, rowCount-1);
    }
    
    public void loadDirectory(File directory){
        if(directory == null || directory.isDirectory() == false){
            throw new IllegalArgumentException("No directory passed.");
        }
        this.clear();
        File[] listFiles = directory.listFiles();
        for(File f : listFiles){
            addFileImpl(f);
        }
        fireTableDataChanged();
    }
}
