import java.awt.*;
import javax.swing.*;

public class PSidePanel extends JPanel {
    private JButton addButton;
    private JButton tableButton;
    private JLabel logo;

    public PSidePanel(GUI parentFrame) { 
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(new Color(0x5C2434));
        this.setPreferredSize(new Dimension(98, 1024));
        this.setMinimumSize(new Dimension(98, 0));

        addButton = CButtons.createAddButton();
        tableButton = CButtons.createTableButton();
        logo = Logo.createLogo();
        
        this.add(logo);
        this.add(addButton);
        this.add(tableButton);

        addButton.addActionListener(e -> {
            CButtons.updateButtonState(addButton, addButton, tableButton);
            parentFrame.switchPanel("AddStudentCard");
        });
        
        tableButton.addActionListener(e -> {
            CButtons.updateButtonState(tableButton, addButton, tableButton);
            parentFrame.switchPanel("TableCard");
        });
        
        // Set table button as active by default
        CButtons.updateButtonState(tableButton, addButton, tableButton);
    }
}