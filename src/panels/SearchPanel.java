package panels;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

public class SearchPanel extends JPanel{
    public SearchPanel() {
        this.setBackground(Color.blue);
        this.setPreferredSize(new Dimension(1440, 72));
        this.setMinimumSize(new Dimension(100, 72));
       
    }
}

