import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class PAddStudentPanel extends JPanel {
    private GUI parentFrame;
    private CStudentForm studentForm;

    public PAddStudentPanel(GUI parentFrame, PTablePanel tablePanel) {
        this.parentFrame = parentFrame;
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.white);

        GridBagConstraints gbcRow2 = new GridBagConstraints();
        gbcRow2.fill = GridBagConstraints.BOTH;
        gbcRow2.weightx = 1.0;

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setBackground(new Color(0xEEEEEE));
        topRow.setPreferredSize(new Dimension(1, 100)); 
        gbcRow2.gridy = 0;
        gbcRow2.weighty = 0.02; 
        this.add(topRow, gbcRow2);

        JPanel bottomRow = new JPanel(new BorderLayout());
        gbcRow2.gridy = 1;
        gbcRow2.weighty = 0.98; 
        this.add(bottomRow, gbcRow2);

        JLabel label = new JLabel("StudentVault");
        label.setFont(new Font("Helvetica", Font.BOLD, 32));
        label.setForeground(Color.BLACK);
        
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelPanel.setBackground(new Color(0xEEEEEE));
        labelPanel.add(label);

        topRow.add(labelPanel, BorderLayout.SOUTH);
        
        CRoundedPanel formPanel = new CRoundedPanel(10);
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.white);
        
        studentForm = new CStudentForm(parentFrame, tablePanel,null, null, null);
        formPanel.add(studentForm);
        
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
        wrapperPanel.add(formPanel, BorderLayout.CENTER);
        bottomRow.add(wrapperPanel, BorderLayout.CENTER);

    }
    public CStudentForm getStudentForm() {
        return studentForm;
    }
}
