
import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PSearchPanel extends JPanel{
    private CRoundedTextField searchField;
    public PSearchPanel() {
        this.setBackground(Color.blue);
        this.setPreferredSize(new Dimension(1440, 72));
        this.setMinimumSize(new Dimension(100, 72));
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new java.awt.Insets(10, 10, 10, 0);
        gbc.weightx = 1.0;
 
        CRoundedPanel searchPanel = new CRoundedPanel(10);
        searchPanel.setLayout(new BorderLayout(10, 0)); // Add horizontal gap between components
        searchPanel.setBackground(new Color(0xE7E7E7));

        // Create a panel for the search field and the search by combo box
        JPanel searchControlsPanel = new JPanel(new BorderLayout(10, 0)); // Increased gap
        searchControlsPanel.setOpaque(false);
        
        // Create the search field
        searchField = CRoundedTextField.createSearchField();
       
        // Add some padding around the combo box
        JPanel searchByWrapper = new JPanel(new BorderLayout());
        searchByWrapper.setOpaque(false);
        searchByWrapper.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        //searchByWrapper.add(searchByField, BorderLayout.CENTER);
        
        // Create the clear button
        JButton clearButton = createButton("Assets/XIcon.png");

        // Add components to the search controls panel
        searchControlsPanel.add(searchField, BorderLayout.CENTER);
        searchControlsPanel.add(searchByWrapper, BorderLayout.WEST);
        
        // Add padding inside the search panel
        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setOpaque(false);
        paddedPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        paddedPanel.add(searchControlsPanel, BorderLayout.CENTER);
        
        searchPanel.add(paddedPanel, BorderLayout.CENTER);
        searchPanel.add(clearButton, BorderLayout.EAST);

        this.add(searchPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.insets = new java.awt.Insets(10, 0, 10, 10);
        JButton filterButton = createButton("Assets/FilterIcon.png");
        this.add(filterButton, gbc);
    }

    private JButton createButton(String iconPath) {
        JButton button = new JButton(new ImageIcon(iconPath));
        button.setPreferredSize(new Dimension(40, 40));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        return button;
    }
    
    
}

