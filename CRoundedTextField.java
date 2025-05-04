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

    //First Name field
    public static CRoundedTextField createFirstNameField() {
    CRoundedTextField firstNameField = new CRoundedTextField("First Name", NAME_FIELD_SIZE);
    
    // Add a document listener to track changes to the text field
    firstNameField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            updateFirstName();
        }

        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            updateFirstName();
        }

        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            updateFirstName();
        }
        
        private void updateFirstName() {
            String firstName = firstNameField.getText();
            if (!firstName.equals("First Name") && !firstName.isEmpty()) {
                StudentDataManager.setFirstName(firstName);
            }
        }
    });
    
    return firstNameField;
    }
    
    public static CRoundedTextField createLastNameField() {
        CRoundedTextField lastNameField = new CRoundedTextField("Last Name", NAME_FIELD_SIZE);
    
        // Add a document listener to track changes to the text field
        lastNameField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateLastName();
            }
    
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateLastName();
            }
    
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateLastName();
            }
            
            private void updateLastName() {
                String lastName = lastNameField.getText();
                if (!lastName.equals("Last Name") && !lastName.isEmpty()) {
                    StudentDataManager.setLastName(lastName);
                }
            }
        });
        
        return lastNameField;
    }

    //ID Field
    public static CRoundedTextField createIdField() {
        CRoundedTextField idField = new CRoundedTextField("Id Number", DEFAULT_SIZE);
        
        // Add document filter to enforce ID format (0000-0000)
        ((javax.swing.text.PlainDocument) idField.getDocument()).setDocumentFilter(
            new javax.swing.text.DocumentFilter() {
                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, 
                                  javax.swing.text.AttributeSet attrs) throws javax.swing.text.BadLocationException {
                    
                    // Skip validation if showing placeholder
                    if (idField.showingPlaceholder) {
                        super.replace(fb, offset, length, text, attrs);
                        return;
                    }
                    
                    String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                    String resultText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
                    
                    // Allow backspace and delete operations
                    if (text.isEmpty()) {
                        super.replace(fb, offset, length, text, attrs);
                        return;
                    }
                    
                    // Only allow digits and dash at the correct position
                    if (resultText.length() <= 9) { // Max length is 9 (including the dash)
                        if (offset == 4 && text.equals("-")) {
                            super.replace(fb, offset, length, text, attrs);
                        } else if (text.matches("[0-9]")) {
                            // Auto-insert dash after 4th digit
                            if (offset == 4 && currentText.length() == 4 && !currentText.contains("-")) {
                                super.replace(fb, offset, length, "-" + text, attrs);
                            } else if ((offset < 4 || (offset > 4 && resultText.length() <= 9))) {
                                super.replace(fb, offset, length, text, attrs);
                            }
                        }
                    }
                }
                
                @Override
                public void insertString(FilterBypass fb, int offset, String text, 
                                      javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
                    // Use replace method for consistent handling
                    replace(fb, offset, 0, text, attr);
                }
            }
        );
        
        // Add a document listener to track changes to the text field
        idField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateId();
            }
    
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateId();
            }
    
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateId();
            }
            
            private void updateId() {
                String id = idField.getInputText();
                if (!id.isEmpty()) {
                    // Only update if ID matches the required format
                    if (id.matches("\\d{4}-\\d{4}")) {
                        StudentDataManager.setIdNumber(id);
                    }
                }
            }
        });
        
        return idField;
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
    /**
 * Clears the text field content and resets to placeholder state
 */
public void clearField() {
    setText(placeholder);
    setForeground(placeholderColor);
    showingPlaceholder = true;
}

/**
 * Static method to clear all form fields
 */
public static void clearIdField(CRoundedTextField idField) {
    // For ID field with document filter, we need special handling
    if (idField != null) {
        // Temporarily remove the document filter
        javax.swing.text.AbstractDocument doc = (javax.swing.text.AbstractDocument) idField.getDocument();
        javax.swing.text.DocumentFilter originalFilter = null;
        
        try {
            // Use reflection to get the current document filter (there's no public API for this)
            java.lang.reflect.Field field = javax.swing.text.AbstractDocument.class.getDeclaredField("documentFilter");
            field.setAccessible(true);
            originalFilter = (javax.swing.text.DocumentFilter) field.get(doc);
            
            // Temporarily set filter to null
            doc.setDocumentFilter(null);
            
            // Clear and set placeholder
            idField.setText("Id Number");
            idField.setText(idField.placeholder);
            idField.setForeground(idField.placeholderColor);
            idField.showingPlaceholder = true;
            
        } catch (Exception e) {
            // If reflection fails, fallback to standard method
            idField.setText("Id Number");
            idField.setText(idField.placeholder);
            idField.setForeground(idField.placeholderColor);
            idField.showingPlaceholder = true;
        } finally {
            // Restore the original filter
            if (originalFilter != null) {
                doc.setDocumentFilter(originalFilter);
            }
        }
    }
}

/**
 * Static method to clear all form fields with special handling for ID field
 */
public static void clearAllFields(CRoundedTextField... fields) {
    for (CRoundedTextField field : fields) {
        if (field != null) {
            // Check if this is the ID field by looking at the placeholder
            if (field.placeholder != null && field.placeholder.contains("ID")) {
                clearIdField(field);
            } else {
                field.clearField();
            }
        }
    }
}
}