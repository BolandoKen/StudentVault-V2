import java.awt.GridLayout;
import java.util.Map;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class DStudentDialogs {
    public static void addStudentDialog(CStudentTable studentTable) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Add New Student");
        dialog.setLayout(new GridLayout(8, 2)); // Changed from 7 to 8 rows
        
        // Create form fields
        JTextField firstNameField = new JTextField(20);
        JTextField lastNameField = new JTextField(20);
        
        // Create gender dropdown
        JComboBox<String> genderDropdown = new JComboBox<>();
        genderDropdown.addItem("-- Select Gender --");
        genderDropdown.addItem("Male");
        genderDropdown.addItem("Female");
        genderDropdown.addItem("Other");
        
        // Create ID number field with input verification
        JTextField idNumberField = new JTextField(20);
        idNumberField.setDocument(new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                // Only allow digits and hyphen in specific position
                if (str.matches("[0-9-]+")) {
                    String currentText = getText(0, getLength());
                    String newText = currentText.substring(0, offs) + str + currentText.substring(offs);
                    
                    // Enforce 0000-0000 format
                    if (newText.length() > 9) return; // Max length is 9 (8 digits + 1 hyphen)
                    if (newText.length() == 5 && !newText.substring(4, 5).equals("-")) {
                        super.insertString(offs, "-", a);
                        if (str.length() > 0) {
                            super.insertString(offs + 1, str, a);
                        }
                    } else {
                        super.insertString(offs, str.replaceAll("[^0-9]", ""), a);
                    }
                }
            }
        });
        
        // Create year level dropdown
        JComboBox<String> yearLevelDropdown = new JComboBox<>();
        yearLevelDropdown.addItem("-- Select Year Level --");
        yearLevelDropdown.addItem("1");
        yearLevelDropdown.addItem("2");
        yearLevelDropdown.addItem("3");
        yearLevelDropdown.addItem("4");
        yearLevelDropdown.addItem("5");
        
        // Create college dropdown
        JComboBox<String> collegeDropdown = new JComboBox<>();
        Map<String, String> collegeMap = CollegeDataManager.loadCollegeMap();
        collegeDropdown.addItem("-- Select College --");
        for (Map.Entry<String, String> entry : collegeMap.entrySet()) {
            collegeDropdown.addItem(entry.getValue() + " (" + entry.getKey() + ")");
        }
        
        // Create program dropdown
        JComboBox<String> programDropdown = new JComboBox<>();
        programDropdown.addItem("-- Select Program --");
        programDropdown.setEnabled(false); // Initially disabled
        
        // Add listener to college dropdown to update programs
        collegeDropdown.addActionListener(e -> {
            int selectedIndex = collegeDropdown.getSelectedIndex();
            if (selectedIndex > 0) {
                // Extract college code from selected item
                String selectedCollege = (String) collegeDropdown.getSelectedItem();
                String collegeCode = selectedCollege.substring(
                    selectedCollege.lastIndexOf("(") + 1, 
                    selectedCollege.lastIndexOf(")")
                );
                
                // Load programs for selected college
                programDropdown.removeAllItems();
                programDropdown.addItem("-- Select Program --");
                Map<String, String> programMap = ProgramDataManager.loadProgramsByCollege(collegeCode);
                for (Map.Entry<String, String> entry : programMap.entrySet()) {
                    programDropdown.addItem(entry.getValue() + " (" + entry.getKey() + ")");
                }
                programDropdown.setEnabled(true);
            } else {
                // Reset program dropdown if no college selected
                programDropdown.removeAllItems();
                programDropdown.addItem("-- Select Program --");
                programDropdown.setEnabled(false);
            }
        });
        
        // Add components to dialog
        dialog.add(new JLabel("First Name:"));
        dialog.add(firstNameField);
        dialog.add(new JLabel("Last Name:"));
        dialog.add(lastNameField);
        dialog.add(new JLabel("Gender:"));
        dialog.add(genderDropdown);
        dialog.add(new JLabel("ID Number (0000-0000 format):"));
        dialog.add(idNumberField);
        dialog.add(new JLabel("Year Level:"));
        dialog.add(yearLevelDropdown);
        dialog.add(new JLabel("College:"));
        dialog.add(collegeDropdown);
        dialog.add(new JLabel("Program:"));
        dialog.add(programDropdown);
        
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            // Get field values
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String gender = genderDropdown.getSelectedIndex() > 0 ? (String) genderDropdown.getSelectedItem() : "";
            String idNumber = idNumberField.getText().trim();
            String yearLevel = yearLevelDropdown.getSelectedIndex() > 0 ? (String) yearLevelDropdown.getSelectedItem() : "";
            
            // Validate inputs
            if (firstName.isEmpty() || lastName.isEmpty() || gender.isEmpty() || 
                idNumber.isEmpty() || yearLevel.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "All fields are required", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
    
        // ====== NEW VALIDATION: No special characters in names ======
        if (!firstName.matches("[a-zA-Z\\s]+")) {
            JOptionPane.showMessageDialog(dialog, 
                "First Name can only contain letters and spaces", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        if (!lastName.matches("[a-zA-Z\\s]+")) {
            JOptionPane.showMessageDialog(dialog, 
                "Last Name can only contain letters and spaces", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
            
            // Validate ID number format (0000-0000)
            if (!idNumber.matches("\\d{4}-\\d{4}")) {
                JOptionPane.showMessageDialog(dialog, 
                    "ID Number must be in 0000-0000 format", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate college selection
            if (collegeDropdown.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(dialog, 
                    "Please select a college", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate program selection
            if (programDropdown.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(dialog, 
                    "Please select a program", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check for existing student ID
            if (StudentDataManager.getStudentById(idNumber) != null) {
                JOptionPane.showMessageDialog(dialog, 
                    "A student with this ID already exists", 
                    "Duplicate Student ID", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Extract program code
            String selectedProgram = (String) programDropdown.getSelectedItem();
            String programCode = selectedProgram.substring(
                selectedProgram.lastIndexOf("(") + 1, 
                selectedProgram.lastIndexOf(")")
            );
            
            // Create student object
            Student student = new Student(firstName, lastName, gender, idNumber, yearLevel, programCode);
            
            // Save student
            if (StudentDataManager.saveStudent(student)) {
                studentTable.refreshData();
                dialog.dispose();
                JOptionPane.showMessageDialog(
                    dialog, 
                    "Student added successfully", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    dialog, 
                    "Failed to add student", 
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

    public static void editStudentDialog(String studentId, CStudentTable studentTable) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Edit Student");
        dialog.setLayout(new GridLayout(8, 2));
        
        Student student = StudentDataManager.getStudentById(studentId);
        if (student == null) {
            JOptionPane.showMessageDialog(
                null, 
                "Student not found", 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        
        // Store original ID for database update
        final String originalId = student.getIdNumber();
        
        // Create form fields with current data
        JTextField firstNameField = new JTextField(student.getFirstName(), 20);
        JTextField lastNameField = new JTextField(student.getLastName(), 20);
        
        // Create gender dropdown with current selection
        JComboBox<String> genderDropdown = new JComboBox<>();
        genderDropdown.addItem("-- Select Gender --");
        genderDropdown.addItem("Male");
        genderDropdown.addItem("Female");
        genderDropdown.addItem("Other");
        // Set current gender
        for (int i = 0; i < genderDropdown.getItemCount(); i++) {
            if (genderDropdown.getItemAt(i).equals(student.getGender())) {
                genderDropdown.setSelectedIndex(i);
                break;
            }
        }
        
// Create ID number field with input verification
JTextField idNumberField = new JTextField(20);
idNumberField.setDocument(new PlainDocument() {
    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        String currentText = getText(0, getLength());
        String newText = currentText.substring(0, offs) + str + currentText.substring(offs);
        
        // Only allow digits and exactly one hyphen in position 4
        if (newText.matches("^\\d{0,4}-?\\d{0,4}$")) {
            super.insertString(offs, str, a);
        }
    }
});
idNumberField.setText(student.getIdNumber());

        // Create year level dropdown with current selection
        JComboBox<String> yearLevelDropdown = new JComboBox<>();
        yearLevelDropdown.addItem("-- Select Year Level --");
        yearLevelDropdown.addItem("1");
        yearLevelDropdown.addItem("2");
        yearLevelDropdown.addItem("3");
        yearLevelDropdown.addItem("4");
        yearLevelDropdown.addItem("5");
        // Set current year level
        for (int i = 0; i < yearLevelDropdown.getItemCount(); i++) {
            if (yearLevelDropdown.getItemAt(i).equals(student.getYearLevel())) {
                yearLevelDropdown.setSelectedIndex(i);
                break;
            }
        }
        
        // Create college dropdown
        JComboBox<String> collegeDropdown = new JComboBox<>();
        Map<String, String> collegeMap = CollegeDataManager.loadCollegeMap();
        collegeDropdown.addItem("-- Select College --");
        for (Map.Entry<String, String> entry : collegeMap.entrySet()) {
            collegeDropdown.addItem(entry.getValue() + " (" + entry.getKey() + ")");
        }
        
        // Create program dropdown
        JComboBox<String> programDropdown = new JComboBox<>();
        programDropdown.addItem("-- Select Program --");
        
        // Find the college for the current program
        String currentProgramCode = student.getProgramCode();
        String currentCollegeCode = null;
        
        // Get the college code for the current program
        if (currentProgramCode != null && !currentProgramCode.isEmpty()) {
            if (ProgramDataManager.getProgramByCode(currentProgramCode)) {
                currentCollegeCode = ProgramDataManager.getCollegeCode();
            }
        }
        
        // Set current college as selected and load its programs
        if (currentCollegeCode != null) {
            for (int i = 0; i < collegeDropdown.getItemCount(); i++) {
                String item = collegeDropdown.getItemAt(i);
                if (item.contains("(" + currentCollegeCode + ")")) {
                    collegeDropdown.setSelectedIndex(i);
                    
                    // Load programs for current college
                    programDropdown.removeAllItems();
                    programDropdown.addItem("-- Select Program --");
                    Map<String, String> programMap = ProgramDataManager.loadProgramsByCollege(currentCollegeCode);
                    for (Map.Entry<String, String> entry : programMap.entrySet()) {
                        programDropdown.addItem(entry.getValue() + " (" + entry.getKey() + ")");
                    }
                    
                    // Set current program as selected
                    for (int j = 0; j < programDropdown.getItemCount(); j++) {
                        String programItem = programDropdown.getItemAt(j);
                        if (programItem.contains("(" + currentProgramCode + ")")) {
                            programDropdown.setSelectedIndex(j);
                            break;
                        }
                    }
                    break;
                }
            }
        }
        
        // Add listener to college dropdown to update programs
        collegeDropdown.addActionListener(e -> {
            int selectedIndex = collegeDropdown.getSelectedIndex();
            if (selectedIndex > 0) {
                // Extract college code from selected item
                String selectedCollege = (String) collegeDropdown.getSelectedItem();
                String collegeCode = selectedCollege.substring(
                    selectedCollege.lastIndexOf("(") + 1, 
                    selectedCollege.lastIndexOf(")")
                );
                
                // Load programs for selected college
                programDropdown.removeAllItems();
                programDropdown.addItem("-- Select Program --");
                Map<String, String> programMap = ProgramDataManager.loadProgramsByCollege(collegeCode);
                for (Map.Entry<String, String> entry : programMap.entrySet()) {
                    programDropdown.addItem(entry.getValue() + " (" + entry.getKey() + ")");
                }
                programDropdown.setEnabled(true);
            } else {
                // Reset program dropdown if no college selected
                programDropdown.removeAllItems();
                programDropdown.addItem("-- Select Program --");
                programDropdown.setEnabled(false);
            }
        });
        
        // Add components to dialog
        dialog.add(new JLabel("First Name:"));
        dialog.add(firstNameField);
        dialog.add(new JLabel("Last Name:"));
        dialog.add(lastNameField);
        dialog.add(new JLabel("Gender:"));
        dialog.add(genderDropdown);
        dialog.add(new JLabel("ID Number (0000-0000 format):"));
        dialog.add(idNumberField);
        dialog.add(new JLabel("Year Level:"));
        dialog.add(yearLevelDropdown);
        dialog.add(new JLabel("College:"));
        dialog.add(collegeDropdown);
        dialog.add(new JLabel("Program:"));
        dialog.add(programDropdown);
        
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            // Get field values
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String gender = genderDropdown.getSelectedIndex() > 0 ? (String) genderDropdown.getSelectedItem() : "";
            String newIdNumber = idNumberField.getText().trim();
            String yearLevel = yearLevelDropdown.getSelectedIndex() > 0 ? (String) yearLevelDropdown.getSelectedItem() : "";
            
            // Validate inputs
            if (firstName.isEmpty() || lastName.isEmpty() || gender.isEmpty() || 
                newIdNumber.isEmpty() || yearLevel.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "All fields are required", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (firstName.isEmpty() || lastName.isEmpty() || gender.isEmpty() || 
            newIdNumber.isEmpty() || yearLevel.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, 
                "All fields are required", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // ====== NEW VALIDATION: No special characters in names ======
        if (!firstName.matches("[a-zA-Z\\s]+")) {
            JOptionPane.showMessageDialog(dialog, 
                "First Name can only contain letters and spaces", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        if (!lastName.matches("[a-zA-Z\\s]+")) {
            JOptionPane.showMessageDialog(dialog, 
                "Last Name can only contain letters and spaces", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
            if (!newIdNumber.matches("\\d{4}-\\d{4}")) {
                JOptionPane.showMessageDialog(dialog, 
                    "ID Number must be in 0000-0000 format", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check for existing student ID (only if ID has changed)
            if (!newIdNumber.equals(originalId) && StudentDataManager.getStudentById(newIdNumber) != null) {
                JOptionPane.showMessageDialog(dialog, 
                    "A student with this ID already exists", 
                    "Duplicate Student ID", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate college selection
            if (collegeDropdown.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(dialog, 
                    "Please select a college", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate program selection
            if (programDropdown.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(dialog, 
                    "Please select a program", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Extract program code
            String selectedProgram = (String) programDropdown.getSelectedItem();
            String programCode = selectedProgram.substring(
                selectedProgram.lastIndexOf("(") + 1, 
                selectedProgram.lastIndexOf(")")
            );
            
            // Update student object
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setGender(gender);
            student.setIdNumber(newIdNumber);
            student.setYearLevel(yearLevel);
            student.setProgramCode(programCode);
            
            // Save changes (using original ID to locate the record)
            if (StudentDataManager.updateStudentWithIdChange(originalId, student)) {
                studentTable.refreshData();
                dialog.dispose();
                JOptionPane.showMessageDialog(
                    dialog, 
                    "Student updated successfully", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    dialog, 
                    "Failed to update student", 
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

    public static void deleteStudentDialog(String studentId, CStudentTable studentTable) {
        int confirm = JOptionPane.showConfirmDialog(
            null, 
            "Are you sure you want to delete this student? This action cannot be undone.", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (StudentDataManager.deleteStudent(studentId)) {
                studentTable.refreshData();
                JOptionPane.showMessageDialog(
                    null, 
                    "Student deleted successfully", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                    null, 
                    "Failed to delete student", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}