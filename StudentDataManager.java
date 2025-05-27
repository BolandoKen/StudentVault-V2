import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentDataManager {
    private static final Logger LOGGER = Logger.getLogger(StudentDataManager.class.getName());
    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentVault";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    
    // Private constructor to prevent instantiation (utility class)
    private StudentDataManager() {}
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found", e);
            throw new SQLException("Database driver not found", e);
        }
    }
    
    public static boolean saveStudent(Student student) {
        if (!validateStudent(student)) {
            return false;
        }
        
        String sql = "INSERT INTO students (first_name, last_name, gender, id, year_level, program_code) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setString(3, student.getGender());
            pstmt.setString(4, student.getIdNumber());
            pstmt.setString(5, student.getYearLevel());
            pstmt.setString(6, student.getProgramCode());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving student", e);
            return false;
        }
    }

    public static Student getStudentById(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return null;
        }
        
        String sql = "SELECT first_name, last_name, gender, id, year_level, program_code " +
                     "FROM students WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, studentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("gender"),
                        rs.getString("id"),
                        rs.getString("year_level"),
                        rs.getString("program_code")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving student with ID: " + studentId, e);
        }
        return null;
    }

    public static boolean updateStudent(Student student) {
        if (!validateStudent(student)) {
            return false;
        }
        
        String sql = "UPDATE students SET first_name = ?, last_name = ?, gender = ?, " +
                     "year_level = ?, program_code = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setString(3, student.getGender());
            pstmt.setString(4, student.getYearLevel());
            pstmt.setString(5, student.getProgramCode());
            pstmt.setString(6, student.getIdNumber());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating student", e);
            return false;
        }
    }
    
    /**
     * Updates a student record allowing the ID to be changed
     * @param originalId The original student ID to locate the record
     * @param student The updated student object with potentially new ID
     * @return true if update was successful, false otherwise
     */
    public static boolean updateStudentWithIdChange(String originalId, Student student) {
        if (!validateStudent(student) || originalId == null || originalId.trim().isEmpty()) {
            return false;
        }
        
        String sql = "UPDATE students SET first_name = ?, last_name = ?, gender = ?, " +
                     "id = ?, year_level = ?, program_code = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setString(3, student.getGender());
            pstmt.setString(4, student.getIdNumber()); // New ID
            pstmt.setString(5, student.getYearLevel());
            pstmt.setString(6, student.getProgramCode());
            pstmt.setString(7, originalId); // Original ID for WHERE clause
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating student with ID change", e);
            return false;
        }
    }
    
    public static boolean deleteStudent(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return false;
        }
        
        String sql = "DELETE FROM students WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, studentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting student with ID: " + studentId, e);
            return false;
        }
    }
    
    private static boolean validateStudent(Student student) {
        return student != null &&
               isNotBlank(student.getFirstName()) &&
               isNotBlank(student.getLastName()) &&
               isNotBlank(student.getGender()) &&
               isNotBlank(student.getIdNumber()) &&
               isNotBlank(student.getYearLevel()) &&
               isNotBlank(student.getProgramCode());
    }
    
    private static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
}