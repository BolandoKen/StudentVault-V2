
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CStudentForm extends JPanel{
    private GUI parentFrame;
    private PTablePanel tablePanel;

    public CStudentForm(GUI parentFrame, PTablePanel tablePanel) {
        this.parentFrame = parentFrame;
        this.tablePanel = tablePanel;
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.white);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        JPanel additionalInfoPanel = new JPanel(new GridBagLayout());
        additionalInfoPanel.setPreferredSize(new Dimension(450, 54));
        additionalInfoPanel.setBackground(Color.white);

        JLabel titleLabel = new JLabel("Add New Student");
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 32));
        additionalInfoPanel.add(titleLabel);
        add(additionalInfoPanel, gbc);

        //Firstname, Lastname row
        gbc.gridy = 1;
        JPanel nameRow = new JPanel(new GridBagLayout());
        nameRow.setPreferredSize(new Dimension(450, 54));
        nameRow.setBackground(Color.white);

        GridBagConstraints nameGbc = new GridBagConstraints();
        nameGbc.gridy = 0;
        nameGbc.fill = GridBagConstraints.BOTH;
        nameGbc.weightx = 1;
        nameGbc.insets = new java.awt.Insets(5, 10, 5, 10);

        nameGbc.gridx = 0;
        CRoundedTextField firstnameField = CRoundedTextField.createFirstNameField();
        nameRow.add(firstnameField, nameGbc);

        nameGbc.gridx = 1;
        CRoundedTextField lastnameField = CRoundedTextField.createLastNameField();
        nameRow.add(lastnameField, nameGbc);
        this.add(nameRow, gbc);

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
        CRoundedComboBox genderComboBox = CRoundedComboBox.createGenderComboBox();
        row2.add(genderComboBox, row2Gbc);

        //Id TextField
        row2Gbc.gridx = 1;
        CRoundedTextField idField = CRoundedTextField.createIdField();
        row2.add(idField, row2Gbc);

        //YearLevel Dropdown
        row2Gbc.gridx = 2;
        CRoundedComboBox yearLevelComboBox = CRoundedComboBox.createYearLevelComboBox();
        row2.add(yearLevelComboBox, row2Gbc);

        this.add(row2, gbc);

        //Collage Row
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        JPanel row3 = new JPanel(new GridBagLayout());
        row3.setPreferredSize(new Dimension(450, 54));
        row3.setBackground(Color.WHITE);

        GridBagConstraints row3Gbc = new GridBagConstraints();
        row3Gbc.gridy = 0;
        row3Gbc.fill = GridBagConstraints.BOTH;
        row3Gbc.weightx = 1; 
        row3Gbc.insets = new java.awt.Insets(5, 10, 5, 10);

        //CollegePanel for Collage Dropdown and Add Button
        JPanel collegePanel = new JPanel(new BorderLayout());
        collegePanel.setOpaque(false);

        CRoundedComboBox collegeComboBox = CRoundedComboBox.createCollegeCombobox();

        JButton addCollegeButton = new JButton("+");
        addCollegeButton.setBackground(new Color(0xE7E7E7));
        setForeground(Color.BLACK);
        addCollegeButton.setPreferredSize(new Dimension(80, 40));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        buttonPanel.add(addCollegeButton);

        collegePanel.removeAll();
        collegePanel.setLayout(new BorderLayout(10, 0)); 
        collegePanel.add(collegeComboBox, BorderLayout.CENTER);
        collegePanel.add(buttonPanel, BorderLayout.EAST);

        row3.add(collegePanel, row3Gbc);
        add(row3, gbc);

        // Row 4: Program Panel
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        JPanel row4 = new JPanel(new GridBagLayout());
        row4.setPreferredSize(new Dimension(450, 54));
        row4.setBackground(Color.WHITE);

        GridBagConstraints row4Gbc = new GridBagConstraints();
        row4Gbc.gridy = 0;
        row4Gbc.fill = GridBagConstraints.BOTH;
        row4Gbc.weightx = 1; 
        row4Gbc.insets = new java.awt.Insets(5, 10, 5, 10);

        JPanel programPanel = new JPanel(new BorderLayout());
        programPanel.setOpaque(false);

        CRoundedComboBox programComboBox = new CRoundedComboBox(new String[0], "Program", new Dimension(300, 40));

        programPanel.add(programComboBox, BorderLayout.CENTER);
        row4.add(programPanel, row4Gbc);
        add(row4, gbc);

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
        row5.setPreferredSize(new Dimension(450, 54));
        row5.setBackground(Color.WHITE);

        GridBagConstraints row5Gbc = new GridBagConstraints();
        row5Gbc.gridy = 0;
        row5Gbc.fill = GridBagConstraints.BOTH;
        row5Gbc.weightx = 1; 
        row5Gbc.insets = new java.awt.Insets(5, 10, 5, 10);

        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(Color.white);

        CButtons addStudentButton = CButtons.createAddStudentButton(tablePanel.getStudentTable());
        actionPanel.add(addStudentButton, BorderLayout.CENTER);
        row5.add(actionPanel, row5Gbc);
        this.add(row5, gbc);

        // Row 6: Cancel Button
        gbc.gridy = 6;
        JPanel row6 = new JPanel(new GridBagLayout());
        row6.setPreferredSize(new Dimension(450, 54));
        row6.setBackground(Color.WHITE);

        GridBagConstraints row6Gbc = new GridBagConstraints();
        row6Gbc.gridy = 0;
        row6Gbc.fill = GridBagConstraints.BOTH;
        row6Gbc.weightx = 1; 
        row6Gbc.insets = new java.awt.Insets(5, 10, 5, 10);

        JPanel cancelPanel = new JPanel(new BorderLayout());
        cancelPanel.setBackground(Color.white);
        CButtons cancelButton = CButtons.createCancelButton();
        cancelPanel.add(cancelButton, BorderLayout.CENTER);
        row6.add(cancelPanel, row6Gbc);
        this.add(row6, gbc);

        cancelButton.addActionListener(e -> {
            parentFrame.switchPanel("TableCard");
        });

    } 
    }
