import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentDataManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentVault";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    
    private static String firstName;
    private static String lastName;
    private static String gender;
    private static String idNumber;
    private static String yearLevel;
    private static String programCode;
    
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
    
    public static void setProgramCode(String value) { programCode = value; }
    public static String getProgramCode() { return programCode; }
 
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    public static boolean saveStudent() {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO students (first_name, last_name, gender, id_number, year_level, program_code) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, gender);
            pstmt.setString(4, idNumber);
            pstmt.setString(5, yearLevel);
            pstmt.setString(6, programCode);
            
            int rowsAffected = pstmt.executeUpdate();
            
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean getStudentById(String studentId) {
        try (Connection conn = getConnection()) {
            String sql = "SELECT first_name, last_name, gender, id_number, year_level, program_code " +
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
                programCode = resultSet.getString("program_code");
                
                return true; // Student found and data loaded
            } else {
                return false; // Student not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateStudent(String studentId) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE students SET first_name = ?, last_name = ?, gender = ?, " +
                         "year_level = ?, program_code = ? WHERE id_number = ?";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, gender);
            pstmt.setString(4, yearLevel);
            pstmt.setString(5, programCode);
            pstmt.setString(6, studentId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean validateFields() {
        return firstName != null && !firstName.isEmpty() &&
               lastName != null && !lastName.isEmpty() &&
               gender != null && !gender.isEmpty() &&
               idNumber != null && !idNumber.isEmpty() &&
               yearLevel != null && !yearLevel.isEmpty() &&
               programCode != null && !programCode.isEmpty();
    }

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

    public static void clearFormData() {
        firstName = null;
        lastName = null;
        gender = null;
        idNumber = null;
        yearLevel = null;
        programCode = null;
    }
}