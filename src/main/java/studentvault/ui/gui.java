package studentvault.ui;
import javax.swing.*;

public class gui extends JFrame {
    public gui() {
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new gui());
    }
}