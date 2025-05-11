import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * Class to manage student data and handle database operations
 */
public class StudentDataManager {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentVault";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    
    // Student form data
    private static String firstName;
    private static String lastName;
    private static String gender;
    private static String idNumber;
    private static String yearLevel;
    private static Integer collegeId;
    private static Integer programId;
    
    // Getters and setters
    public static void setFirstName(String value) { firstName = value; }
    public static String getFirstName() { return firstName; }
    
    public static void setLastName(String value) { lastName = value; }
    public static String getLastName() { return lastName; }
    
    public static void setGender(String value) { gender = value; }
    public static String getGender() { return gender; }
    
    public static void setIdNumber(String value) { idNumber = value; }
    public static String getIdNumber() { return idNumber; }
    
    public static void setYearLevel(String value) { yearLevel = value; }
    public static String getYearLevel() { return yearLevel; }
    
    public static void setCollegeId(Integer value) { collegeId = value; }
    public static Integer getCollegeId() { return collegeId; }
    
    public static void setProgramId(Integer value) { programId = value; }
    public static Integer getProgramId() { return programId; }
    
    /**
     * Create a database connection
     * @return A Connection object
     * @throws SQLException if connection fails
     * @throws ClassNotFoundException if driver not found
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    /**
     * Save student data to the database
     * @return true if successful, false otherwise
     */
    public static boolean saveStudent() {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO students (first_name, last_name, gender, id_number, year_level, college_id, program_id) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, gender);
            pstmt.setString(4, idNumber);
            pstmt.setString(5, yearLevel);
            pstmt.setInt(6, collegeId);
            pstmt.setInt(7, programId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
 * Retrieves student information from the database based on student ID
 * @param studentId The ID number of the student to retrieve
 * @return true if student found and data loaded, false otherwise
 */
public static boolean getStudentById(String studentId) {
    try (Connection conn = getConnection()) {
        String sql = "SELECT first_name, last_name, gender, id_number, year_level, college_id, program_id " +
                     "FROM students WHERE id_number = ?";
        
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, studentId);
        
        var resultSet = pstmt.executeQuery();
        
        if (resultSet.next()) {
            // Load the retrieved data into the static fields
            firstName = resultSet.getString("first_name");
            lastName = resultSet.getString("last_name");
            gender = resultSet.getString("gender");
            idNumber = resultSet.getString("id_number");
            yearLevel = resultSet.getString("year_level");
            collegeId = resultSet.getInt("college_id");
            programId = resultSet.getInt("program_id");
            
            return true; // Student found and data loaded
        } else {
            return false; // Student not found
        }
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

/**
 * Updates existing student information in the database
 * @param studentId The ID number of the student to update
 * @return true if update successful, false otherwise
 */
public static boolean updateStudent(String studentId) {
    try (Connection conn = getConnection()) {
        String sql = "UPDATE students SET first_name = ?, last_name = ?, gender = ?, " +
                     "year_level = ?, college_id = ?, program_id = ? WHERE id_number = ?";
        
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, firstName);
        pstmt.setString(2, lastName);
        pstmt.setString(3, gender);
        pstmt.setString(4, yearLevel);
        pstmt.setInt(5, collegeId);
        pstmt.setInt(6, programId);
        pstmt.setString(7, studentId);
        
        int rowsAffected = pstmt.executeUpdate();
        
        return rowsAffected > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
    
    /**
     * Check if all required fields are filled
     * @return true if all fields have values, false otherwise
     */
    public static boolean validateFields() {
        return firstName != null && !firstName.isEmpty() &&
               lastName != null && !lastName.isEmpty() &&
               gender != null && !gender.isEmpty() &&
               idNumber != null && !idNumber.isEmpty() &&
               yearLevel != null && !yearLevel.isEmpty() &&
               collegeId != null &&
               programId != null;
    }

    /**
     * Deletes a student from the database based on student ID
     * @param studentId The ID number of the student to delete
     * @return true if deletion successful, false otherwise
     */
    public static boolean deleteStudent(String studentId) {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM students WHERE id_number = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, studentId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Clear all form data
     */
    public static void clearFormData() {
        firstName = null;
        lastName = null;
        gender = null;
        idNumber = null;
        yearLevel = null;
        collegeId = null;
        programId = null;
    }
    public static boolean loadStudentAndSetComboBoxes(String studentId, 
                                                  CRoundedComboBox collegeComboBox, 
                                                  CRoundedComboBox programComboBox) {
    // First retrieve the student data
    boolean studentFound = StudentDataManager.getStudentById(studentId);
    
    if (!studentFound) {
        return false; // Student not found
    }
    
    // Get the college and program IDs from the retrieved student data
    Integer collegeId = StudentDataManager.getCollegeId();
    Integer programId = StudentDataManager.getProgramId();
    
    if (collegeId == null || programId == null) {
        return false; // Invalid data
    }
    
    // Set the college ComboBox
    boolean collegeFound = false;
    String collegeName = null;
    
    // Find the college name by ID
    for (Map.Entry<String, Integer> entry : CRoundedComboBox.collegeMap.entrySet()) {
        if (entry.getValue().equals(collegeId)) {
            collegeName = entry.getKey();
            collegeFound = true;
            break;
        }
    }
    
    if (!collegeFound || collegeName == null) {
        return false; // College not found
    }
    
    // Set the college ComboBox selection
    for (int i = 0; i < collegeComboBox.getItemCount(); i++) {
        String item = collegeComboBox.getItemAt(i);
        if (collegeName.equals(item)) {
            collegeComboBox.setSelectedIndex(i);
            break;
        }
    }
    
    // Create/Update the program ComboBox based on the college ID
    CRoundedComboBox updatedProgramComboBox = CRoundedComboBox.createProgramComboBox(collegeId);
    
    // Replace all items in the existing programComboBox with items from updatedProgramComboBox
    programComboBox.removeAllItems();
    for (int i = 0; i < updatedProgramComboBox.getItemCount(); i++) {
        programComboBox.addItem(updatedProgramComboBox.getItemAt(i));
    }
    
    // Find the program name by ID and set the selection
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
        
        // Get program name by ID
        String sql = "SELECT program_name FROM " + tableName + " WHERE program_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, programId);
        
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            String programName = rs.getString("program_name");
            
            // Set the program ComboBox selection
            for (int i = 0; i < programComboBox.getItemCount(); i++) {
                String item = programComboBox.getItemAt(i);
                if (programName.equals(item)) {
                    programComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
        
        rs.close();
        pstmt.close();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
    
    return true;
}
}