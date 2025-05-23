import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class CollegeDataManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentVault";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    
    private static String collegeName;
    private static String collegeCode;
    
    public static void setCollegeName(String value) { collegeName = value; }
    public static String getCollegeName() { return collegeName; }
    
    public static void setCollegeCode(String value) { collegeCode = value; }
    
    public static String getCollegeCode() { return collegeCode; }
  
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
  
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
    
    public static boolean addCollege(String name, String code) {
        // Validate input
        if (name == null || name.trim().isEmpty() || 
            code == null || code.trim().isEmpty()) {
            return false; // Invalid input
        }

        // Trim the input to remove any leading/trailing whitespace
        name = name.trim();
        code = code.trim();

        // Check if college code already exists
        if (collegeCodeExists(code)) {
            return false; // College code already exists
        }

        // Check if college name already exists
        if (collegeNameExists(name)) {
            return false; // College name already exists
        }

        // Attempt to save the college
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO colleges (college_name, college_code) VALUES (?, ?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, code);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Successfully added - also update static fields
                collegeName = name;
                collegeCode = code;
                return true; // Success
            } else {
                return false; // Database error
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Database error
        }
    }
    
    public static boolean updateCollege(String oldCode, String newName, String newCode) {
    Connection conn = null;
    try {
        conn = getConnection();
        // Start a transaction
        conn.setAutoCommit(false);
        
        // First, ensure the new college code exists
        // If it doesn't exist, insert a new college record
        String checkCollegeSql = "SELECT COUNT(*) FROM colleges WHERE college_code = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkCollegeSql);
        checkStmt.setString(1, newCode);
        ResultSet rs = checkStmt.executeQuery();
        
        boolean newCodeExists = false;
        if (rs.next()) {
            newCodeExists = rs.getInt(1) > 0;
        }
        
        // If new code doesn't exist, insert a new college record
        if (!newCodeExists) {
            String insertCollegeSql = "INSERT INTO colleges (college_code, college_name) VALUES (?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertCollegeSql);
            insertStmt.setString(1, newCode);
            insertStmt.setString(2, newName);
            insertStmt.executeUpdate();
        }
        
        // Update programs to use the new college code
        String updateProgramsSql = "UPDATE programs SET college_code = ? WHERE college_code = ?";
        PreparedStatement updateProgramsStmt = conn.prepareStatement(updateProgramsSql);
        updateProgramsStmt.setString(1, newCode);
        updateProgramsStmt.setString(2, oldCode);
        updateProgramsStmt.executeUpdate();
        
        // Update the college name for the new code
        String updateCollegeSql = "UPDATE colleges SET college_name = ? WHERE college_code = ?";
        PreparedStatement updateCollegeStmt = conn.prepareStatement(updateCollegeSql);
        updateCollegeStmt.setString(1, newName);
        updateCollegeStmt.setString(2, newCode);
        int collegeRowsAffected = updateCollegeStmt.executeUpdate();
        
        // If the old code is different from the new code, delete the old college record
        if (!oldCode.equals(newCode)) {
            String deleteOldCollegeSql = "DELETE FROM colleges WHERE college_code = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteOldCollegeSql);
            deleteStmt.setString(1, oldCode);
            deleteStmt.executeUpdate();
        }
        
        // Commit the transaction
        conn.commit();
        
        return collegeRowsAffected > 0;
    } catch (Exception e) {
        // Rollback in case of any error
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
        e.printStackTrace();
        return false;
    } finally {
        // Reset to default commit behavior
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
    
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
    
    public static boolean validateFields() {
        return collegeName != null && !collegeName.isEmpty() &&
               collegeCode != null && !collegeCode.isEmpty();
    }
    
    public static boolean deleteCollege(String code) {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false); // Begin transaction
    
            // Step 1: Update programs referencing this college
            String updateSql = "UPDATE programs SET college_code = 'N/A' WHERE college_code = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setString(1, code);
                updateStmt.executeUpdate();
            }
    
            // Step 2: Delete the college
            String deleteSql = "DELETE FROM colleges WHERE college_code = ?";
            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setString(1, code);
                int rowsAffected = deleteStmt.executeUpdate();
    
                conn.commit(); // Commit if all went well
                return rowsAffected > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                // Roll back in case of error
                if (!getConnection().getAutoCommit()) {
                    getConnection().rollback();
                }
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        }
    }
    
    
    public static void clearFormData() {
        collegeName = null;
        collegeCode = null;
    }
    
    public static Map<String, String> loadCollegeMap() {
        Map<String, String> collegeMap = new HashMap<>();
        
        try (Connection conn = getConnection()) {
            String sql = "SELECT college_code, college_name FROM colleges WHERE college_code <> 'N/A' ORDER BY college_code";
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