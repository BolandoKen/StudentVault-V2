import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

public class PStudentTablePanel extends JPanel{

    private CStudentTable studentTable;

    public PStudentTablePanel() {
        this.setBackground(Color.orange);
        this.setLayout(new BorderLayout());

        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
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
        SwingUtilities.invokeLater(() -> dialog.getRootPane().requestFocusInWindow());
        } else {
        JOptionPane.showMessageDialog(this, "Please select a row first.");
        }
    });
        deleteButton.addActionListener(e -> {
            JTable table = studentTable.getTable();
            int selectedRow = table.getSelectedRow();
    
            if (selectedRow != -1) {
                String idNumber = table.getValueAt(selectedRow, 3).toString();
                System.out.println("Selected ID Number: " + idNumber);
                StudentDataManager.deleteStudent(idNumber);
                studentTable.refreshData();
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