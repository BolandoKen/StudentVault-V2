import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CStudentTable extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private Map<String, String> programCodeMap; // Changed to String keys

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
}