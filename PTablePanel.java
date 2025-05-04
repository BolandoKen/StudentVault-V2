import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

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
        
        // Add it to your frame or panel
        this.add(studentTable, BorderLayout.CENTER);

        studentTable.refreshData();

        editButton.addActionListener(e -> {
            DEditStudentDialog dialog = new DEditStudentDialog(this);
            dialog.setVisible(true);
        });
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