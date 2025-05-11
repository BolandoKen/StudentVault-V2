import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to manage college data and handle database operations
 */
public class CollegeDataManager {
    // Database connection details (same as StudentDataManager)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentVault";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    
    // College form data
    private static String collegeName;
    private static String collegeCode;
    
    // Getters and setters
    public static void setCollegeName(String value) { collegeName = value; }
    public static String getCollegeName() { return collegeName; }
    
    public static void setCollegeCode(String value) { collegeCode = value; }
    public static String getCollegeCode() { return collegeCode; }
    
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
     * Save college data to the database
     * @return true if successful, false otherwise
     */
    public static boolean saveCollege() {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO colleges (college_name, college_code) VALUES (?, ?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, collegeName);
            pstmt.setString(2, collegeCode);
            
            int rowsAffected = pstmt.executeUpdate();
            
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Retrieves college information from the database based on college code
     * @param code The code of the college to retrieve
     * @return true if college found and data loaded, false otherwise
     */
    public static boolean getCollegeByCode(String code) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT college_name, college_code FROM colleges WHERE college_code = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, code);
            
            ResultSet resultSet = pstmt.executeQuery();
            
            if (resultSet.next()) {
                // Load the retrieved data into the static fields
                collegeName = resultSet.getString("college_name");
                collegeCode = resultSet.getString("college_code");
                
                return true; // College found and data loaded
            } else {
                return false; // College not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Updates existing college information in the database
     * @param oldCode The current code of the college to update
     * @param newName The new name for the college
     * @param newCode The new code for the college
     * @return true if update successful, false otherwise
     */
    public static boolean updateCollege(String oldCode, String newName, String newCode) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE colleges SET college_name = ?, college_code = ? WHERE college_code = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newName);
            pstmt.setString(2, newCode);
            pstmt.setString(3, oldCode);
            
            int rowsAffected = pstmt.executeUpdate();
            
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Retrieves the name of a college based on its code
     * @param code The code of the college
     * @return The name of the college, or null if not found
     */
    public static String getCollegeName(String code) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT college_name FROM colleges WHERE college_code = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, code);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("college_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Check if all required fields are filled
     * @return true if all fields have values, false otherwise
     */
    public static boolean validateFields() {
        return collegeName != null && !collegeName.isEmpty() &&
               collegeCode != null && !collegeCode.isEmpty();
    }
    
    /**
     * Deletes a college from the database based on college code
     * @param code The code of the college to delete
     * @return true if deletion successful, false otherwise
     */
    public static boolean deleteCollege(String code) {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM colleges WHERE college_code = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, code);
            
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
        collegeName = null;
        collegeCode = null;
    }
    
    /**
     * Loads all colleges into a map (code -> name)
     * @return Map of college codes to names
     */
    public static Map<String, String> loadCollegeMap() {
        Map<String, String> collegeMap = new HashMap<>();
        
        try (Connection conn = getConnection()) {
            String sql = "SELECT college_code, college_name FROM colleges";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String code = rs.getString("college_code");
                String name = rs.getString("college_name");
                collegeMap.put(code, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return collegeMap;
    }
    
    /**
     * Checks if a college code already exists
     * @param code The code to check
     * @return true if code exists, false otherwise
     */
    public static boolean collegeCodeExists(String code) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT 1 FROM colleges WHERE college_code = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, code);
            
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Checks if a college name already exists
     * @param name The name to check
     * @return true if name exists, false otherwise
     */
    public static boolean collegeNameExists(String name) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT 1 FROM colleges WHERE college_name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}