import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CCollegeTable extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public CCollegeTable() {
        setLayout(new BorderLayout());
        
        // Define column headers
        String[] columns = {"College ID", "College Name", "College Code"};
        
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
        
        // Load college data
        loadCollegeData();
    }
    
    /**
     * Loads college data from database
     */
    private void loadCollegeData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        try (Connection conn = StudentDataManager.getConnection()) {
            String sql = "SELECT college_id, college_name, college_code FROM colleges ORDER BY college_id";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int collegeId = rs.getInt("college_id");
                String collegeName = rs.getString("college_name");
                String collegeCode = rs.getString("college_code");
                
                // Add row to table
                Object[] rowData = {collegeId, collegeName, collegeCode};
                tableModel.addRow(rowData);
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error loading college data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Refreshes table data from the database
     */
    public void refreshData() {
        loadCollegeData();
    }
    
    /**
     * Returns the JTable component
     */
    public JTable getTable() {
        return table;
    }
    
    /**
     * Deletes a college record
     * @param row The row index to delete
     * @return true if deletion was successful
     */
    public boolean deleteCollege(int row) {
        if (row < 0 || row >= tableModel.getRowCount()) {
            return false;
        }
        
        int collegeId = (int) tableModel.getValueAt(row, 0);
        
        try (Connection conn = StudentDataManager.getConnection()) {
            String sql = "DELETE FROM colleges WHERE college_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, collegeId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Remove from table model
                tableModel.removeRow(row);
                return true;
            }
            return false;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error deleting college: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Adds a new college record
     * @param collegeName The name of the college
     * @param collegeCode The code of the college
     * @return true if addition was successful
     */
    public boolean addCollege(String collegeName, String collegeCode) {
        try (Connection conn = StudentDataManager.getConnection()) {
            String sql = "INSERT INTO colleges (college_name, college_code) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, collegeName);
            pstmt.setString(2, collegeCode);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                refreshData();
                return true;
            }
            return false;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error adding college: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Updates an existing college record
     * @param row The row index to update
     * @param collegeName The new name of the college
     * @param collegeCode The new code of the college
     * @return true if update was successful
     */
    public boolean updateCollege(int row, String collegeName, String collegeCode) {
        if (row < 0 || row >= tableModel.getRowCount()) {
            return false;
        }
        
        int collegeId = (int) tableModel.getValueAt(row, 0);
        
        try (Connection conn = StudentDataManager.getConnection()) {
            String sql = "UPDATE colleges SET college_name = ?, college_code = ? WHERE college_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, collegeName);
            pstmt.setString(2, collegeCode);
            pstmt.setInt(3, collegeId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                refreshData();
                return true;
            }
            return false;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error updating college: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
 * Refreshes the table data with additional features:
 * - Preserves current selection if possible
 * - Shows loading feedback
 * - Handles errors gracefully
 */
public void refreshTable() {
    // Save current selection if any
    int selectedRow = table.getSelectedRow();
    int selectedId = -1;
    if (selectedRow >= 0) {
        selectedId = (int) tableModel.getValueAt(selectedRow, 0);
    }

    // Show loading feedback
    Cursor oldCursor = getCursor();
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    
    try {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Reload data
        try (Connection conn = StudentDataManager.getConnection()) {
            String sql = "SELECT college_id, college_name, college_code FROM colleges ORDER BY college_id";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int collegeId = rs.getInt("college_id");
                String collegeName = rs.getString("college_name");
                String collegeCode = rs.getString("college_code");
                
                Object[] rowData = {collegeId, collegeName, collegeCode};
                tableModel.addRow(rowData);
            }
        }
        
        // Restore selection if possible
        if (selectedId >= 0) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if ((int) tableModel.getValueAt(i, 0) == selectedId) {
                    table.setRowSelectionInterval(i, i);
                    break;
                }
            }
        }
    } catch (SQLException | ClassNotFoundException e) {
        System.err.println("Error refreshing table data: " + e.getMessage());
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
            "Error refreshing data: " + e.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE);
    } finally {
        // Restore cursor
        setCursor(oldCursor);
    }
}
}