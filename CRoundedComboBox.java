import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

    public static Map<String, Integer> collegeMap;


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
    CRoundedComboBox genderComboBox = new CRoundedComboBox(genders, "Gender", smallSize);
    
    // Add an action listener to save the selected gender
    genderComboBox.addActionListener(e -> {
        String selectedGender = (String) genderComboBox.getSelectedItem();
        if (selectedGender != null && !selectedGender.equals("Gender")) {
            // Store the selected gender using the StudentDataManager
            StudentDataManager.setGender(selectedGender);
            System.out.println("Gender selected: " + selectedGender);
        }
    });
    
    return genderComboBox;
    }

    public static CRoundedComboBox createYearLevelComboBox() {
        String[] yearLevels = {"1st Year", "2nd Year", "3rd Year", "4th Year", "5+"};
        CRoundedComboBox yearLevelComboBox = new CRoundedComboBox(yearLevels, "Year", smallSize);
        
        // Add an action listener to save the selected year level
        yearLevelComboBox.addActionListener(e -> {
            String selectedYearLevel = (String) yearLevelComboBox.getSelectedItem();
            if (selectedYearLevel != null && !selectedYearLevel.equals("Year")) {
                // Store the selected year level using the StudentDataManager
                StudentDataManager.setYearLevel(selectedYearLevel);
                System.out.println("Year Level selected: " + selectedYearLevel);
            }
        });
        
        return yearLevelComboBox;
    }

    public static CRoundedComboBox createCollegeCombobox() {
        ArrayList<String> collegeList = new ArrayList<>();
        collegeMap = new HashMap<>();
    
        try {
            Connection conn = StudentDataManager.getConnection();
    
            String sql = "SELECT college_id, college_name FROM colleges";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
    
            while (rs.next()) {
                int collegeId = rs.getInt("college_id");
                String collegeName = rs.getString("college_name");
    
                collegeList.add(collegeName);
                collegeMap.put(collegeName, collegeId);
            }
    
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String[] collegesArray = collegeList.toArray(new String[0]);
        CRoundedComboBox collegeComboBox = new CRoundedComboBox(collegesArray, "College", bigSize);
        
        // Add an action listener to save the selected college
        collegeComboBox.addActionListener(e -> {
            String selectedCollege = (String) collegeComboBox.getSelectedItem();
            if (selectedCollege != null && !selectedCollege.equals("College")) {
                // Get the college_id from the map
                Integer collegeId = collegeMap.get(selectedCollege);
                if (collegeId != null) {
                    // Store the selected college ID using the StudentDataManager
                    StudentDataManager.setCollegeId(collegeId);
                    System.out.println("College selected: " + selectedCollege + " (ID: " + collegeId + ")");
                }
            }
        });
        
        return collegeComboBox;
    }

    //Progem ComboBox
    public static CRoundedComboBox createProgramComboBox(int collegeId) {
        ArrayList<String> programList = new ArrayList<>();
        Map<String, Integer> programMap = new HashMap<>();
        
        try {
            Connection conn = StudentDataManager.getConnection();
    
            // Check if we have programs or pragrams table
            boolean programsExists = false;
            boolean pragramsExists = false;
            
            ResultSet tables = conn.getMetaData().getTables(null, null, "%", null);
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                if (tableName.equalsIgnoreCase("programs")) {
                    programsExists = true;
                } else if (tableName.equalsIgnoreCase("pragrams")) {
                    pragramsExists = true;
                }
            }
            
            String tableName = programsExists ? "programs" : (pragramsExists ? "pragrams" : "programs");
            
            // Use correct table name to fetch programs
            String sql = "SELECT program_id, program_name FROM " + tableName + " WHERE college_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, collegeId);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int programId = rs.getInt("program_id");
                String programName = rs.getString("program_name");
                
                programList.add(programName);
                programMap.put(programName, programId);
            }
            
            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching programs: " + e.getMessage());
        }
        
        String[] programsArray = programList.toArray(new String[0]);
        CRoundedComboBox programComboBox = new CRoundedComboBox(programsArray, "Program", bigSize);
        
        // Add action listener to save the selected program
        programComboBox.addActionListener(e -> {
            String selectedProgram = (String) programComboBox.getSelectedItem();
            if (selectedProgram != null && !selectedProgram.equals("Program")) {
                // Get the program_id from the map
                Integer programId = programMap.get(selectedProgram);
                if (programId != null) {
                    // Store the selected program ID using the StudentDataManager
                    StudentDataManager.setProgramId(programId);
                    System.out.println("Program selected: " + selectedProgram + " (ID: " + programId + ")");
                }
            }
        });
        
        return programComboBox;
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