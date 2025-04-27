package components;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Buttons {
    private static final String ADD_ICON_DEFAULT = "assets/AddIcon.png";
    private static final String ADD_ICON_SELECTED = "assets/SelectedAddIcon.png";
    private static final String TABLE_ICON_DEFAULT = "assets/TableIcon.png";
    private static final String TABLE_ICON_SELECTED = "assets/SelectedTableIcon.png";
    
    private static final Map<String, ImageIcon> iconCache = new HashMap<>();
    
    public static JButton createButton(ImageIcon icon) {
        JButton button = new JButton(icon);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        return button;
    }
    
    public static JButton createAddButton() {
        ImageIcon defaultIcon = loadIcon(ADD_ICON_DEFAULT);
        ImageIcon selectedIcon = loadIcon(ADD_ICON_SELECTED);
        
        JButton button = createButton(defaultIcon);
        button.putClientProperty("defaultIcon", defaultIcon);
        button.putClientProperty("selectedIcon", selectedIcon);
        
        return button;
    }

    public static JButton createTableButton() {
        ImageIcon defaultIcon = loadIcon(TABLE_ICON_DEFAULT);
        ImageIcon selectedIcon = loadIcon(TABLE_ICON_SELECTED);
        
        JButton button = createButton(defaultIcon);
        button.putClientProperty("defaultIcon", defaultIcon);
        button.putClientProperty("selectedIcon", selectedIcon);
        
        return button;
    }
    
    public static void updateButtonState(JButton clickedButton, JButton... allButtons) {
        for (JButton button : allButtons) {
            button.setIcon((ImageIcon) button.getClientProperty("defaultIcon"));
        }
        clickedButton.setIcon((ImageIcon) clickedButton.getClientProperty("selectedIcon"));
    }
    
    private static ImageIcon loadIcon(String path) {
        if (!iconCache.containsKey(path)) {
            iconCache.put(path, new ImageIcon(path));
        }
        return iconCache.get(path);
    }
}