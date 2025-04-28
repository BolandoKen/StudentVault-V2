import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CRoundedTextField extends JTextField {
    private static final int DEFAULT_CORNER_RADIUS = 15;
    private static final Color DEFAULT_BACKGROUND = new Color(240, 240, 240);
    private static final Color DEFAULT_TEXT_COLOR = Color.BLACK;
    private static final Color DEFAULT_PLACEHOLDER_COLOR = Color.GRAY;
    private static final Font DEFAULT_FONT = new Font("Helvetica", Font.PLAIN, 18);
    private static final Dimension DEFAULT_SIZE = new Dimension(130, 30);
    private static final Dimension NAME_FIELD_SIZE = new Dimension(180, 40);
    
    private int cornerRadius;
    private String placeholder;
    private boolean showingPlaceholder;
    private Color placeholderColor;
    
    public CRoundedTextField() {
        this("", DEFAULT_SIZE);
    }
    
    public CRoundedTextField(String placeholder, Dimension size) {
        this(placeholder, DEFAULT_BACKGROUND, DEFAULT_TEXT_COLOR, DEFAULT_FONT, 
             DEFAULT_CORNER_RADIUS, size, DEFAULT_PLACEHOLDER_COLOR);
    }

    public CRoundedTextField(String placeholder, Color backgroundColor, Color textColor, 
                            Font font, int cornerRadius, Dimension size, 
                            Color placeholderColor) {
        super();
        
        this.cornerRadius = cornerRadius;
        this.placeholder = placeholder;
        this.showingPlaceholder = true;
        this.placeholderColor = placeholderColor;
        
        setupUI(backgroundColor, textColor, font, size);
        
        if (placeholder != null && !placeholder.isEmpty()) {
            setText(placeholder);
            setForeground(placeholderColor);
        }
    }
    
    private void setupUI(Color backgroundColor, Color textColor, Font font, Dimension size) {
        setOpaque(false);
        setBackground(backgroundColor);
        setForeground(textColor);
        setFont(font);
        setBorder(new EmptyBorder(10, 15, 10, 15));
        setPreferredSize(size);
        
        // Add focus listener for placeholder behavior
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (showingPlaceholder) {
                    setText("");
                    setForeground(textColor);
                    showingPlaceholder = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty() && placeholder != null && !placeholder.isEmpty()) {
                    setText(placeholder);
                    setForeground(placeholderColor);
                    showingPlaceholder = true;
                }
            }
        });
    }
    
    public static CRoundedTextField createFirstNameField() {
        return new CRoundedTextField("First Name", NAME_FIELD_SIZE);
    }
    
    public static CRoundedTextField createLastNameField() {
        return new CRoundedTextField("Last Name", NAME_FIELD_SIZE);
    }

    public static CRoundedTextField createIdField() {
        return new CRoundedTextField("ID", DEFAULT_SIZE);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        
        g2.dispose();
        super.paintComponent(g);
    }
    
    @Override
    protected void paintBorder(Graphics g) {
        // No border implementation
    }
    
    /**
     * Returns the actual text entered by user, not the placeholder
     */
    public String getInputText() {
        return showingPlaceholder ? "" : getText();
    }
}