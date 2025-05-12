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
        
        // Define column headers - removed Program ID, changed College ID to College Code
        String[] columns = {"Program Name", "Program Code", "College Code"};
        
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
            // Updated SQL query to get college_code instead of college_id and removed program_id
            String sql = "SELECT program_name, program_code, college_code FROM programs ORDER BY program_code";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String programName = rs.getString("program_name");
                String programCode = rs.getString("program_code");
                String collegeCode = rs.getString("college_code");
                
                // Add row to table - removed programId, changed collegeId to collegeCode
                Object[] rowData = {programName, programCode, collegeCode};
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
     * Gets the program code from the selected row
     * @param row The selected row index
     * @return The program code or null if invalid row
     */
    public String getSelectedProgramCode(int row) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            return (String) tableModel.getValueAt(row, 1);
        }
        return null;
    }
    
    /**
     * Gets the college code from the selected row
     * @param row The selected row index
     * @return The college code or null if invalid row
     */
    public String getSelectedCollegeCode(int row) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            return (String) tableModel.getValueAt(row, 2);
        }
        return null;
    }
    
    /**
     * Gets the program name from the selected row
     * @param row The selected row index
     * @return The program name or null if invalid row
     */
    public String getSelectedProgramName(int row) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            return (String) tableModel.getValueAt(row, 0);
        }
        return null;
    }
    
}