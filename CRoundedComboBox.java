import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class CRoundedComboBox extends JComboBox<String> {

    private static final int DEFAULT_CORNER_RADIUS = 15;
    private static final Color DEFAULT_BACKGROUND = new Color(240, 240, 240);
    private static final Color DEFAULT_TEXT_COLOR = Color.GRAY;
    private static final Color DEFAULT_SELECTION_COLOR = new Color(100, 150, 220);
    private static final Font DEFAULT_FONT = new Font("Helvetica", Font.PLAIN, 18);
    
    private static final Dimension smallSize = new Dimension(130, 40);
    private static final Dimension bigSize = new Dimension(300, 40);
    private static final Dimension DEFAULT_SIZE = new Dimension(200, 30);
    
    private int cornerRadius;
    private Color selectionColor;

    public CRoundedComboBox() {
        this(new String[]{}, "Select an option", DEFAULT_SIZE);
    }

    public CRoundedComboBox(String[] items, String placeholder, Dimension size) {
        this(items, DEFAULT_BACKGROUND, DEFAULT_TEXT_COLOR, DEFAULT_FONT, 
             DEFAULT_CORNER_RADIUS, size, placeholder, DEFAULT_SELECTION_COLOR);
    }
  
    public CRoundedComboBox(String[] items, Color backgroundColor, Color textColor, 
                           Font font, int cornerRadius, Dimension size, 
                           String placeholder, Color selectionColor) {
        super();
        
        this.cornerRadius = cornerRadius;
        this.selectionColor = selectionColor;
        
        setupUI(backgroundColor, textColor, font, size, placeholder);
        
        if (placeholder != null && !placeholder.isEmpty()) {
            addItem(placeholder);
        }
        
        for (String item : items) {
            addItem(item);
        }
        
        setSelectedIndex(0);
    }
    
    private void setupUI(Color backgroundColor, Color textColor, Font font, 
                        Dimension size, String placeholder) {
        setUI(new RoundedComboBoxUI(backgroundColor, cornerRadius));
        setBackground(backgroundColor);
        setForeground(textColor);
        setFont(font);
        setOpaque(false);
        setBorder(new EmptyBorder(5, 10, 5, 10));
        setPreferredSize(size);
        
        setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (value != null && value.equals(placeholder)) {
                    setForeground(Color.GRAY);
                } else {
                    setForeground(textColor);
                }
                
                if (isSelected) {
                    setBackground(selectionColor);
                    setForeground(Color.WHITE);
                }
                
                return this;
            }
        });
    }
    
  
    public static CRoundedComboBox createGenderComboBox() {
        String[] genders = {"Male", "Female", "Non-binary", "Prefer not to say"};
        return new CRoundedComboBox(genders, "Gender", smallSize);
    }

    public static CRoundedComboBox createYearLevelComboBox() {
        String[] yearLevels = {"1st Year", "2nd Year", "3rd Year", "4th Year", "5+"};
        return new CRoundedComboBox(yearLevels, "Year", smallSize);
    }

    public static CRoundedComboBox createCollegeCombobox(){
        ArrayList<String> collegeList = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/StudentVault";
        String username = "root";
        String password = "root";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement stmt = conn.createStatement();
            String sql = "SELECT college_name FROM colleges";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String collegeName = rs.getString("college_name");
                collegeList.add(collegeName);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] collegesArray = collegeList.toArray(new String[0]);

        return new CRoundedComboBox(collegesArray, "College", bigSize);
    }
    public static CRoundedComboBox createProgramComboBox() {
        String[] programs = {"Computer Science", "Information Technology", "Engineering", "Business Administration", "Psychology"};
        return new CRoundedComboBox(programs, "Program", bigSize);
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
    
    private static class RoundedComboBoxUI extends BasicComboBoxUI {
        private final Color backgroundColor;
        private final int cornerRadius;
        
        public RoundedComboBoxUI(Color backgroundColor, int cornerRadius) {
            this.backgroundColor = backgroundColor;
            this.cornerRadius = cornerRadius;
        }
        
        @Override
        protected JButton createArrowButton() {
            JButton button = new JButton("▼");
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            button.setFont(new Font("Arial", Font.BOLD, 12));
            button.setForeground(Color.DARK_GRAY);
            return button;
        }
        
        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), cornerRadius, cornerRadius);
            
            g2.dispose();
            super.paint(g, c);
        }
    }
}