import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

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
  
    private void loadProgramData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        try (Connection conn = ProgramDataManager.getConnection()) {
            // Updated SQL query to get college_code instead of college_id and removed program_id
            String sql = "SELECT program_code, program_name, college_code FROM programs WHERE program_code <> 'N/A' ORDER BY program_code";
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

    public void refreshData() {
        loadProgramData();
    }
  
    public JTable getTable() {
        return table;
    }
    
    public String getSelectedProgramCode(int row) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            return (String) tableModel.getValueAt(row, 1);
        }
        return null;
    }
   
    public String getSelectedCollegeCode(int row) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            return (String) tableModel.getValueAt(row, 2);
        }
        return null;
    }
    
    public String getSelectedProgramName(int row) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            return (String) tableModel.getValueAt(row, 0);
        }
        return null;
    }
    public void clearSearch() {
    TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) getTable().getRowSorter();
    if (sorter != null) {
        sorter.setRowFilter(null);
    }
}

    public void searchAllColumns(String searchText) {
    DefaultTableModel model = (DefaultTableModel) getTable().getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    getTable().setRowSorter(sorter);
    RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("(?i)" + searchText);
    sorter.setRowFilter(filter);
}

    public void searchByColumn(String searchText, String columnName) {
    DefaultTableModel model = (DefaultTableModel) getTable().getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    getTable().setRowSorter(sorter);
    
    int columnIndex = -1;
    switch (columnName) {
        case "Program Code":
            columnIndex = 1;
            break;
        case "Program Name":
            columnIndex = 0;
            break;
        case "College Code":
            columnIndex = 2;
            break;
    }
    
    if (columnIndex >= 0) {
        RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("(?i)" + searchText, columnIndex);
        sorter.setRowFilter(filter);
    }
}

    
}