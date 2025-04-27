package panels;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

public class SidePanel extends JPanel{
    public SidePanel() {
        this.setBackground(Color.GREEN);
        this.setPreferredSize(new Dimension(98, 1024));
        this.setMinimumSize(new Dimension(98, 0));
    }
        
}
