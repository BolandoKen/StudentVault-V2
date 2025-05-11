import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DEditStudentDialog extends JDialog{

    private CRoundedTextField firstnameField;
    private CRoundedTextField lastnameField;
    private CRoundedTextField idField;
    private CRoundedComboBox genderComboBox;
    private CRoundedComboBox yearLevelComboBox;
    private CRoundedComboBox collegeComboBox;
    private CRoundedComboBox programComboBox;

    public DEditStudentDialog(JPanel parent, String idNumber, CStudentTable studentTable) {
        super(); 
        this.getContentPane().setBackground(Color.white);
        StudentDataManager.getStudentById(idNumber);

        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.white);
        formPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        JPanel additionalInfoPanel = new JPanel(new GridBagLayout());
        additionalInfoPanel.setBackground(Color.white);

        JLabel titleLabel = new JLabel("Edit Student");
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 32));
        additionalInfoPanel.add(titleLabel);
        formPanel.add(additionalInfoPanel, gbc);

        //Firstname, Lastname row
        gbc.gridy = 1;
        JPanel nameRow = new JPanel(new GridBagLayout());
        nameRow.setBackground(Color.white);

        GridBagConstraints nameGbc = new GridBagConstraints();
        nameGbc.gridy = 0;
        nameGbc.fill = GridBagConstraints.BOTH;
        nameGbc.weightx = 1;
        nameGbc.insets = new java.awt.Insets(5, 10, 5, 10);

        nameGbc.gridx = 0;
        firstnameField = CRoundedTextField.createFirstNameField();

        nameRow.add(firstnameField, nameGbc);

        nameGbc.gridx = 1;
        lastnameField = CRoundedTextField.createLastNameField();
        nameRow.add(lastnameField, nameGbc);
        formPanel.add(nameRow, gbc);

        //Gender, Id, YearLevel row
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;

        JPanel row2 = new JPanel(new GridBagLayout());
        row2.setBackground(Color.white);
        GridBagConstraints row2Gbc = new GridBagConstraints();
        row2Gbc.gridy = 0;
        row2Gbc.fill = GridBagConstraints.BOTH;
        row2Gbc.weightx = 1;
        row2Gbc.insets = new java.awt.Insets(5, 10, 5, 10);

        //Gender Dropdown
        row2Gbc.gridx = 0;
        genderComboBox = CRoundedComboBox.createGenderComboBox();
        row2.add(genderComboBox, row2Gbc);

        //Id TextField
        row2Gbc.gridx = 1;
        idField = CRoundedTextField.createIdField();
        row2.add(idField, row2Gbc);

        //YearLevel Dropdown
        row2Gbc.gridx = 2;
        yearLevelComboBox = CRoundedComboBox.createYearLevelComboBox();
        row2.add(yearLevelComboBox, row2Gbc);

        formPanel.add(row2, gbc);

        firstnameField.setText(StudentDataManager.getFirstName());
        lastnameField.setText(StudentDataManager.getLastName());
        idField.setText(idNumber);
        idField.setEditable(false);

        //Collage Row
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        JPanel row3 = new JPanel(new GridBagLayout());
        row3.setBackground(Color.WHITE);

        GridBagConstraints row3Gbc = new GridBagConstraints();
        row3Gbc.gridy = 0;
        row3Gbc.fill = GridBagConstraints.BOTH;
        row3Gbc.weightx = 1; 
        row3Gbc.insets = new java.awt.Insets(5, 10, 5, 10);

        //CollegePanel for Collage Dropdown and Add Button
        JPanel collegePanel = new JPanel(new BorderLayout());
        collegePanel.setOpaque(false);

        collegeComboBox = CRoundedComboBox.createCollegeCombobox();

        JButton addCollegeButton = new JButton("+");
        addCollegeButton.setBackground(new Color(0xE7E7E7));
        setForeground(Color.BLACK);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        buttonPanel.add(addCollegeButton);

        collegePanel.removeAll();
        collegePanel.setLayout(new BorderLayout(10, 0)); 
        collegePanel.add(collegeComboBox, BorderLayout.CENTER);
        collegePanel.add(buttonPanel, BorderLayout.EAST);

        row3.add(collegePanel, row3Gbc);
        formPanel.add(row3, gbc);

        // Row 4: Program Panel
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        JPanel row4 = new JPanel(new GridBagLayout());
        row4.setBackground(Color.WHITE);

        GridBagConstraints row4Gbc = new GridBagConstraints();
        row4Gbc.gridy = 0;
        row4Gbc.fill = GridBagConstraints.BOTH;
        row4Gbc.weightx = 1; 
        row4Gbc.insets = new java.awt.Insets(5, 10, 5, 10);

        JPanel programPanel = new JPanel(new BorderLayout());
        programPanel.setOpaque(false);

        programComboBox = new CRoundedComboBox(new String[0], "Program", new Dimension(300, 40));

        programPanel.add(programComboBox, BorderLayout.CENTER);
        row4.add(programPanel, row4Gbc);
        formPanel.add(row4, gbc);

        StudentDataManager.loadStudentAndSetComboBoxes(idNumber, collegeComboBox, programComboBox);

        setComboBoxSelection(genderComboBox, StudentDataManager.getGender());
        setComboBoxSelection(yearLevelComboBox, StudentDataManager.getYearLevel());
        
        collegeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // Get the selected college
                String selectedCollege = (String) collegeComboBox.getSelectedItem();
                // Skip placeholder option
                if (selectedCollege != null && !selectedCollege.equals("College")) {
                    // Get the college_id from the map
                    Integer collegeId = CRoundedComboBox.collegeMap.get(selectedCollege);
                    
                    if (collegeId != null) {
                        // Create program combo box based on the selected college_id
                        CRoundedComboBox updatedProgramComboBox = CRoundedComboBox.createProgramComboBox(collegeId);
            
                        // Replace the old program combo box with the updated one
                        programPanel.removeAll();
                        programPanel.add(updatedProgramComboBox, BorderLayout.CENTER);
                        programPanel.revalidate();
                        programPanel.repaint();
                    }
                }
            }
        });

        // Row 5: AddStudent Button
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        JPanel row5 = new JPanel(new GridBagLayout());
        row5.setBackground(Color.WHITE);

        GridBagConstraints row5Gbc = new GridBagConstraints();
        row5Gbc.gridy = 0;
        row5Gbc.fill = GridBagConstraints.BOTH;
        row5Gbc.weightx = 1; 
        row5Gbc.insets = new java.awt.Insets(5, 10, 5, 10);

        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(Color.white);

        CButtons saveButton = CButtons.saveButton();

        saveButton.addActionListener(e -> {
        if (!StudentDataManager.validateFields()) {
            JOptionPane.showMessageDialog(null, 
                "Please fill in all required fields.", 
                "Missing Information", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        boolean success = StudentDataManager.updateStudent(idNumber);
        
        if (success) {

            studentTable.refreshData();
            
            // Show success message
            JOptionPane.showMessageDialog(null, 
                "Student added successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
        } else {
            JOptionPane.showMessageDialog(null, 
                "Failed to add student. Please try again.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        this.dispose();
    });
  
        actionPanel.add(saveButton, BorderLayout.CENTER);
        row5.add(actionPanel, row5Gbc);
        formPanel.add(row5, gbc);

        // Row 6: Cancel Button
        gbc.gridy = 6;
        JPanel row6 = new JPanel(new GridBagLayout());
        row6.setBackground(Color.WHITE);

        GridBagConstraints row6Gbc = new GridBagConstraints();
        row6Gbc.gridy = 0;
        row6Gbc.fill = GridBagConstraints.BOTH;
        row6Gbc.weightx = 1; 
        row6Gbc.insets = new java.awt.Insets(5, 10, 5, 10);

     
        CButtons cancelButton = CButtons.createCancelButton();
        cancelButton.requestFocusInWindow();
    
        row6.add(cancelButton, row6Gbc);
        formPanel.add(row6, gbc);

        this.add(formPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(parent);

        cancelButton.addActionListener(e -> {
            this.dispose();
        });
    }
    private void setComboBoxSelection(CRoundedComboBox comboBox, String value) {
        if (value != null) {
            for (int i = 0; i < comboBox.getItemCount(); i++) {
                if (value.equals(comboBox.getItemAt(i))) {
                    comboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    }
}
