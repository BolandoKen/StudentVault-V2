import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CProgramTable extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public CProgramTable() {
        setLayout(new BorderLayout());
        
        // Define column headers
        String[] columns = {"Program ID", "Program Name", "Program Code", "College ID"};
        
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
        
        // Load program data
        loadProgramData();
    }
    
    /**
     * Loads program data from database
     */
    private void loadProgramData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        try (Connection conn = ProgramDataManager.getConnection()) {
            String sql = "SELECT program_id, program_name, program_code, college_id FROM programs ORDER BY program_id";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int programId = rs.getInt("program_id");
                String programName = rs.getString("program_name");
                String programCode = rs.getString("program_code");
                int collegeId = rs.getInt("college_id");
                
                // Add row to table
                Object[] rowData = {programId, programName, programCode, collegeId};
                tableModel.addRow(rowData);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error loading program data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Refreshes table data from the database
     */
    public void refreshData() {
        loadProgramData();
    }
    
    /**
     * Returns the JTable component
     */
    public JTable getTable() {
        return table;
    }
    
    /**
     * Deletes a program record
     * @param row The row index to delete
     * @return true if deletion was successful
     */
    public boolean deleteProgram(int row) {
        if (row < 0 || row >= tableModel.getRowCount()) {
            return false;
        }
        
        int programId = (int) tableModel.getValueAt(row, 0);
        
        try (Connection conn = ProgramDataManager.getConnection()) {
            String sql = "DELETE FROM programs WHERE program_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, programId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Remove from table model
                tableModel.removeRow(row);
                return true;
            }
            return false;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error deleting program: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Adds a new program record
     * @param programName The name of the program
     * @param programCode The code of the program
     * @param collegeId The ID of the college
     * @return true if addition was successful
     */
    public boolean addProgram(String programName, String programCode, int collegeId) {
        try (Connection conn = ProgramDataManager.getConnection()) {
            String sql = "INSERT INTO programs (program_name, program_code, college_id) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, programName);
            pstmt.setString(2, programCode);
            pstmt.setInt(3, collegeId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                refreshData();
                return true;
            }
            return false;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error adding program: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Updates an existing program record
     * @param row The row index to update
     * @param programName The new name of the program
     * @param programCode The new code of the program
     * @param collegeId The new college ID
     * @return true if update was successful
     */
    public boolean updateProgram(int row, String programName, String programCode, int collegeId) {
        if (row < 0 || row >= tableModel.getRowCount()) {
            return false;
        }
        
        int programId = (int) tableModel.getValueAt(row, 0);
        
        try (Connection conn = ProgramDataManager.getConnection()) {
            String sql = "UPDATE programs SET program_name = ?, program_code = ?, college_id = ? WHERE program_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, programName);
            pstmt.setString(2, programCode);
            pstmt.setInt(3, collegeId);
            pstmt.setInt(4, programId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                refreshData();
                return true;
            }
            return false;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error updating program: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}