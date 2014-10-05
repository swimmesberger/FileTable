package org.fseek.simon.swing.filetable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.fseek.simon.swing.filetable.ui.DateCellRenderer;
import org.fseek.simon.swing.filetable.ui.DefaultFileCellRenderer;
import org.fseek.simon.swing.filetable.ui.FileNameCellRenderer;
import org.fseek.simon.swing.filetable.ui.FileSizeCellRenderer;
import org.fseek.simon.swing.filetable.ui.FileTableCellEditor;
import org.fseek.simon.swing.filetable.ui.FileTableHeader;
import org.fseek.simon.swing.filetable.ui.publish.XTableCellRenderer;
import org.fseek.thedeath.os.icons.FileIconWrapper;
import org.fseek.thedeath.os.util.Debug;
import org.fseek.thedeath.os.util.OSUtil;

/**
 *
 * @author Simon Wimmesberger
 */
public class FileTable extends JTable {
    private static final Dimension DIMENSION_ZERO = new Dimension(0, 0);
          
    public static final int START_EDIT_DELAY = 800;

    private File directory;
    private FileTableModel model;
    private Timer startEditTimer;
    protected boolean processedClick = false;
    
    private int lastSelectedRow;
    private int lastSelectedColumn;
    private int mouseOverRow;
    private int mouseOverColumn;

    public FileTable(File directory) {
        if (directory == null || directory.isDirectory() == false) {
            throw new IllegalArgumentException("No directory passed.");
        }
        this.directory = directory;
        this.initTable();
    }
    
    protected void initUI(){
        this.initClientProperties();
        this.setOpaque(false);
        this.setBackground(OSUtil.getOSAppearance().getTreePanelColor());
         
        this.setBorder(BorderFactory.createEmptyBorder());
        
        //hide grid
        this.setShowGrid(false);
        this.setIntercellSpacing(DIMENSION_ZERO);
        this.setRowHeight(21);
        this.setColumnSelectionAllowed(false);
    }

    protected void initTable() {
        this.model = new FileTableModel(this.directory);
        this.setModel(model);
        
        this.setTableHeader(new FileTableHeader(getColumnModel()));
        this.setDefaultEditor(FileIconWrapper.class, new FileTableCellEditor());
        this.setDefaultRenderer(FileIconWrapper.class, new FileNameCellRenderer());
        this.setDefaultRenderer(Object.class, new DefaultFileCellRenderer());
        this.getColumnModel().getColumn(FileTableModel.SIZE_COLUMN_IDX).setCellRenderer(new FileSizeCellRenderer());
        this.getColumnModel().getColumn(FileTableModel.MODIFY_DATE_COLUMN_IDX).setCellRenderer(new DateCellRenderer());
        
        this.setAutoCreateRowSorter(true);
        this.getRowSorter().toggleSortOrder(0);

        this.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false && startEditTimer != null) {
                    startEditTimer.stop();
                    startEditTimer = null;
                }
            }
        });
        
        MouseAdapter mouseAdapter = new MouseAdapter() {
            private boolean setMouseOver(int column, int row){
                boolean changed = false;
                if(mouseOverColumn != column){
                    mouseOverColumn = column;
                    changed = true;
                }
                if(mouseOverRow != row){
                    mouseOverRow = row;
                    changed = true;
                }
                return changed;
            }
            
            @Override
            public void mouseMoved(MouseEvent e) {
                int column = FileTable.this.columnAtPoint(e.getPoint());
                int row = FileTable.this.rowAtPoint(e.getPoint());
                boolean changed = setMouseOver(column, row);
                if(changed){
                    repaint();
                }
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = FileTable.this.columnAtPoint(e.getPoint());
                int row = FileTable.this.rowAtPoint(e.getPoint());
                processedClick = onCellClick(e, row, column);
                if(SwingUtilities.isMiddleMouseButton(e)){
                    clearSelection();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boolean changed = setMouseOver(-1, -1);
                if(changed){
                    repaint();
                }
            }
        };
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);

        this.initUI();
    }

    @Override
    public void clearSelection() {
        lastSelectedColumn = getSelectedColumn();
        lastSelectedRow = getSelectedRow();
        super.clearSelection();
    }

    protected void initClientProperties(){
        this.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);
        this.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    }
    
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Object value = getValueAt(row, column);

        boolean isSelected = false;
        boolean hasFocus = false;

        // Only indicate the selection and focused cell if not printing
        if (!isPaintingForPrint()) {
            isSelected = isCellSelected(row, column);

            boolean rowIsLead =
                (selectionModel.getLeadSelectionIndex() == row);
            boolean colIsLead =
                (columnModel.getSelectionModel().getLeadSelectionIndex() == column);

            hasFocus = (rowIsLead && colIsLead) && isFocusOwner();
        }
        
        if(renderer instanceof XTableCellRenderer){
            XTableCellRenderer extCellRenderer = (XTableCellRenderer)renderer;
            boolean isMouseOver = mouseOverRow == row;
            boolean isLastSelected = lastSelectedColumn == column && lastSelectedRow == row;
            return extCellRenderer.getTableCellRendererComponent(this, value, isSelected, hasFocus, isMouseOver, isLastSelected, row, column);
        }else{
            return renderer.getTableCellRendererComponent(this, value,
                                                          isSelected, hasFocus,
                                                          row, column);
        }
    }
    
    

    @Override
    protected void configureEnclosingScrollPane() {
        super.configureEnclosingScrollPane();
        Container parent = SwingUtilities.getUnwrappedParent(this);
        if (parent instanceof JViewport) {
            JViewport port = (JViewport) parent;
            Container gp = port.getParent();
            if (gp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) gp;

                JViewport viewport = scrollPane.getViewport();
                viewport.setOpaque(true);
                viewport.setBackground(this.getBackground());

                scrollPane.getColumnHeader().setBackground(this.getBackground());
                scrollPane.getColumnHeader().setOpaque(true);
            }
        }
    }

    @Override
    public boolean editCellAt(int row, int column, EventObject e) {
        TableCellEditor editor = getCellEditor(row, column);
        TableCellRenderer cellRenderer = getCellRenderer(row, column);
        if(cellRenderer instanceof XTableCellRenderer){
            XTableCellRenderer xCellRenderer = (XTableCellRenderer)cellRenderer;
            if(!xCellRenderer.isEditAllowed(this, row, column, e)){
                return false;
            }
        }
        if (editor != null && editor.isCellEditable(e) && e != null) {
            processEditingStarted(row, column);
            return false;
        }
        return super.editCellAt(row, column, e);
    }

    protected boolean onCellClick(MouseEvent evt, final int row, final int column) {
        boolean clickP = false;
        if (startEditTimer != null && startEditTimer.isRunning() && startEditTimer.getActionCommand().equals(String.valueOf(column)) == false) {
            startEditTimer.stop();
            startEditTimer = null;
        }
        if (SwingUtilities.isLeftMouseButton(evt) && evt.getClickCount() >= 2) {
            if (startEditTimer != null) {
                startEditTimer.stop();
                startEditTimer = null;
            }
            File file = model.getFile(convertRowIndexToModel(row));
            clickP = processDoubleClick(file);
        }
        return clickP;
    }

    protected void processEditingStarted(final int row, final int column) {
        if (processedClick) {
            processedClick = false;
        }
        startEditTimer = new Timer(START_EDIT_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Timer source = (Timer) e.getSource();
                source.stop();
                if (startEditTimer != null) {
                    startEditTimer = null;
                }
                FileTable.this.editCellAt(row, column);
            }
        });
        startEditTimer.setActionCommand(String.valueOf(column));
        startEditTimer.start();
    }

    protected boolean processDoubleClick(File clickedFile) {
        if (clickedFile.isDirectory()) {
            this.model.loadDirectory(clickedFile);
            Debug.println(clickedFile.getAbsolutePath() + " directory clicked.");
        } else {
            OSUtil.openFile(clickedFile);
            Debug.println(clickedFile.getAbsolutePath() + " file clicked.");
        }
        return true;
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Debug.setDebug(false);
                    Debug.setShowErrors(false);
                    if (args.length > 0) {
                        if (args[0].toLowerCase().equals("-debug")) {
                            Debug.setDebug(true);
                            Debug.setShowErrors(true);
                        }
                    }
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    JFrame frame = new JFrame("FileTable");
                    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    JPanel mainPanel = new JPanel(new BorderLayout());
                    JScrollPane scroll = new JScrollPane(new FileTable(OSUtil.getFileSystem().getHomeFolder()));
                    mainPanel.add(scroll, BorderLayout.CENTER);
                    frame.add(mainPanel);
                    frame.pack();
                    frame.setVisible(true);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(FileTable.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
