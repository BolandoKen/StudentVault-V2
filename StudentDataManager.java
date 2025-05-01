import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}