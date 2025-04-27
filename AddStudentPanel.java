import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class AddStudentPanel extends JPanel {
    public AddStudentPanel() {
        this.setBackground(Color.white);
        this.setLayout(new GridBagLayout());

        GridBagConstraints mainGBC = new GridBagConstraints();
        mainGBC.fill = GridBagConstraints.BOTH;
        mainGBC.weightx = 1.0;  // Make the components take full width

        // First row (topRow)
        mainGBC.gridy = 0;
        mainGBC.weighty = 1;  // This row takes up a small amount of space
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setBackground(Color.green);
        JLabel topRowJLabel = new JLabel("StudentVault");
        topRowJLabel.setHorizontalAlignment(SwingConstants.LEFT);
        topRow.add(topRowJLabel, BorderLayout.WEST);  // Align to the left

        // Second row (bottomRow)
        mainGBC.gridy = 1;  // Move to the next row
        mainGBC.weighty = 1;  // This row takes up more space
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setBackground(Color.gray);
        JLabel bottomRowJLabel = new JLabel("bottom row");
        bottomRow.add(bottomRowJLabel, BorderLayout.WEST);  // Align to the left

        // Add both rows to the panel
        this.add(topRow, mainGBC);
        this.add(bottomRow, mainGBC);
    }
}
