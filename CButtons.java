import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class CButtons extends JButton {
    // Default appearance constants
    private static final int DEFAULT_CORNER_RADIUS = 15;
    private static final Color DEFAULT_BACKGROUND = new Color(66, 133, 244);  // Blue
    private static final Color DEFAULT_TEXT_COLOR = Color.WHITE;
    private static final Font DEFAULT_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Dimension DEFAULT_SIZE = new Dimension(120, 40);
    private static final Dimension ADD_COLLEGE_SIZE = new Dimension(150, 45);
    
    // Icon paths
    private static final String ADD_ICON_DEFAULT = "assets/AddIcon.png";
    private static final String ADD_ICON_SELECTED = "assets/SelectedAddIcon.png";
    private static final String TABLE_ICON_DEFAULT = "assets/TableIcon.png";
    private static final String TABLE_ICON_SELECTED = "assets/SelectedTableIcon.png";
    
    // Icon cache
    private static final Map<String, ImageIcon> iconCache = new HashMap<>();
    
    // Button properties
    private final int cornerRadius;
    private Color backgroundColor;
    private Color hoverColor;
    private Color originalColor;
    private Color textColor;
    private boolean isIconButton;

    /**
     * Default constructor with standard appearance
     */
    public CButtons(String text) {
        this(text, DEFAULT_BACKGROUND, DEFAULT_TEXT_COLOR, DEFAULT_FONT, DEFAULT_CORNER_RADIUS, DEFAULT_SIZE);
    }
    
    /**
     * Constructor with basic customization
     */
    public CButtons(String text, Color backgroundColor, Color textColor) {
        this(text, backgroundColor, textColor, DEFAULT_FONT, DEFAULT_CORNER_RADIUS, DEFAULT_SIZE);
    }
    
    /**
     * Icon button constructor
     */
    public CButtons(ImageIcon icon) {
        super(icon);
        this.cornerRadius = 0; // Transparent background for icon buttons
        this.isIconButton = true;
        setupIconButton();
    }
    
    /**
     * Full constructor with customization options
     */
    public CButtons(String text, Color backgroundColor, Color textColor, 
                        Font font, int cornerRadius, Dimension size) {
        super(text);
        this.cornerRadius = cornerRadius;
        this.originalColor = backgroundColor;
        this.backgroundColor = new Color(backgroundColor.getRGB());  // Create a copy to prevent issues
        this.hoverColor = calculateHoverColor(backgroundColor);
        this.textColor = textColor;
        this.isIconButton = false;

        setupUI(font, size);
    }
    
    private void setupUI(Font font, Dimension size) {
        setFont(font);
        setForeground(textColor);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setPreferredSize(size);
        
        // Add hover effect
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!isIconButton) {
                    setBackground(hoverColor);
                }
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!isIconButton) {
                    setBackground(originalColor);
                }
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
    
    private void setupIconButton() {
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        // Add hover effect (cursor only)
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }
    
    private Color calculateHoverColor(Color baseColor) {
        // Darken the color for hover effect
        return new Color(
            Math.max((int)(baseColor.getRed() * 0.85), 0),
            Math.max((int)(baseColor.getGreen() * 0.85), 0),
            Math.max((int)(baseColor.getBlue() * 0.85), 0)
        );
    }
    
    public static CButtons createAddCollegeButton() {
        Color addButtonColor = new Color(46, 125, 50);  
        CButtons button = new CButtons("Add College", addButtonColor, Color.WHITE, 
                                              new Font("Helvetica", Font.BOLD, 16), 
                                              DEFAULT_CORNER_RADIUS, ADD_COLLEGE_SIZE);
        return button;
    }
    
    
    public static CButtons createAddStudentButton(CStudentTable studentTable) {
        Color addButtonColor = new Color(0x5C2434);  
        CButtons button = new CButtons("Add Student", addButtonColor, Color.WHITE, 
                                              new Font("Helvetica", Font.PLAIN, 18), 
                                              DEFAULT_CORNER_RADIUS, DEFAULT_SIZE);

        button.addActionListener(e -> {


        // Validate required fields
        if (!StudentDataManager.validateFields()) {
            JOptionPane.showMessageDialog(null, 
                "Please fill in all required fields.", 
                "Missing Information", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Try to save data to database
        boolean success = StudentDataManager.saveStudent();
        
        if (success) {

            studentTable.refreshData();
            
            JOptionPane.showMessageDialog(null, 
                "Student added successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Clear form data
            StudentDataManager.clearFormData();
            
            // Trigger form reset (you'll need to implement this)
            // CStudentForm.resetForm();
        } else {
            JOptionPane.showMessageDialog(null, 
                "Failed to add student. Please try again.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    });
    
    return button;
    }

    public static CButtons createCancelButton(){
        Color addButtonColor = new Color(0xE7E7E7); 
        CButtons button = new CButtons("Cancel", addButtonColor, Color.BLACK, 
                                              new Font("Helvetica", Font.PLAIN, 18), 
                                              DEFAULT_CORNER_RADIUS, DEFAULT_SIZE);
        return button;
    }
  
    public static CButtons createIconButton(ImageIcon icon) {
        return new CButtons(icon);
    }
  
    public static CButtons createAddIconButton() {
        ImageIcon defaultIcon = loadIcon(ADD_ICON_DEFAULT);
        ImageIcon selectedIcon = loadIcon(ADD_ICON_SELECTED);
        
        CButtons button = createIconButton(defaultIcon);
        button.putClientProperty("defaultIcon", defaultIcon);
        button.putClientProperty("selectedIcon", selectedIcon);
        
        return button;
    }
    
    public static CButtons createTableIconButton() {
        ImageIcon defaultIcon = loadIcon(TABLE_ICON_DEFAULT);
        ImageIcon selectedIcon = loadIcon(TABLE_ICON_SELECTED);
        
        CButtons button = createIconButton(defaultIcon);
        button.putClientProperty("defaultIcon", defaultIcon);
        button.putClientProperty("selectedIcon", selectedIcon);
        
        return button;
    }
    
    /**
     * Updates button states for a group of buttons
     */
    public static void updateButtonState(CButtons clickedButton, CButtons... allButtons) {
        for (CButtons button : allButtons) {
            button.setIcon((ImageIcon) button.getClientProperty("defaultIcon"));
        }
        clickedButton.setIcon((ImageIcon) clickedButton.getClientProperty("selectedIcon"));
    }
    
    /**
     * Loads icon from path with caching
     */
    private static ImageIcon loadIcon(String path) {
        if (!iconCache.containsKey(path)) {
            iconCache.put(path, new ImageIcon(path));
        }
        return iconCache.get(path);
    }

    @Override
    public void setBackground(Color color) {
        if (!isIconButton) {
            this.backgroundColor = color;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (!isIconButton) {
            // For regular rounded buttons, draw the background
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw rounded background
            g2.setColor(backgroundColor);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

            g2.dispose();
        }
        
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // No border for both types of buttons
    }
}