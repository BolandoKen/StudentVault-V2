import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PStudentTablePanel extends JPanel{

    private CStudentTable studentTable;
    private boolean selectionMode = false;
    private JButton deleteButton;
    private JButton cancelButton;
    private JButton confirmDeleteButton;
    private JPanel buttonsPanel;
    private JButton editButton;
    private CSearchPanels.StudentSearchPanel searchPanel;

    public PStudentTablePanel() {
        studentTable = new CStudentTable();

        searchPanel = new CSearchPanels.StudentSearchPanel(searchTerms -> {
            String searchText = searchTerms[0];
            String columnName = searchTerms[1];
            filterStudentTable(searchText, columnName);
        });
    
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        JPanel searchPanelContainer = new JPanel(new BorderLayout());
        searchPanelContainer.setOpaque(false);
        gbc.gridy = 0;
        gbc.weighty = 0.02;

        searchPanelContainer.add(searchPanel, BorderLayout.NORTH);
        this.add(searchPanelContainer, gbc);

        JPanel topRow = new JPanel(new GridBagLayout());
        topRow.setOpaque(false);
        topRow.setPreferredSize(new Dimension(1, 100));
        gbc.gridy = 1;
        gbc.weighty = 0.1;
        this.add(topRow, gbc);

        GridBagConstraints gbcTopRow = new GridBagConstraints();
        gbcTopRow.fill = GridBagConstraints.BOTH;
        gbcTopRow.gridy = 0; 
        gbcTopRow.weightx = 0.5;
        gbcTopRow.weighty = 1.0; 
        gbcTopRow.anchor = GridBagConstraints.SOUTH; 

        gbcTopRow.gridx = 0;
        gbcTopRow.weightx = 0.5;
        gbcTopRow.anchor = GridBagConstraints.WEST; 

        //setup TopLeftPanel
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false); 
        topRow.add(leftPanel, gbcTopRow);

        gbcTopRow.gridx = 1;
        gbcTopRow.weightx = 0.5; 
        gbcTopRow.anchor = GridBagConstraints.EAST; 

        //setup TopRightPanel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        topRow.add(rightPanel, gbcTopRow);

        buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));
        buttonsPanel.setOpaque(false);
        rightPanel.add(buttonsPanel, BorderLayout.SOUTH);

        JButton addCollegeButton = new JButton(new ImageIcon("Assets/PlusIcon.png"));
        addCollegeButton.setBorderPainted(false);
        addCollegeButton.setFocusPainted(false);
        addCollegeButton.setContentAreaFilled(false);
        addCollegeButton.setBackground(new Color(0xE7E7E7));

        addCollegeButton.addActionListener(e -> {
            DStudentDialogs.addStudentDialog(studentTable);
        });
 
        deleteButton = new JButton(new ImageIcon("Assets/DeleteIcon.png"));
        deleteButton.setBorderPainted(false);
        deleteButton.setFocusPainted(false);
        deleteButton.setContentAreaFilled(false);

        deleteButton.addActionListener(e -> {
            JTable table = studentTable.getTable();
            int selectedRow = table.getSelectedRow();
        
            if (selectedRow != -1) {
                Object idValue = table.getValueAt(selectedRow, 3);
                if (idValue != null) {
                    String studentId = idValue.toString();
                    DStudentDialogs.deleteStudentDialog(studentId, studentTable); 
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a student to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
    
        editButton = new JButton(new ImageIcon("Assets/EditIcon.png"));
        editButton.setBorderPainted(false);
        editButton.setFocusPainted(false);
        editButton.setContentAreaFilled(false);

        editButton.addActionListener(e -> {
            JTable table = studentTable.getTable();
            int selectedRow = table.getSelectedRow();
        
            if (selectedRow != -1) {
                Object idValue = table.getValueAt(selectedRow, 3);
                if (idValue != null) {
                    String studentId = idValue.toString();
                    DStudentDialogs.editStudentDialog(studentId, studentTable);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a student to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        buttonsPanel.add(addCollegeButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(editButton);

        JLabel studentText = new JLabel("Students");
        studentText.setFont(new Font("Helvetica", Font.BOLD, 32));
        JPanel textContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        textContainer.setOpaque(false);

        textContainer.add(studentText);
        leftPanel.add(textContainer, BorderLayout.SOUTH);

        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setOpaque(false); 
        gbc.gridy = 2;
        gbc.weighty = 0.9;
        this.add(bottomRow, gbc);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        bottomRow.add(scrollPane, BorderLayout.CENTER);
    }
    public JTable getTable() {
        return studentTable.getTable();
    }
    private void filterStudentTable(String searchText, String columnName) {
    if (searchText.isEmpty()) {
        // If search is empty, refresh to show all data
        studentTable.refreshData();
        return;
    }
    
    // Get the table model
    DefaultTableModel model = (DefaultTableModel) studentTable.getTable().getModel();
    
    // Store original data if needed, or reload from database with filter
    // For now, we'll filter the existing table data
    JTable table = studentTable.getTable();
    javax.swing.RowFilter<Object, Object> rf = null;
    
    try {
        if ("All".equals(columnName)) {
            // Search all columns
            rf = javax.swing.RowFilter.regexFilter("(?i)" + searchText);
        } else {
            // Search specific column
            int columnIndex = getColumnIndex(columnName);
            if (columnIndex != -1) {
                rf = javax.swing.RowFilter.regexFilter("(?i)" + searchText, columnIndex);
            }
        }
    } catch (java.util.regex.PatternSyntaxException e) {
        return; // If regex is invalid, don't filter
    }
    
    // Apply the filter
    javax.swing.table.TableRowSorter<DefaultTableModel> sorter = 
        new javax.swing.table.TableRowSorter<>(model);
    table.setRowSorter(sorter);
    sorter.setRowFilter(rf);
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
    
}