import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

public class PTablePanel extends JPanel{

    private CStudentTable studentTable;

    public PTablePanel() {
        this.setBackground(Color.orange);
        this.setLayout(new BorderLayout());

        JButton editButton = new JButton("Edit");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(editButton);
        this.add(buttonPanel, BorderLayout.NORTH);
        
        studentTable = new CStudentTable();

       editButton.addActionListener(e -> {
        JTable table = studentTable.getTable();
        int selectedRow = table.getSelectedRow();
    
        if (selectedRow != -1) {
        String idNumber = table.getValueAt(selectedRow, 3).toString();
        System.out.println("Selected ID Number: " + idNumber);
        StudentDataManager.getStudentById(idNumber);
        DEditStudentDialog dialog = new DEditStudentDialog(this, idNumber, studentTable);
        dialog.setVisible(true);
        } else {
        JOptionPane.showMessageDialog(this, "Please select a row first.");
        }
    });

        this.add(studentTable, BorderLayout.CENTER);

        studentTable.refreshData();

    }  
    
    public void refreshTable() {
        if (studentTable != null) {
            studentTable.refreshData();
        }
    }
    
    public CStudentTable getStudentTable() {
        return studentTable;
    }
   
}