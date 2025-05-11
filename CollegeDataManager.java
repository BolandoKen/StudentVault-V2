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
    private static Integer collegeId;
    
    // Getters and setters
    public static void setCollegeName(String value) { collegeName = value; }
    public static String getCollegeName() { return collegeName; }
    
    public static void setCollegeCode(String value) { collegeCode = value; }
    public static String getCollegeCode() { return collegeCode; }
    
    public static void setCollegeId(Integer value) { collegeId = value; }
    public static Integer getCollegeId() { return collegeId; }
    
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
     * Retrieves college information from the database based on college ID
     * @param id The ID of the college to retrieve
     * @return true if college found and data loaded, false otherwise
     */
    public static boolean getCollegeById(int id) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT college_id, college_name, college_code FROM colleges WHERE college_id = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            ResultSet resultSet = pstmt.executeQuery();
            
            if (resultSet.next()) {
                // Load the retrieved data into the static fields
                collegeId = resultSet.getInt("college_id");
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
     * @param id The ID of the college to update
     * @return true if update successful, false otherwise
     */
    public static boolean updateCollege(int id, String collegeName, String collegeCode) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE colleges SET college_name = ?, college_code = ? WHERE college_id = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, collegeName);
            pstmt.setString(2, collegeCode);
            pstmt.setInt(3, id);
            
            int rowsAffected = pstmt.executeUpdate();
            
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Retrieves the name of a college based on its ID
     * @param id The ID of the college
     * @return The name of the college, or null if not found
     */
    public static String getCollegeName(int id) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT college_name FROM colleges WHERE college_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
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
     * Retrieves the code of a college based on its ID
     * @param id The ID of the college
     * @return The code of the college, or null if not found
     */
    public static String getCollegeCode(int id) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT college_code FROM colleges WHERE college_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("college_code");
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
     * Deletes a college from the database based on college ID
     * @param id The ID of the college to delete
     * @return true if deletion successful, false otherwise
     */
    public static boolean deleteCollege(int id) {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM colleges WHERE college_id = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
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
        collegeId = null;
    }
    
    /**
     * Loads all colleges into a map (id -> name)
     * @return Map of college IDs to names
     */
    public static Map<Integer, String> loadCollegeMap() {
        Map<Integer, String> collegeMap = new HashMap<>();
        
        try (Connection conn = getConnection()) {
            String sql = "SELECT college_id, college_name FROM colleges";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("college_id");
                String name = rs.getString("college_name");
                collegeMap.put(id, name);
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