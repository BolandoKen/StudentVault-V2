import java.awt.FlowLayout;
import javax.swing.*;

public class Dialogs {
    // Remove the private constructor as it's not needed
    // private Dialogs() { ... }

    public static void editCollegeDialog(int college_Id, CCollegeTable collegeTable) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Edit College");
        dialog.setLayout(new FlowLayout());
        
        JTextField collegeNameField = new JTextField(20);
        collegeNameField.setText(CollegeDataManager.getCollegeName(college_Id));

        JTextField collegeCodeField = new JTextField(20);
        collegeCodeField.setText(CollegeDataManager.getCollegeCode(college_Id));

        dialog.add(new JLabel("College Name:"));
        dialog.add(collegeNameField);
        dialog.add(new JLabel("College Code:"));
        dialog.add(collegeCodeField);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            String collegeName = collegeNameField.getText();
            String collegeCode = collegeCodeField.getText();
            CollegeDataManager.updateCollege(college_Id, collegeName, collegeCode);
            collegeTable.refreshTable();
            dialog.dispose();
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