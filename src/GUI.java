import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.*;
import panels.SearchPanel;
import panels.SidePanel;
import panels.TablePanel;


public class GUI extends JFrame {

    public GUI() {
        this.setTitle("StudentVault");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1440, 1024);
        ImageIcon studentVaultLogo = new ImageIcon("Assets/StudentVaultLogo.png");
        this.setIconImage(studentVaultLogo.getImage());
        
        JPanel mainBackground = new JPanel(new GridBagLayout());
        mainBackground.setBackground(Color.RED);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        gbc.weighty = 1;
        gbc.weightx = 0;
        gbc.gridx = 0;
        SidePanel sidePanel = new SidePanel();
        mainBackground.add(sidePanel, gbc);

        gbc.weightx = 1;
        gbc.gridx = 1;
        JPanel rightContainer = new JPanel(new GridBagLayout());
        mainBackground.add(rightContainer, gbc);

        GridBagConstraints rightContainerGbc = new GridBagConstraints();
        rightContainerGbc.fill = GridBagConstraints.BOTH;
        
        rightContainerGbc.weighty = 0;
        rightContainerGbc.weightx = 1;
        rightContainerGbc.gridy = 0;
        SearchPanel searchPanel = new SearchPanel();
        rightContainer.add(searchPanel, rightContainerGbc);

        rightContainerGbc.weighty = 1;
        rightContainerGbc.gridy = 1;
        TablePanel tablePanel = new TablePanel();
        rightContainer.add(tablePanel, rightContainerGbc);


        this.add(mainBackground);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new GUI());
    }
}
