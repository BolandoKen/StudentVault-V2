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
     * Loads college data from database (updated for new table structure)
     */
    private void loadCollegeData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        try (Connection conn = StudentDataManager.getConnection()) {
            // Updated query for new table structure
            String sql = "SELECT college_code, college_name FROM colleges ORDER BY college_code";
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
    
    /**
     * Refreshes table data from the database
     */
    public void refreshTable() {
        // Save current selection if any (now using college code as identifier)
        int selectedRow = table.getSelectedRow();
        String selectedCode = selectedRow >= 0 ? (String) tableModel.getValueAt(selectedRow, 0) : null;

        // Show loading feedback
        Cursor oldCursor = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        try {
            loadCollegeData();
            
            // Restore selection if possible
            if (selectedCode != null) {
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    if (selectedCode.equals(tableModel.getValueAt(i, 0))) {
                        table.setRowSelectionInterval(i, i);
                        break;
                    }
                }
            }
        } finally {
            // Restore cursor
            setCursor(oldCursor);
        }
    }
    
    /**
     * Returns the JTable component
     */
    public JTable getTable() {
        return table;
    }
}