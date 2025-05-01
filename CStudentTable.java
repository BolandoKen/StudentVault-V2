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
    private Map<Integer, String> collegeCodeMap;
    private Map<Integer, String> programCodeMap;

    public CStudentTable() {
        setLayout(new BorderLayout());
        
        // Define column headers
        String[] columns = {"First Name", "Last Name", "Gender", "ID Number", "Year Level", "College", "Program"};
        
        // Create a table model that doesn't allow cell editing
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Create table with the model
        table = new JTable(tableModel);
        
        // Style the table
        table.setRowHeight(25);
        table.setFont(new Font("Helvetica", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Helvetica", Font.BOLD, 14));
        
        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        // Load reference data for colleges and programs
        loadCollegeMap();
        loadProgramMap();
        
        // Load student data
        loadStudentData();
    }
    
    /**
     * Loads college codes into a map for quick lookup
     */
    private void loadCollegeMap() {
        collegeCodeMap = new HashMap<>();
        
        try (Connection conn = StudentDataManager.getConnection()) {
            String sql = "SELECT college_id, college_code FROM colleges";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int collegeId = rs.getInt("college_id");
                String collegeCode = rs.getString("college_code");
                collegeCodeMap.put(collegeId, collegeCode);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error loading college data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Loads program codes into a map for quick lookup
     */
    private void loadProgramMap() {
        programCodeMap = new HashMap<>();
        
        try (Connection conn = StudentDataManager.getConnection()) {
            String sql = "SELECT program_id, program_code FROM programs";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int programId = rs.getInt("program_id");
                String programCode = rs.getString("program_code");
                programCodeMap.put(programId, programCode);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error loading program data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Loads student data from database
     */
    private void loadStudentData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        try (Connection conn = StudentDataManager.getConnection()) {
            String sql = "SELECT first_name, last_name, gender, id_number, year_level, college_id, program_id FROM students";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String gender = rs.getString("gender");
                String idNumber = rs.getString("id_number");
                String yearLevel = rs.getString("year_level");
                int collegeId = rs.getInt("college_id");
                int programId = rs.getInt("program_id");
                
                // Get college code and program code from maps
                String collegeCode = collegeCodeMap.getOrDefault(collegeId, "Unknown");
                String programCode = programCodeMap.getOrDefault(programId, "Unknown");
                
                // Add row to table
                Object[] rowData = {firstName, lastName, gender, idNumber, yearLevel, collegeCode, programCode};
                tableModel.addRow(rowData);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error loading student data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Refreshes table data from the database
     */
    public void refreshData() {
        loadStudentData();
    }
    
    /**
     * Returns the JTable component
     */
    public JTable getTable() {
        return table;
    }
    
    /**
     * Deletes a student record
     * @param row The row index to delete
     * @return true if deletion was successful
     */
    public boolean deleteStudent(int row) {
        if (row < 0 || row >= tableModel.getRowCount()) {
            return false;
        }
        
        String firstName = (String) tableModel.getValueAt(row, 0);
        String lastName = (String) tableModel.getValueAt(row, 1);
        String idNumber = (String) tableModel.getValueAt(row, 3);
        
        try (Connection conn = StudentDataManager.getConnection()) {
            String sql = "DELETE FROM students WHERE first_name = ? AND last_name = ? AND id_number = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, idNumber);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Remove from table model
                tableModel.removeRow(row);
                return true;
            }
            return false;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error deleting student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}