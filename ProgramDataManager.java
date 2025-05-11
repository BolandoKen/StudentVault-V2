import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to manage program data and handle database operations
 */
public class ProgramDataManager {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentVault";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    
    // Program form data
    private static String programName;
    private static String programCode;
    private static Integer programId;
    private static Integer collegeId;
    
    // Getters and setters
    public static void setProgramName(String value) { programName = value; }
    public static String getProgramName() { return programName; }
    
    public static void setProgramCode(String value) { programCode = value; }
    public static String getProgramCode() { return programCode; }
    
    public static void setProgramId(Integer value) { programId = value; }
    public static Integer getProgramId() { return programId; }
    
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
     * Save program data to the database
     * @return true if successful, false otherwise
     */
    public static boolean saveProgram() {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO programs (program_name, program_code, college_id) VALUES (?, ?, ?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, programName);
            pstmt.setString(2, programCode);
            pstmt.setInt(3, collegeId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Retrieves program information from the database based on program ID
     * @param id The ID of the program to retrieve
     * @return true if program found and data loaded, false otherwise
     */
    public static boolean getProgramById(int id) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT program_id, program_name, program_code, college_id FROM programs WHERE program_id = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            ResultSet resultSet = pstmt.executeQuery();
            
            if (resultSet.next()) {
                // Load the retrieved data into the static fields
                programId = resultSet.getInt("program_id");
                programName = resultSet.getString("program_name");
                programCode = resultSet.getString("program_code");
                collegeId = resultSet.getInt("college_id");
                
                return true; // Program found and data loaded
            } else {
                return false; // Program not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Updates existing program information in the database
     * @param id The ID of the program to update
     * @return true if update successful, false otherwise
     */
    public static boolean updateProgram(int id) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE programs SET program_name = ?, program_code = ?, college_id = ? WHERE program_id = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, programName);
            pstmt.setString(2, programCode);
            pstmt.setInt(3, collegeId);
            pstmt.setInt(4, id);
            
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
        return programName != null && !programName.isEmpty() &&
               programCode != null && !programCode.isEmpty() &&
               collegeId != null;
    }
    
    /**
     * Deletes a program from the database based on program ID
     * @param id The ID of the program to delete
     * @return true if deletion successful, false otherwise
     */
    public static boolean deleteProgram(int id) {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM programs WHERE program_id = ?";
            
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
        programName = null;
        programCode = null;
        programId = null;
        collegeId = null;
    }
    
    /**
     * Loads all programs into a map (id -> name)
     * @return Map of program IDs to names
     */
    public static Map<Integer, String> loadProgramMap() {
        Map<Integer, String> programMap = new HashMap<>();
        
        try (Connection conn = getConnection()) {
            String sql = "SELECT program_id, program_name FROM programs";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("program_id");
                String name = rs.getString("program_name");
                programMap.put(id, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return programMap;
    }
    
    /**
     * Checks if a program code already exists
     * @param code The code to check
     * @return true if code exists, false otherwise
     */
    public static boolean programCodeExists(String code) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT 1 FROM programs WHERE program_code = ?";
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
     * Checks if a program name already exists
     * @param name The name to check
     * @return true if name exists, false otherwise
     */
    public static boolean programNameExists(String name) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT 1 FROM programs WHERE program_name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Loads programs by college ID
     * @param collegeId The college ID to filter by
     * @return Map of program IDs to names
     */
    public static Map<Integer, String> loadProgramsByCollege(int collegeId) {
        Map<Integer, String> programMap = new HashMap<>();
        
        try (Connection conn = getConnection()) {
            String sql = "SELECT program_id, program_name FROM programs WHERE college_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, collegeId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("program_id");
                String name = rs.getString("program_name");
                programMap.put(id, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return programMap;
    }
}