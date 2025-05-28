import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class CCollegeTable extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;
    private JComboBox<String> searchColumnComboBox;

    public CCollegeTable() {
        setLayout(new BorderLayout());
        
        // Define column headers (now only college code and name)
        String[] columns = {"College Code", "College Name"};
        
        // Create a table model that doesn't allow cell editing
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Create table with the model
        table = new JTable(tableModel);
        
        // Create and add the sorter for search filtering
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        
        // Set column sorting preferences
        sorter.setSortable(0, true); // College Code is sortable
        sorter.setSortable(1, true); // College Name is sortable
        
        // Set default sort (optional - sorts by College Code by default)
        sorter.setSortKeys(java.util.Arrays.asList(new RowSorter.SortKey(0, SortOrder.ASCENDING)));

        table.setRowHeight(25);
        table.setFont(new Font("Helvetica", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 14));
        
        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        // Load college data
        loadCollegeData();
    }
 

    public void applySearchFilter(String searchText, String columnName) {
        System.out.println("CCollegeTable: applySearchFilter called");
        System.out.println("CCollegeTable: searchText = '" + searchText + "'");
        System.out.println("CCollegeTable: columnName = '" + columnName + "'");
        
        // Use SwingUtilities.invokeLater to ensure UI updates happen on EDT
        SwingUtilities.invokeLater(() -> {
            if (searchText == null || searchText.trim().isEmpty()) {
                System.out.println("CCollegeTable: Search text is empty, clearing filter");
                sorter.setRowFilter(null);
            } else {
                String searchTerm = searchText.trim();
                System.out.println("CCollegeTable: searchTerm = '" + searchTerm + "'");
                
                try {
                    if (columnName.equals("All")) {
                        System.out.println("CCollegeTable: Searching in all columns");
                        RowFilter<DefaultTableModel, Integer> filter = new RowFilter<DefaultTableModel, Integer>() {
                            @Override
                            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                                for (int i = 0; i < entry.getValueCount(); i++) {
                                    if (entry.getStringValue(i) != null) {
                                        String value = entry.getStringValue(i).toLowerCase();
                                        if (value.contains(searchTerm.toLowerCase())) {
                                            System.out.println("CCollegeTable: Match found in column " + i + ": " + value);
                                            return true;
                                        }
                                    }
                                }
                                return false;
                            }
                        };
                        sorter.setRowFilter(filter);
                    } else {
                        System.out.println("CCollegeTable: Searching in specific column: " + columnName);
                        int columnIndex = columnName.equals("College Code") ? 0 : 1;
                        System.out.println("CCollegeTable: Column index: " + columnIndex);
                        
                        // Use case-insensitive regex filter
                        RowFilter<DefaultTableModel, Integer> filter = 
                            RowFilter.regexFilter("(?i).*" + java.util.regex.Pattern.quote(searchTerm) + ".*", columnIndex);
                        sorter.setRowFilter(filter);
                    }
                } catch (java.util.regex.PatternSyntaxException e) {
                    System.err.println("Invalid regex pattern: " + e.getMessage());
                    // Fall back to no filter if regex is invalid
                    sorter.setRowFilter(null);
                }
            }
            
            // Force comprehensive UI refresh
            table.clearSelection();
            tableModel.fireTableDataChanged();
            sorter.allRowsChanged();
            table.repaint();
            table.revalidate();
            this.repaint();
            this.revalidate();
            
            // Update parent container if it exists
            Container parent = getParent();
            if (parent != null) {
                parent.repaint();
                parent.revalidate();
            }
            
            System.out.println("CCollegeTable: Filter applied, visible rows: " + table.getRowCount());
            System.out.println("CCollegeTable: Total model rows: " + tableModel.getRowCount());
        });
    }

    private void loadCollegeData() {
        // Clear any existing filter before loading data
        if (sorter != null) {
            sorter.setRowFilter(null);
        }
        
        tableModel.setRowCount(0);
        
        try (Connection conn = StudentDataManager.getConnection()) {
            String sql = "SELECT college_code, college_name FROM colleges WHERE college_code <> 'N/A' ORDER BY college_code";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
    
            while (rs.next()) {
                String collegeCode = rs.getString("college_code");
                String collegeName = rs.getString("college_name");
                tableModel.addRow(new Object[]{collegeCode, collegeName});
            }
            
            // Ensure UI is updated after loading data
            SwingUtilities.invokeLater(() -> {
                tableModel.fireTableDataChanged();
                table.repaint();
                table.revalidate();
            });
            
        } catch (SQLException e) {
            System.err.println("Error loading college data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        int selectedRow = table.getSelectedRow();
        String selectedCode = null;
        
        // Get selected code safely
        if (selectedRow >= 0) {
            int modelRow = table.convertRowIndexToModel(selectedRow);
            selectedCode = (String) tableModel.getValueAt(modelRow, 0);
        }
        
        Cursor oldCursor = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        try {
            // Clear filter and reload data
            sorter.setRowFilter(null);
            loadCollegeData();
            
            // Restore selection if possible
            if (selectedCode != null) {
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if (selectedCode.equals(tableModel.getValueAt(i, 0))) {
                        // Convert model index to view index
                        int viewIndex = table.convertRowIndexToView(i);
                        table.setRowSelectionInterval(viewIndex, viewIndex);
                        break;
                    }
                }
            }
        } finally {
            setCursor(oldCursor);
        }
    }
    
    public JTable getTable() {
        return table;
    }
    
    public String getSelectedCollegeCode() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            return null;
        }
        
        int modelRow = table.convertRowIndexToModel(viewRow);
        return (String) tableModel.getValueAt(modelRow, 0);
    }

    public JTextField getSearchField() {
        return searchField;
    }
    
    public JComboBox<String> getSearchColumnComboBox() {
        return searchColumnComboBox;
    }
}