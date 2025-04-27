package app;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.*;
import panels.AddStudent;
import panels.SearchPanel;
import panels.SidePanel;
import panels.TablePanel;

public class GUI extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel rightContainer;
    
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
        SidePanel sidePanel = new SidePanel(this);
        mainBackground.add(sidePanel, gbc);

        gbc.weightx = 1;
        gbc.gridx = 1;
        rightContainer = new JPanel(cardLayout);
        mainBackground.add(rightContainer, gbc);
        
        JPanel tableCard = new JPanel(new GridBagLayout());
        GridBagConstraints tableCardGBC = new GridBagConstraints();
        tableCardGBC.fill = GridBagConstraints.BOTH;
        
        tableCardGBC.weighty = 0;
        tableCardGBC.weightx = 1;
        tableCardGBC.gridy = 0;
        SearchPanel searchPanel = new SearchPanel();
        tableCard.add(searchPanel, tableCardGBC);

        tableCardGBC.weighty = 1;
        tableCardGBC.gridy = 1;
        TablePanel tablePanel = new TablePanel();
        tableCard.add(tablePanel, tableCardGBC);

        JPanel addStudentCard = new JPanel(new BorderLayout());
        AddStudent addStudentPanel = new AddStudent();
        addStudentCard.add(addStudentPanel);

        rightContainer.add(tableCard, "TableCard");
        rightContainer.add(addStudentCard, "AddStudentCard");

        this.add(mainBackground);
        this.setVisible(true);
    }
    
    public void switchPanel(String panelName) {
        cardLayout.show(rightContainer, panelName);
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