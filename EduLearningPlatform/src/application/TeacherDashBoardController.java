package application;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TeacherDashBoardController implements Initializable {
    
    // Database connection
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    
    @FXML
    private AnchorPane Schedulesection;
    
    @FXML
    private AnchorPane studlistsection;
    
    @FXML
    private AnchorPane smtgradesection;
    
    @FXML
    private Button addgradebtn;

    @FXML
    private TextField byclass;

    @FXML
    private TextField byid;

    @FXML
    private Button byidbtn;

    @FXML
    private TableColumn<Student, String> classcolumn;

    @FXML
    private TableColumn<ScheduleData, String> coursecolumn;

    @FXML
    private TableColumn<ScheduleData, String> daycolumn;

    @FXML
    private TableColumn<Student, String> fnamecolumn;

    @FXML
    private TableColumn<GradeData, String> gradecolumn;

    @FXML
    private TableColumn<Student, String> idcolumn;

    @FXML
    private Button liststudbtn;

    @FXML
    private Button logoutbtn;

    @FXML
    private TableColumn<Student, String> namecolumn;

    @FXML
    private Button sbtgradebtn;

    @FXML
    private Button schedulebtn;

    @FXML
    private Button schedulebtn11;

    @FXML
    private TableColumn<Student, String> sectioncolumn;

    @FXML
    private TableColumn<Student, String> sexcolumn;

    @FXML
    private TableColumn<GradeData, String> studFnamecolumn;

    @FXML
    private TableColumn<GradeData, String> studnamecolumn;

    @FXML
    private TextField texstudid;

    @FXML
    private TableColumn<ScheduleData, String> timecolumn;

    @FXML
    private TextField txtcourscode;

    @FXML
    private TextField txtgrade;

    @FXML
    private Button updategrade;
    
    @FXML
    private Button addmaterial;
    
    @FXML
    private Label username;

    // TableViews (add these to your FXML)
    @FXML private TableView<Student> studentTable;
    @FXML private TableView<ScheduleData> scheduleTable;
    @FXML private TableView<GradeData> gradeTable;

    // Data lists
    private ObservableList<Student> studentList;
    private ObservableList<ScheduleData> scheduleList;
    private ObservableList<GradeData> gradeList;

    public void displayUsername() {
        String user = Data.username;
        if (user != null && !user.isEmpty()) {
            user = user.substring(0, 1).toUpperCase() + user.substring(1);
            username.setText(user);
        } else {
            username.setText("Teacher");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        displayUsername();
        initializeDatabase();
        setupTableColumns();
        
        // Load initial data
        displayAllStudents();
        displayTeacherSchedule();
        displayGrades();
    }

    private void initializeDatabase() {
        try {
            // Use the same connection method as AdminDashBoardController
            String url = "jdbc:mysql://localhost:3306/EduLearning";
            String user = "root";
            String password = ""; // Your password here
            
            connect = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected successfully in Teacher Dashboard!");
            
            // Create tables if they don't exist
            createTablesIfNotExist();
        } catch (Exception e) {
            showErrorAlert("Database Error", "Cannot connect to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createTablesIfNotExist() {
        // Ensure grades table exists with proper structure
        String createGradesTable = 
            "CREATE TABLE IF NOT EXISTS grades (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "student_id VARCHAR(50), " +
            "course_code VARCHAR(50), " +
            "grade VARCHAR(10), " +  // This will store numeric grades (0-100)
            "FOREIGN KEY (student_id) REFERENCES students(id), " +
            "UNIQUE KEY unique_grade (student_id, course_code))";
        
        // Create teacher_courses table if it doesn't exist
        String createTeacherCoursesTable = 
            "CREATE TABLE IF NOT EXISTS teacher_courses (" +
            "teacher_id VARCHAR(50), " +
            "course_code VARCHAR(50), " +
            "PRIMARY KEY (teacher_id, course_code))";
        
        try (Statement stmt = connect.createStatement()) {
            stmt.execute(createGradesTable);
            stmt.execute(createTeacherCoursesTable);
            System.out.println("Tables checked/created successfully");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    private void setupTableColumns() {
        // Student table columns - FIXED property names
        idcolumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        namecolumn.setCellValueFactory(new PropertyValueFactory<>("firstName")); // Changed from "name"
        fnamecolumn.setCellValueFactory(new PropertyValueFactory<>("lastName")); // Changed from "last_name"
        sexcolumn.setCellValueFactory(new PropertyValueFactory<>("gender")); // Changed from "sex"
        classcolumn.setCellValueFactory(new PropertyValueFactory<>("className")); // Changed from "studentClass"
        sectioncolumn.setCellValueFactory(new PropertyValueFactory<>("section"));

        // Schedule table columns - FIXED: ScheduleData uses getter methods
        coursecolumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        daycolumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        timecolumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Grade table columns - FIXED property names
        studnamecolumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        studFnamecolumn.setCellValueFactory(new PropertyValueFactory<>("studentLastName"));
        gradecolumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
    }

    // STUDENT MANAGEMENT
    @FXML
    void studentList(ActionEvent event) {
        Schedulesection.setVisible(false);
        smtgradesection.setVisible(false);
        studlistsection.setVisible(true);
        displayAllStudents();
    }

    @FXML
    void HandleSearchByClass(ActionEvent event) {
        String className = byclass.getText().trim();
        if (className.isEmpty()) {
            showErrorAlert("Input Error", "Please enter a class to search");
            return;
        }
        
        searchStudentsByClass(className);
    }

    @FXML
    void handleSearchById(ActionEvent event) {
        String studentId = byid.getText().trim();
        if (studentId.isEmpty()) {
            showErrorAlert("Input Error", "Please enter a student ID to search");
            return;
        }
        
        searchStudentById(studentId);
    }

    private void displayAllStudents() {
        studentList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM students";
        
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while (result.next()) {
                studentList.add(new Student(
                    result.getString("id"),
                    result.getString("name"),
                    result.getString("last_name"),
                    result.getString("gender"),
                    result.getString("class"),
                    result.getString("section")
                ));
            }
            
            studentTable.setItems(studentList);
            
            if (studentList.isEmpty()) {
                showInfoAlert("No Students", "No students found in the database");
            }
            
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to load students: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeDatabaseResources();
        }
    }

    private void searchStudentsByClass(String className) {
        studentList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM students WHERE class = ?";
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, className);
            result = prepare.executeQuery();
            
            while (result.next()) {
                studentList.add(new Student(
                    result.getString("id"),
                    result.getString("name"),
                    result.getString("last_name"),
                    result.getString("gender"),
                    result.getString("class"),
                    result.getString("section")
                ));
            }
            
            studentTable.setItems(studentList);
            
            if (studentList.isEmpty()) {
                showInfoAlert("No Results", "No students found in class: " + className);
            }
            
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to search students: " + e.getMessage());
        } finally {
            closeDatabaseResources();
        }
    }

    private void searchStudentById(String studentId) {
        studentList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM students WHERE id = ?";
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, studentId);
            result = prepare.executeQuery();
            
            if (result.next()) {
                studentList.add(new Student(
                    result.getString("id"),
                    result.getString("name"),
                    result.getString("last_name"),
                    result.getString("gender"),
                    result.getString("class"),
                    result.getString("section")
                ));
                studentTable.setItems(studentList);
            } else {
                showErrorAlert("Not Found", "Student with ID " + studentId + " not found");
                displayAllStudents(); // Show all students if not found
            }
            
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to search student: " + e.getMessage());
        } finally {
            closeDatabaseResources();
        }
    }

    // SCHEDULE MANAGEMENT
    @FXML
    void Schedule(ActionEvent event) {
        studlistsection.setVisible(false);
        smtgradesection.setVisible(false);
        Schedulesection.setVisible(true);
        displayTeacherSchedule();
    }

    private void displayTeacherSchedule() {
        scheduleList = FXCollections.observableArrayList();
        
        // First, try to get teacher's courses
        String teacherName = getTeacherName();
        if (teacherName == null) {
            teacherName = Data.username; // Fallback to username
        }
        
        // Query schedule for this teacher's courses
        // Assuming schedule has teacher_name column or we join with teacher_courses
        String sql = "SELECT s.course_name, s.day, s.time, s.class, s.section " +
                    "FROM schedule s " +
                    "WHERE s.course_name IN (SELECT course_code FROM teacher_courses WHERE teacher_id = ?) " +
                    "OR s.teacher_name = ? " +
                    "ORDER BY FIELD(s.day, 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'), s.time";
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, Data.username);
            prepare.setString(2, teacherName);
            result = prepare.executeQuery();
            
            while (result.next()) {
                // Use the factory method to create ScheduleData
                ScheduleData schedule = ScheduleData.createDailySchedule(
                    result.getString("course_name"),
                    result.getString("day"),
                    result.getString("time"),
                    result.getString("class"),
                    result.getString("section")
                );
                scheduleList.add(schedule);
            }
            
            scheduleTable.setItems(scheduleList);
            
            if (scheduleList.isEmpty()) {
                // Show sample schedule if no data found
                addSampleScheduleData();
                showInfoAlert("Schedule", "No schedule found for teacher. Showing sample data.");
            }
            
        } catch (SQLException e) {
            // If schedule table doesn't exist or has different structure, show sample data
            System.err.println("Error loading schedule: " + e.getMessage());
            addSampleScheduleData();
            showInfoAlert("Schedule", "Using sample schedule data.");
        } finally {
            closeDatabaseResources();
        }
    }

    private void addSampleScheduleData() {
        scheduleList = FXCollections.observableArrayList();
        
        // Add sample schedule data
        scheduleList.add(ScheduleData.createDailySchedule(
            "Mathematics", "Monday", "09:00-10:00", "10A", "A"
        ));
        scheduleList.add(ScheduleData.createDailySchedule(
            "Physics", "Monday", "11:00-12:00", "10B", "B"
        ));
        scheduleList.add(ScheduleData.createDailySchedule(
            "Chemistry", "Tuesday", "10:00-11:00", "11A", "A"
        ));
        scheduleList.add(ScheduleData.createDailySchedule(
            "Biology", "Wednesday", "14:00-15:00", "11B", "B"
        ));
        
        scheduleTable.setItems(scheduleList);
    }

    private String getTeacherName() {
        String sql = "SELECT name FROM teachers WHERE id = ? OR email = ?";
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, Data.username);
            prepare.setString(2, Data.username + "@school.com"); // Assuming email format
            result = prepare.executeQuery();
            
            if (result.next()) {
                return result.getString("name");
            }
        } catch (SQLException e) {
            System.err.println("Error getting teacher name: " + e.getMessage());
        }
        
        return null;
    }

    // GRADE MANAGEMENT
    @FXML
    void gradeSubmit(ActionEvent event) {
        Schedulesection.setVisible(false);
        studlistsection.setVisible(false);
        smtgradesection.setVisible(true);
        displayGrades();
    }

    @FXML
    void handleAddGrade(ActionEvent event) {
        if (validateGradeFields()) {
            String studentId = texstudid.getText().trim();
            String courseCode = txtcourscode.getText().trim();
            String grade = txtgrade.getText().trim();
            
            // Validate that grade is a number 0-100
            if (!isValidNumericGrade(grade)) {
                showErrorAlert("Validation Error", "Please enter a valid numeric grade (0-100)");
                return;
            }
            
            // Check if student exists
            if (!studentExists(studentId)) {
                showErrorAlert("Student Not Found", "Student with ID " + studentId + " does not exist");
                return;
            }
            
            // Check if course exists
            if (!courseExists(courseCode)) {
                showErrorAlert("Course Not Found", "Course with code " + courseCode + " does not exist");
                return;
            }
            
            // Insert or update grade with numeric value (0-100)
            String sql = "INSERT INTO grades (student_id, course_code, grade) VALUES (?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE grade = VALUES(grade)";
            
            try {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, studentId);
                prepare.setString(2, courseCode);
                prepare.setString(3, grade); // Store numeric grade (0-100)
                
                int rowsAffected = prepare.executeUpdate();
                
                if (rowsAffected > 0) {
                    showSuccessMessage("Grade submitted successfully! (Grade: " + grade + ")");
                    clearGradeFields();
                    displayGrades();
                } else {
                    showErrorAlert("Submission Failed", "Failed to submit grade");
                }
                
            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate entry")) {
                    showErrorAlert("Duplicate Grade", "Grade already exists for this student and course. Use update instead.");
                } else {
                    showErrorAlert("Database Error", "Failed to submit grade: " + e.getMessage());
                }
            } finally {
                closeDatabaseResources();
            }
        }
    }

    @FXML
    void handleUpdateGrade(ActionEvent event) {
        GradeData selectedGrade = gradeTable.getSelectionModel().getSelectedItem();
        if (selectedGrade == null) {
            showErrorAlert("No Selection", "Please select a grade record to update");
            return;
        }
        
        // Populate fields with selected grade data
        texstudid.setText(selectedGrade.getStudentId());
        txtcourscode.setText(selectedGrade.getCourseCode());
        
        if (validateGradeFieldsForUpdate()) {
            String studentId = texstudid.getText().trim();
            String courseCode = txtcourscode.getText().trim();
            String grade = txtgrade.getText().trim();
            
            // Validate that grade is a number 0-100
            if (!isValidNumericGrade(grade)) {
                showErrorAlert("Validation Error", "Please enter a valid numeric grade (0-100)");
                return;
            }
            
            String sql = "UPDATE grades SET grade = ? WHERE student_id = ? AND course_code = ?";
            
            try {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, grade); // Store numeric grade (0-100)
                prepare.setString(2, studentId);
                prepare.setString(3, courseCode);
                
                int affectedRows = prepare.executeUpdate();
                
                if (affectedRows > 0) {
                    showSuccessMessage("Grade updated successfully! (Grade: " + grade + ")");
                    clearGradeFields();
                    displayGrades();
                } else {
                    showErrorAlert("Update Failed", "No grade record found to update");
                }
                
            } catch (SQLException e) {
                showErrorAlert("Database Error", "Failed to update grade: " + e.getMessage());
            } finally {
                closeDatabaseResources();
            }
        }
    }

    private void displayGrades() {
        gradeList = FXCollections.observableArrayList();
        
        // First try to get grades for teacher's courses
        String sql = "SELECT g.student_id, g.course_code, g.grade, s.name, s.last_name, c.name as course_name " +
                    "FROM grades g " +
                    "JOIN students s ON g.student_id = s.id " +
                    "LEFT JOIN courses c ON g.course_code = c.code " +
                    "WHERE g.course_code IN (SELECT course_code FROM teacher_courses WHERE teacher_id = ?) " +
                    "OR EXISTS (SELECT 1 FROM schedule sch WHERE sch.course_name = g.course_code AND sch.teacher_name = ?) " +
                    "ORDER BY g.student_id, g.course_code";
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, Data.username);
            prepare.setString(2, getTeacherName());
            result = prepare.executeQuery();
            
            while (result.next()) {
                GradeData gradeData = new GradeData(
                    result.getString("student_id"),
                    result.getString("name"),
                    result.getString("last_name"),
                    result.getString("course_code"),
                    result.getString("grade")
                );
                
                // Set course name if available
                String courseName = result.getString("course_name");
                if (courseName != null && !courseName.isEmpty()) {
                    gradeData.setCourseName(courseName);
                }
                
                gradeList.add(gradeData);
            }
            
            gradeTable.setItems(gradeList);
            
            // If no grades found for teacher's courses, show all grades
            if (gradeList.isEmpty()) {
                showInfoAlert("Grades", "No grades found for your courses. Showing all grades.");
                displayAllGrades();
            }
            
        } catch (SQLException e) {
            // If teacher_courses table doesn't exist, show all grades
            System.err.println("Error loading teacher's grades: " + e.getMessage());
            displayAllGrades();
        } finally {
            closeDatabaseResources();
        }
    }

    private void displayAllGrades() {
        gradeList = FXCollections.observableArrayList();
        String sql = "SELECT g.student_id, g.course_code, g.grade, s.name, s.last_name, c.name as course_name " +
                    "FROM grades g " +
                    "JOIN students s ON g.student_id = s.id " +
                    "LEFT JOIN courses c ON g.course_code = c.code " +
                    "ORDER BY g.student_id, g.course_code";
        
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while (result.next()) {
                GradeData gradeData = new GradeData(
                    result.getString("student_id"),
                    result.getString("name"),
                    result.getString("last_name"),
                    result.getString("course_code"),
                    result.getString("grade")
                );
                
                // Set course name if available
                String courseName = result.getString("course_name");
                if (courseName != null && !courseName.isEmpty()) {
                    gradeData.setCourseName(courseName);
                }
                
                gradeList.add(gradeData);
            }
            
            gradeTable.setItems(gradeList);
            
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to load grades: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeDatabaseResources();
        }
    }
   

    // MATERIAL MANAGEMENT - FIXED FXML path
    @FXML
    void handleAdMaterial(ActionEvent event) {
        try {
            // Changed from "/application/admin_panel.fxml" to the correct path
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_panel.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("PDF Management - Add Teaching Materials");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root, 800, 600));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Cannot open PDF management panel: " + e.getMessage());
        }
    }

    // VALIDATION METHODS FOR NUMERIC GRADES ONLY (0-100)
    private boolean validateGradeFields() {
        if (texstudid.getText().isEmpty()) {
            showErrorAlert("Validation Error", "Please enter Student ID");
            return false;
        }
        
        if (txtcourscode.getText().isEmpty()) {
            showErrorAlert("Validation Error", "Please enter Course Code");
            return false;
        }
        
        if (txtgrade.getText().isEmpty()) {
            showErrorAlert("Validation Error", "Please enter Grade");
            return false;
        }
        
        // Validate grade is numeric (0-100)
        String grade = txtgrade.getText().trim();
        if (!isValidNumericGrade(grade)) {
            showErrorAlert("Validation Error", "Please enter a valid numeric grade (0-100)");
            return false;
        }
        
        return true;
    }

    private boolean validateGradeFieldsForUpdate() {
        if (txtgrade.getText().isEmpty()) {
            showErrorAlert("Validation Error", "Please enter Grade");
            return false;
        }
        
        String grade = txtgrade.getText().trim();
        if (!isValidNumericGrade(grade)) {
            showErrorAlert("Validation Error", "Please enter a valid numeric grade (0-100)");
            return false;
        }
        
        return true;
    }

    // VALIDATION: ONLY ACCEPT NUMERIC GRADES 0-100
    private boolean isValidNumericGrade(String grade) {
        // Check if it's a valid number
        if (grade == null || grade.trim().isEmpty()) {
            return false;
        }
        
        try {
            // Parse the grade as an integer
            int numericGrade = Integer.parseInt(grade.trim());
            
            // Check if it's in the valid range (0-100)
            return numericGrade >= 0 && numericGrade <= 100;
        } catch (NumberFormatException e) {
            // Not a valid number
            return false;
        }
    }

    // SIMPLIFIED GRADE VALIDATION METHOD (keeping for compatibility)
    private boolean isValidGrade(String grade) {
        return isValidNumericGrade(grade);
    }

    private boolean studentExists(String studentId) {
        String sql = "SELECT id FROM students WHERE id = ?";
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, studentId);
            result = prepare.executeQuery();
            boolean exists = result.next();
            closeDatabaseResources();
            return exists;
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean courseExists(String courseCode) {
        String sql = "SELECT code FROM courses WHERE code = ?";
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, courseCode);
            result = prepare.executeQuery();
            boolean exists = result.next();
            closeDatabaseResources();
            return exists;
        } catch (SQLException e) {
            return false;
        }
    }

    // UTILITY METHODS
    private void clearGradeFields() {
        texstudid.clear();
        txtcourscode.clear();
        txtgrade.clear();
    }

    private void closeDatabaseResources() {
        try {
            if (result != null) {
                result.close();
                result = null;
            }
            if (prepare != null) {
                prepare.close();
                prepare = null;
            }
        } catch (SQLException e) {
            System.err.println("Error closing database resources: " + e.getMessage());
        }
    }

    @FXML
    void signOut(ActionEvent event) {
        try {
            if (confirmSignOut()) {
                closeDatabaseResources();
                try {
                    if (connect != null && !connect.isClosed()) {
                        connect.close();
                    }
                } catch (SQLException e) {
                    System.err.println("Error closing database connection: " + e.getMessage());
                }
                
                Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                stage.centerOnScreen();
            }
        } catch (IOException e) {
            showErrorAlert("Error", "Cannot load login screen: " + e.getMessage());
        }
    }

    private boolean confirmSignOut() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        alert.setHeaderText("Confirm Sign Out");
        alert.setContentText("Are you sure you want to sign out?");
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        showErrorAlert("Error", message);
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}