public class Student {
    private String firstName;
    private String lastName;
    private String gender;
    private String idNumber;
    private String yearLevel;
    private String programCode;

    public Student(String firstName, String lastName, String gender, 
                  String idNumber, String yearLevel, String programCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.idNumber = idNumber;
        this.yearLevel = yearLevel;
        this.programCode = programCode;
    }

    // Getters and setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }
    
    public String getYearLevel() { return yearLevel; }
    public void setYearLevel(String yearLevel) { this.yearLevel = yearLevel; }
    
    public String getProgramCode() { return programCode; }
    public void setProgramCode(String programCode) { this.programCode = programCode; }
}