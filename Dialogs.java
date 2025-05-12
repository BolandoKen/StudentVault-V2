import java.awt.FlowLayout;
import javax.swing.*;

public class Dialogs {
    public static void editCollegeDialog(String collegeCode, CCollegeTable collegeTable) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Edit College");
        dialog.setLayout(new FlowLayout());
        
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
            String collegeName = collegeNameField.getText();
            String newCollegeCode = collegeCodeField.getText();
            
            if (!CollegeDataManager.updateCollege(collegeCode, collegeName, newCollegeCode)) {
                JOptionPane.showMessageDialog(dialog, 
                    "Failed to update college", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            } else {
                collegeTable.refreshTable();
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