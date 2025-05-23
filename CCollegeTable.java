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
        
        table.setRowHeight(25);
        table.setFont(new Font("Helvetica", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 14));
        
        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        // Create search panel
        JPanel searchPanel = createSearchPanel();
        //add(searchPanel, BorderLayout.NORTH);
        
        // Load college data
        loadCollegeData();
    }
    
    /**
     * Creates a panel with search components
     */
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // Create search field
        searchField = new JTextField(20);
        searchField.addActionListener(e -> performSearch());
        
        // Create column selector for search
        searchColumnComboBox = new JComboBox<>(new String[]{"All Columns", "College Code", "College Name"});
        
        // Create search button
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performSearch());
        
        // Create clear button
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            searchField.setText("");
            performSearch();
        });
        
        // Add components to panel
        panel.add(new JLabel("Search:"));
        panel.add(searchField);
        panel.add(searchColumnComboBox);
        panel.add(searchButton);
        panel.add(clearButton);
        
        return panel;
    }
    
    /**
     * Performs the search based on the search field and column selection
     */
    private void performSearch() {
        String searchText = searchField.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            sorter.setRowFilter(null); // Show all rows if search is empty
            return;
        }
        
        int selectedIndex = searchColumnComboBox.getSelectedIndex();
        
        if (selectedIndex == 0) { // All Columns
            // Create a composite row filter that checks all columns
            RowFilter<DefaultTableModel, Integer> filter = new RowFilter<DefaultTableModel, Integer>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                    for (int i = 0; i < entry.getValueCount(); i++) {
                        if (entry.getStringValue(i).toLowerCase().contains(searchText)) {
                            return true;
                        }
                    }
                    return false;
                }
            };
            sorter.setRowFilter(filter);
        } else {
            // Search in specific column (subtract 1 because "All Columns" is at index 0)
            int columnToSearch = selectedIndex - 1;
            RowFilter<DefaultTableModel, Integer> filter = 
                RowFilter.regexFilter("(?i)" + searchText, columnToSearch);
            sorter.setRowFilter(filter);
        }
    }
    
    /**
     * Search by a specific term in all columns
     * 
     * @param searchTerm The term to search for
     */
    public void searchAllColumns(String searchTerm) {
        searchField.setText(searchTerm);
        searchColumnComboBox.setSelectedIndex(0); // All columns
        performSearch();
    }
    
    /**
     * Search in a specific column
     * 
     * @param searchTerm The term to search for
     * @param columnName The name of the column to search in ("College Code" or "College Name")
     */
    public void searchByColumn(String searchTerm, String columnName) {
        searchField.setText(searchTerm);
        
        // Set the appropriate column in combo box
        if (columnName.equals("College Code")) {
            searchColumnComboBox.setSelectedIndex(1);
        } else if (columnName.equals("College Name")) {
            searchColumnComboBox.setSelectedIndex(2);
        } else {
            searchColumnComboBox.setSelectedIndex(0); // Default to All Columns
        }
        
        performSearch();
    }
    
    /**
     * Clear the current search and show all rows
     */
    public void clearSearch() {
        searchField.setText("");
        performSearch();
    }
    
    /**
     * Loads college data from database (updated for new table structure)
     */
    private void loadCollegeData() {
    // Clear existing data
    tableModel.setRowCount(0);

    try (Connection conn = StudentDataManager.getConnection()) {
        // Skip 'N/A' college using WHERE clause
        String sql = "SELECT college_code, college_name FROM colleges WHERE college_code <> 'N/A' ORDER BY college_code";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            String collegeCode = rs.getString("college_code");
            String collegeName = rs.getString("college_name");

            // Add row to table (now only code and name)
            Object[] rowData = {collegeCode, collegeName};
            tableModel.addRow(rowData);
        }
    } catch (SQLException | ClassNotFoundException e) {
        System.err.println("Error loading college data: " + e.getMessage());
        e.printStackTrace();
    }
}

    
   
    public void refreshTable() {
     
        int selectedRow = table.getSelectedRow();
        String selectedCode = selectedRow >= 0 ? (String) tableModel.getValueAt(selectedRow, 0) : null;
        
       
        String currentSearch = searchField.getText();
        int currentSearchColumn = searchColumnComboBox.getSelectedIndex();

        Cursor oldCursor = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        try {
           
            sorter.setRowFilter(null);
            
      
            loadCollegeData();
         
            if (selectedCode != null) {
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if (selectedCode.equals(tableModel.getValueAt(i, 0))) {
                        table.setRowSelectionInterval(i, i);
                        break;
                    }
                }
            }
            
  
            if (!currentSearch.isEmpty()) {
                performSearch();
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