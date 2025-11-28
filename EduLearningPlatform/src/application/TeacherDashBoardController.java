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
import javafx.scene.control.ComboBox;
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
    private TableColumn<StudentData, String> classcolumn;

    @FXML
    private TableColumn<ScheduleData, String> coursecolumn;

    @FXML
    private TableColumn<ScheduleData, String> daycolumn;

    @FXML
    private TableColumn<StudentData, String> fnamecolumn;

    @FXML
    private TableColumn<GradeData, String> gradecolumn;

    @FXML
    private TableColumn<StudentData, String> idcolumn;

    @FXML
    private Button liststudbtn;

    @FXML
    private Button logoutbtn;

    @FXML
    private TableColumn<StudentData, String> namecolumn;

    @FXML
    private Button sbtgradebtn;

    @FXML
    private Button schedulebtn;

    @FXML
    private Button schedulebtn11;

    @FXML
    private TableColumn<StudentData, String> sectioncolumn;

    @FXML
    private TableColumn<StudentData, String> sexcolumn;

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
    @FXML private TableView<StudentData> studentTable;
    @FXML private TableView<ScheduleData> scheduleTable;
    @FXML private TableView<GradeData> gradeTable;

    // Data lists
    private ObservableList<StudentData> studentList;
    private ObservableList<ScheduleData> scheduleList;
    private ObservableList<GradeData> gradeList;

    public void displayUsername() {
        String user = Data.username;
        user = user.substring(0, 1).toUpperCase() + user.substring(1);
        username.setText(user);
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
            connect = DatabaseConnection.connectDb();
            createTablesIfNotExist();
        } catch (Exception e) {
            showErrorAlert("Database Error", "Cannot connect to database: " + e.getMessage());
        }
    }

    private void createTablesIfNotExist() {
        String[] createTables = {
            "CREATE TABLE IF NOT EXISTS grades (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "student_id VARCHAR(50), " +
            "course_code VARCHAR(50), " +
            "grade VARCHAR(10), " +
            "FOREIGN KEY (student_id) REFERENCES students(id))"
        };
        
        try (Statement stmt = connect.createStatement()) {
            for (String sql : createTables) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    private void setupTableColumns() {
        // Student table columns
        idcolumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        namecolumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        fnamecolumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        sexcolumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        classcolumn.setCellValueFactory(new PropertyValueFactory<>("studentClass"));
        sectioncolumn.setCellValueFactory(new PropertyValueFactory<>("section"));

        // Schedule table columns
        coursecolumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        daycolumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        timecolumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Grade table columns
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
                studentList.add(new StudentData(
                    result.getString("id"),
                    result.getString("name"),
                    result.getString("last_name"),
                    result.getString("gender"),
                    result.getString("class"),
                    result.getString("section")
                ));
            }
            
            studentTable.setItems(studentList);
            
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to load students: " + e.getMessage());
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
                studentList.add(new StudentData(
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
                studentList.add(new StudentData(
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
        
      
        String sql = "SELECT * FROM schedule WHERE teacher_id = ? OR teacher_name = ?";
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, Data.username); // Using username as teacher identifier
            prepare.setString(2, Data.username);
            result = prepare.executeQuery();
            
            while (result.next()) {
                scheduleList.add(new ScheduleData(
                    result.getString("course_name"),
                    result.getString("day"),
                    result.getString("time"),
                    result.getString("class"),
                    result.getString("section")
                ));
            }
            
            scheduleTable.setItems(scheduleList);
            
        } catch (SQLException e) {
            // If schedule table doesn't exist or has different structure, show sample data
            showInfoAlert("Error", "Something wrong");

        }
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
            
            // Check if student exists
            if (!studentExists(studentId)) {
                showErrorAlert("Student Not Found", "Student with ID " + studentId + " does not exist");
                return;
            }
            
            String sql = "INSERT INTO grades (student_id, course_code, grade) VALUES (?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE grade = ?";
            
            try {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, studentId);
                prepare.setString(2, courseCode);
                prepare.setString(3, grade);
                prepare.setString(4, grade);
                
                prepare.executeUpdate();
                showSuccessMessage("Grade submitted successfully!");
                clearGradeFields();
                displayGrades();
                
            } catch (SQLException e) {
                showErrorAlert("Database Error", "Failed to submit grade: " + e.getMessage());
            }
        }
    }

    @FXML
    void handleUpdateGrade(ActionEvent event) {
        if (gradeTable.getSelectionModel().getSelectedItem() == null) {
            showErrorAlert("No Selection", "Please select a grade record to update");
            return;
        }
        
        if (validateGradeFields()) {
            String studentId = texstudid.getText().trim();
            String courseCode = txtcourscode.getText().trim();
            String grade = txtgrade.getText().trim();
            
            String sql = "UPDATE grades SET grade = ? WHERE student_id = ? AND course_code = ?";
            
            try {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, grade);
                prepare.setString(2, studentId);
                prepare.setString(3, courseCode);
                
                int affectedRows = prepare.executeUpdate();
                
                if (affectedRows > 0) {
                    showSuccessMessage("Grade updated successfully!");
                    clearGradeFields();
                    displayGrades();
                } else {
                    showErrorAlert("Update Failed", "No grade record found to update");
                }
                
            } catch (SQLException e) {
                showErrorAlert("Database Error", "Failed to update grade: " + e.getMessage());
            }
        }
    }

    private void displayGrades() {
        gradeList = FXCollections.observableArrayList();
        
        // Join grades with students table to get student names
        String sql = "SELECT g.student_id, g.course_code, g.grade, s.name, s.last_name " +
                    "FROM grades g " +
                    "JOIN students s ON g.student_id = s.id " +
                    "WHERE g.course_code IN (SELECT course_code FROM teacher_courses WHERE teacher_id = ?)";
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, Data.username);
            result = prepare.executeQuery();
            
            while (result.next()) {
                gradeList.add(new GradeData(
                    result.getString("student_id"),
                    result.getString("name"),
                    result.getString("last_name"),
                    result.getString("course_code"),
                    result.getString("grade")
                ));
            }
            
            gradeTable.setItems(gradeList);
            
        } catch (SQLException e) {
            // If the query fails, try a simpler approach
            displayAllGrades();
        }
    }

    private void displayAllGrades() {
        gradeList = FXCollections.observableArrayList();
        String sql = "SELECT g.student_id, g.course_code, g.grade, s.name, s.last_name " +
                    "FROM grades g " +
                    "JOIN students s ON g.student_id = s.id";
        
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while (result.next()) {
                gradeList.add(new GradeData(
                    result.getString("student_id"),
                    result.getString("name"),
                    result.getString("last_name"),
                    result.getString("course_code"),
                    result.getString("grade")
                ));
            }
            
            gradeTable.setItems(gradeList);
            
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to load grades: " + e.getMessage());
        }
    }

    // MATERIAL MANAGEMENT
    @FXML
    void handleAdMaterial(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/admin_panel.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("PDF Management - Add Teaching Materials");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root, 600, 400));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Cannot open PDF management panel");
        }
    }

    // VALIDATION METHODS
    private boolean validateGradeFields() {
        if (texstudid.getText().isEmpty() || txtcourscode.getText().isEmpty() || txtgrade.getText().isEmpty()) {
            showErrorAlert("Validation Error", "Please fill all grade fields");
            return false;
        }
        
        // Validate grade format (you can customize this)
        String grade = txtgrade.getText().trim();
        if (!grade.matches("[A-F][+-]?|[0-9]{1,3}")) {
            showErrorAlert("Validation Error", "Please enter a valid grade (A-F, A+, B-, or 0-100)");
            return false;
        }
        
        return true;
    }

    private boolean studentExists(String studentId) {
        String sql = "SELECT id FROM students WHERE id = ?";
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, studentId);
            result = prepare.executeQuery();
            return result.next();
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

    @FXML
    void signOut(ActionEvent event) {
        try {
            if (confirmSignOut()) {
                Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                stage.centerOnScreen();
            } else {
                showErrorAlert("Sign Out Cancelled", "You are still signed in.");
            }
        } catch (IOException e) {
            showErrorAlert("Error", "Cannot load login screen.");
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