import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;

public class PTablePanel extends JPanel{

    private CStudentTable studentTable;

    public PTablePanel() {
        this.setBackground(Color.orange);
        this.setLayout(new BorderLayout());

        // Create the student table panel
        studentTable = new CStudentTable();

        // Add it to your frame or panel
        this.add(studentTable, BorderLayout.CENTER);

        // To refresh the data after adding a new student
        studentTable.refreshData();
    }  
    
    public void refreshTable() {
        if (studentTable != null) {
            studentTable.refreshData();
        }
    }
    
    /**
     * Returns the student table component
     * @return The CStudentTable instance
     */
    public CStudentTable getStudentTable() {
        return studentTable;
    }
   
}