import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.*;

public class Dialogs {
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

   public static void editProgramDialog(String programCode, CProgramTable programTable) {
    JDialog dialog = new JDialog();
    dialog.setTitle("Edit Program");
    dialog.setLayout(new FlowLayout());
    
    JTextField programNameField = new JTextField(20);
    programNameField.setText(ProgramDataManager.getProgramName(programCode));

    JTextField programCodeField = new JTextField(20);
    programCodeField.setText(programCode);

    dialog.add(new JLabel("Program Name:"));
    dialog.add(programNameField);
    dialog.add(new JLabel("Program Code:"));
    dialog.add(programCodeField);

    JButton saveButton = new JButton("Save");
    JButton cancelButton = new JButton("Cancel");

    saveButton.addActionListener(e -> {
        String newProgramName = programNameField.getText();
        String newProgramCode = programCodeField.getText();
        
        if (!ProgramDataManager.updateProgram(programCode, newProgramName, newProgramCode)) {
            JOptionPane.showMessageDialog(dialog, 
                "Failed to update program", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        } else {
            programTable.refreshData();
            dialog.dispose();
        }
    });
    
    cancelButton.addActionListener(e -> dialog.dispose());

    dialog.add(saveButton);
    dialog.add(cancelButton);
    
    dialog.pack();
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);
}
}