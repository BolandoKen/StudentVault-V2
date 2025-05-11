import java.awt.FlowLayout;
import javax.swing.*;

public class Dialogs {
    // Remove the private constructor as it's not needed
    // private Dialogs() { ... }

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
        
        // Pack the dialog to size it properly
        dialog.pack();
        
        // Center the dialog
        dialog.setLocationRelativeTo(null);
        
        // Make it visible
        dialog.setVisible(true);
    }
}