import java.awt.GridLayout;
import java.util.Map;
import javax.swing.*;

public class Dialogs {

    public static void addCollegeDialog(CCollegeTable collegeTable) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Add New College");
        dialog.setLayout(new GridLayout(3, 2));
        
        JTextField collegeNameField = new JTextField(20);
        JTextField collegeCodeField = new JTextField(20);
    
        dialog.add(new JLabel("College Name:"));
        dialog.add(collegeNameField);
        dialog.add(new JLabel("College Code:"));
        dialog.add(collegeCodeField);
    
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
    
        saveButton.addActionListener(e -> {
            String newCollegeName = collegeNameField.getText().trim();
            String newCollegeCode = collegeCodeField.getText().trim();
            
            // Validate inputs
            if (newCollegeName.isEmpty() || newCollegeCode.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "College name and code cannot be empty", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check for existing college code
            if (CollegeDataManager.collegeCodeExists(newCollegeCode)) {
                JOptionPane.showMessageDialog(dialog, 
                    "A college with this code already exists", 
                    "Duplicate College Code", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check for existing college name
            if (CollegeDataManager.collegeNameExists(newCollegeName)) {
                JOptionPane.showMessageDialog(dialog, 
                    "A college with this name already exists", 
                    "Duplicate College Name", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Set the college details
            CollegeDataManager.setCollegeName(newCollegeName);
            CollegeDataManager.setCollegeCode(newCollegeCode);
            
            // Attempt to save the college
            if (CollegeDataManager.saveCollege()) {
                // Refresh the table to show the new data
                collegeTable.refreshTable();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Failed to add college due to a database error", 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
    
        dialog.add(saveButton);
        dialog.add(cancelButton);
        
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    public static void editCollegeDialog(String collegeCode, CCollegeTable collegeTable) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Edit College");
        dialog.setLayout(new GridLayout(3, 2));
        
        JTextField collegeNameField = new JTextField(20);
        collegeNameField.setText(CollegeDataManager.getCollegeName(collegeCode));
    
        JTextField collegeCodeField = new JTextField(20);
        collegeCodeField.setText(collegeCode);
    
        dialog.add(new JLabel("College Name:"));
        dialog.add(collegeNameField);
        dialog.add(new JLabel("College Code:"));
        dialog.add(collegeCodeField);
    
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
    
        saveButton.addActionListener(e -> {
            String newCollegeName = collegeNameField.getText().trim();
            String newCollegeCode = collegeCodeField.getText().trim();
            
            // Validate inputs
            if (newCollegeName.isEmpty() || newCollegeCode.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "College name and code cannot be empty", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Confirm code change if different from original
            if (!newCollegeCode.equals(collegeCode)) {
                int confirm = JOptionPane.showConfirmDialog(
                    dialog, 
                    "Changing the college code will update all related programs. Continue?", 
                    "Confirm Update", 
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            
            // Perform the update
            if (CollegeDataManager.updateCollege(collegeCode, newCollegeName, newCollegeCode)) {
                // Refresh the table to show updated data
                collegeTable.refreshTable();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Failed to update college", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
    
        dialog.add(saveButton);
        dialog.add(cancelButton);
        
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    public static void deleteCollegeDialog(String collegeCode, CCollegeTable collegeTable) {

        int confirm = JOptionPane.showConfirmDialog(
            null, 
            "Are you sure you want to delete this college? This action cannot be undone.", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (CollegeDataManager.deleteCollege(collegeCode)) {
                // Refresh the table to reflect the deletion
                collegeTable.refreshTable();
                JOptionPane.showMessageDialog(
                    null, 
                    "College deleted successfully.", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    null, 
                    "Failed to delete college. Please try again.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    public static void addProgramDialog(CProgramTable programTable) {
    JDialog dialog = new JDialog();
    dialog.setTitle("Add New Program");
    dialog.setLayout(new GridLayout(4, 2));
    
    JTextField programNameField = new JTextField(20);
    JTextField programCodeField = new JTextField(20);
    
    // Create a college dropdown
    JComboBox<String> collegeDropdown = new JComboBox<>();
    Map<String, String> collegeMap = CollegeDataManager.loadCollegeMap();
    // Add a prompt as first item
    collegeDropdown.addItem("-- Select College --");
    // Add all colleges to dropdown
    for (Map.Entry<String, String> entry : collegeMap.entrySet()) {
        collegeDropdown.addItem(entry.getValue() + " (" + entry.getKey() + ")");
    }
    
    dialog.add(new JLabel("Program Name:"));
    dialog.add(programNameField);
    dialog.add(new JLabel("Program Code:"));
    dialog.add(programCodeField);
    dialog.add(new JLabel("College:"));
    dialog.add(collegeDropdown);
    
    JButton saveButton = new JButton("Save");
    JButton cancelButton = new JButton("Cancel");
    
    saveButton.addActionListener(e -> {
        String newProgramName = programNameField.getText().trim();
        String newProgramCode = programCodeField.getText().trim();
        
        // Get selected college
        int selectedIndex = collegeDropdown.getSelectedIndex();
        
        // Validate inputs
        if (newProgramName.isEmpty() || newProgramCode.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, 
                "Program name and code cannot be empty", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate college selection
        if (selectedIndex <= 0) {
            JOptionPane.showMessageDialog(dialog, 
                "Please select a college for this program", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check for existing program code
        if (ProgramDataManager.programCodeExists(newProgramCode)) {
            JOptionPane.showMessageDialog(dialog, 
                "A program with this code already exists", 
                "Duplicate Program Code", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check for existing program name
        if (ProgramDataManager.programNameExists(newProgramName)) {
            JOptionPane.showMessageDialog(dialog, 
                "A program with this name already exists", 
                "Duplicate Program Name", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Extract college code from selected item
        String selectedCollege = (String) collegeDropdown.getSelectedItem();
        String collegeCode = selectedCollege.substring(selectedCollege.lastIndexOf("(") + 1, selectedCollege.lastIndexOf(")"));
        
        // Attempt to add the program directly
        if (ProgramDataManager.addProgram(newProgramName, newProgramCode, collegeCode)) {
            // Refresh the table to show the new data
            programTable.refreshData();
            dialog.dispose();
        } else {
            JOptionPane.showMessageDialog(dialog, 
                "Failed to add program due to a database error", 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    });
    
    cancelButton.addActionListener(e -> dialog.dispose());
    
    dialog.add(saveButton);
    dialog.add(cancelButton);
    
    dialog.pack();
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);
}
    public static void editProgramDialog(String programCode, CProgramTable programTable) {
    JDialog dialog = new JDialog();
    dialog.setTitle("Edit Program");
    dialog.setLayout(new GridLayout(4, 2)); // Changed to GridLayout for better organization
    
    // Load current program data
    ProgramDataManager.getProgramByCode(programCode);
    
    // Create form fields
    JTextField programNameField = new JTextField(20);
    programNameField.setText(ProgramDataManager.getProgramName());
    
    JTextField programCodeField = new JTextField(20);
    programCodeField.setText(ProgramDataManager.getProgramCode());
    
    // Create college dropdown
    JComboBox<String> collegeDropdown = new JComboBox<>();
    Map<String, String> collegeMap = CollegeDataManager.loadCollegeMap();
    
    // Add default option
    collegeDropdown.addItem("-- Select College --");
    
    // Add colleges to dropdown
    for (Map.Entry<String, String> entry : collegeMap.entrySet()) {
        collegeDropdown.addItem(entry.getValue() + " (" + entry.getKey() + ")");
    }
    
    // Set current college as selected
    String currentCollegeCode = ProgramDataManager.getCollegeCode();
    if (currentCollegeCode != null) {
        for (int i = 0; i < collegeDropdown.getItemCount(); i++) {
            String item = collegeDropdown.getItemAt(i);
            if (item.contains("(" + currentCollegeCode + ")")) {
                collegeDropdown.setSelectedIndex(i);
                break;
            }
        }
    }
    
    // Add components to dialog
    dialog.add(new JLabel("Program Name:"));
    dialog.add(programNameField);
    dialog.add(new JLabel("Program Code:"));
    dialog.add(programCodeField);
    dialog.add(new JLabel("College:"));
    dialog.add(collegeDropdown);
    
    // Create buttons
    JButton saveButton = new JButton("Save");
    JButton cancelButton = new JButton("Cancel");
    
    saveButton.addActionListener(e -> {
        String newProgramName = programNameField.getText().trim();
        String newProgramCode = programCodeField.getText().trim();
        
        // Validate inputs
        if (newProgramName.isEmpty() || newProgramCode.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, 
                "Program name and code cannot be empty", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Validate college selection
        if (collegeDropdown.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(dialog, 
                "Please select a college for this program", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Extract college code from selected item
        String selectedCollege = (String) collegeDropdown.getSelectedItem();
        String collegeCode = selectedCollege.substring(
            selectedCollege.lastIndexOf("(") + 1, 
            selectedCollege.lastIndexOf(")")
        );
        
        // Update program
        if (ProgramDataManager.updateProgram(
            programCode, 
            newProgramName, 
            newProgramCode, 
            collegeCode
        )) {
            programTable.refreshData();
            dialog.dispose();
            JOptionPane.showMessageDialog(
                dialog, 
                "Program updated successfully", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                dialog, 
                "Failed to update program", 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    });
    
    cancelButton.addActionListener(e -> dialog.dispose());
    
    dialog.add(saveButton);
    dialog.add(cancelButton);
    
    dialog.pack();
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);
}
    public static void deleteProgramDialog(String programCode, CProgramTable programTable) {
    int confirm = JOptionPane.showConfirmDialog(
        null, 
        "Are you sure you want to delete this program? This action cannot be undone.", 
        "Confirm Delete", 
        JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        if (ProgramDataManager.deleteProgram(programCode)) {
            // Refresh the table to reflect the deletion
            programTable.refreshData();
            JOptionPane.showMessageDialog(
                null, 
                "Program deleted successfully.", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                null, 
                "Failed to delete program. Please try again.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
    
}
