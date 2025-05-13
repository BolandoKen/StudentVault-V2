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
    private static String collegeCode;
    
    // Getters and setters
    public static void setProgramName(String value) { programName = value; }
    public static String getProgramName() { return programName; }
    
    public static void setProgramCode(String value) { programCode = value; }
    public static String getProgramCode() { return programCode; }
    
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
     * Save program data to the database
     * @return true if successful, false otherwise
     */
    public static boolean saveProgram() {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO programs (program_name, program_code, college_code) VALUES (?, ?, ?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, programName);
            pstmt.setString(2, programCode);
            pstmt.setString(3, collegeCode);
            
            int rowsAffected = pstmt.executeUpdate();
            
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Retrieves program information from the database based on program code
     * @param code The code of the program to retrieve
     * @return true if program found and data loaded, false otherwise
     */
    public static boolean getProgramByCode(String code) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT program_name, program_code, college_code FROM programs WHERE program_code = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, code);
            
            ResultSet resultSet = pstmt.executeQuery();
            
            if (resultSet.next()) {
                // Load the retrieved data into the static fields
                programName = resultSet.getString("program_name");
                programCode = resultSet.getString("program_code");
                collegeCode = resultSet.getString("college_code");
                
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
     * Retrieves the name of a program based on its code
     * @param programCode The code of the program
     * @return The name of the program, or null if not found
     */
    public static String getProgramName(String programCode) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT program_name FROM programs WHERE program_code = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, programCode);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("program_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
 * Updates existing program information in the database
 * @param oldProgramCode The current code of the program to update
 * @param newProgramName The new name for the program
 * @param newProgramCode The new code for the program
 * @return true if update successful, false otherwise
 */
    public static boolean updateProgram(String oldProgramCode, String newProgramName, String newProgramCode) {
        try (Connection conn = getConnection()) {
        String sql = "UPDATE programs SET program_name = ?, program_code = ? WHERE program_code = ?";
        
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, newProgramName);
        pstmt.setString(2, newProgramCode);
        pstmt.setString(3, oldProgramCode);
        
        int rowsAffected = pstmt.executeUpdate();
        
        return rowsAffected > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
    
/**
 * Adds a new program to the database with the specified details
 * @param programName The name of the program to add
 * @param programCode The code for the program to add
 * @param collegeCode The code of the college to which this program belongs
 * @return true if program was added successfully, false otherwise
 */
public static boolean addProgram(String programName, String programCode, String collegeCode) {
    // Validate inputs
    if (programName == null || programName.trim().isEmpty() ||
        programCode == null || programCode.trim().isEmpty() ||
        collegeCode == null || collegeCode.trim().isEmpty()) {
        return false;
    }
    
    // Check for duplicate program code
    if (programCodeExists(programCode)) {
        return false;
    }
    
    // Check for duplicate program name
    if (programNameExists(programName)) {
        return false;
    }
    
    try (Connection conn = getConnection()) {
        String sql = "INSERT INTO programs (program_name, program_code, college_code) VALUES (?, ?, ?)";
        
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, programName);
        pstmt.setString(2, programCode);
        pstmt.setString(3, collegeCode);
        
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
               collegeCode != null && !collegeCode.isEmpty();
    }
    
    /**
     * Deletes a program from the database based on program code
     * @param code The code of the program to delete
     * @return true if deletion successful, false otherwise
     */
    public static boolean deleteProgram(String code) {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM programs WHERE program_code = ?";
            
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
        programName = null;
        programCode = null;
        collegeCode = null;
    }
    
    /**
     * Loads all programs into a map (code -> name)
     * @return Map of program codes to names
     */
    public static Map<String, String> loadProgramMap() {
        Map<String, String> programMap = new HashMap<>();
        
        try (Connection conn = getConnection()) {
            String sql = "SELECT program_code, program_name FROM programs";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String code = rs.getString("program_code");
                String name = rs.getString("program_name");
                programMap.put(code, name);
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
     * Loads programs by college code
     * @param collegeCode The college code to filter by
     * @return Map of program codes to names
     */
    public static Map<String, String> loadProgramsByCollege(String collegeCode) {
        Map<String, String> programMap = new HashMap<>();
        
        try (Connection conn = getConnection()) {
            String sql = "SELECT program_code, program_name FROM programs WHERE college_code = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, collegeCode);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String code = rs.getString("program_code");
                String name = rs.getString("program_name");
                programMap.put(code, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return programMap;
    }
}