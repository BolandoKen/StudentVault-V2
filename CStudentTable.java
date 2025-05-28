import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class CStudentTable extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private Map<String, String> programCodeMap; // Changed to String keys
    private TableRowSorter<DefaultTableModel> sorter;

    public CStudentTable() {
        setLayout(new BorderLayout());
        
        // Define column headers
        String[] columns = {"First Name", "Last Name", "Gender", "ID Number", "Year Level", "Program"};
        
        // Create a table model that doesn't allow cell editing
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Create table with the model
        table = new JTable(tableModel);

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        
        table.setRowHeight(25);
        table.setFont(new Font("Helvetica", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 14));
        
        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        // Load reference data for programs
        loadProgramMap();
        
        // Load student data
        loadStudentData();
    }
    
    private void loadProgramMap() {
        programCodeMap = new HashMap<>();
        
        try (Connection conn = StudentDataManager.getConnection()) {
            String sql = "SELECT program_code, program_name FROM programs";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String programCode = rs.getString("program_code");
                String programName = rs.getString("program_name");
                programCodeMap.put(programCode, programName);
            }
        } catch (SQLException e) {
            System.err.println("Error loading program data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadStudentData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        try (Connection conn = StudentDataManager.getConnection()) {
            String sql = "SELECT first_name, last_name, gender, id as id_number, year_level, program_code FROM students";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            System.out.println("Executing query: " + sql);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Query executed, checking results...");

            while (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String gender = rs.getString("gender");
                String idNumber = rs.getString("id_number");
                String yearLevel = rs.getString("year_level");
                String programCode = rs.getString("program_code");
                System.out.println("Found student: " + rs.getString("first_name") + " " + rs.getString("last_name"));
                
                // Handle NULL program code
                if (programCode == null) {
                    programCode = "N/A";
                }
                
                // Get program name from map, or use code if not found
                String programDisplay = programCodeMap.getOrDefault(programCode, programCode);
                
                // Add row to table
                Object[] rowData = {firstName, lastName, gender, idNumber, yearLevel, programDisplay};
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            System.err.println("Error loading student data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void refreshData() {
        loadProgramMap(); // Refresh program data too
        loadStudentData();
    }
    
    public JTable getTable() {
        return table;
    }
    
    // Add search and filter methods
    public void clearSearch() {
        // Clear any row filter but keep the sorter for sorting functionality
        if (sorter != null) {
            sorter.setRowFilter(null);
        }
    }

    public void searchAllColumns(String searchText) {
        if (sorter != null) {
            RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("(?i)" + searchText);
            sorter.setRowFilter(filter);
        }
    }

    public void searchByColumn(String searchText, String columnName) {
        if (sorter != null) {
            int columnIndex = getColumnIndex(columnName);
            
            if (columnIndex >= 0) {
                RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("(?i)" + searchText, columnIndex);
                sorter.setRowFilter(filter);
            }
        }
    }
    
    // Helper method to get column index by name
    private int getColumnIndex(String columnName) {
        switch (columnName) {
            case "First Name": return 0;
            case "Last Name": return 1;
            case "Gender": return 2;
            case "ID Number": return 3;
            case "Year Level": return 4;
            case "Program": return 5;
            default: return -1;
        }
    }
    
    // Helper methods for getting selected row data (accounting for sorting)
    public String getSelectedStudentId(int row) {
        if (row >= 0 && row < table.getRowCount()) {
            // Convert view row to model row in case table is sorted
            int modelRow = table.convertRowIndexToModel(row);
            return (String) tableModel.getValueAt(modelRow, 3); // ID Number is column 3
        }
        return null;
    }
    
    public String getSelectedFirstName(int row) {
        if (row >= 0 && row < table.getRowCount()) {
            int modelRow = table.convertRowIndexToModel(row);
            return (String) tableModel.getValueAt(modelRow, 0);
        }
        return null;
    }
    
    public String getSelectedLastName(int row) {
        if (row >= 0 && row < table.getRowCount()) {
            int modelRow = table.convertRowIndexToModel(row);
            return (String) tableModel.getValueAt(modelRow, 1);
        }
        return null;
    }
}