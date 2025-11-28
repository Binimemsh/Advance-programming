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
import javafx.stage.Stage;

public class AdminDashBoardController {
    
    // Database connection
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    
    @FXML
    private TableColumn<CourseData, String> Coursenamecolumn;

    @FXML
    private Button addcoursebtn;

    @FXML
    private AnchorPane addcoursesection;

    @FXML
    private Button addschedule;

    @FXML
    private Button addstudent;

    @FXML
    private Button addstudentbtn;

    @FXML
    private AnchorPane addstudentsection;

    @FXML
    private Button addteacher;

    @FXML
    private Button addteacherbtn;

    @FXML
    private AnchorPane addteachersection;

    @FXML
    private TableColumn<CourseData, String> columncoursecode;

    @FXML
    private TableColumn<CourseData, String> columncoursename;

    @FXML
    private TableColumn<StudentData, String> columnstudLname;

    @FXML
    private TableColumn<StudentData, String> columnstudclass;

    @FXML
    private TableColumn<StudentData, String> columnstudid;

    @FXML
    private TableColumn<StudentData, String> columnstudname;

    @FXML
    private TableColumn<StudentData, String> columnstudsection;

    @FXML
    private TableColumn<StudentData, String> columnstudsex;

    @FXML
    private TableColumn<TeacherData, String> columntecherLname;

    @FXML
    private TableColumn<TeacherData, String> columntecheremail;

    @FXML
    private TableColumn<TeacherData, String> columntecherfield;

    @FXML
    private TableColumn<TeacherData, String> columntecherid;

    @FXML
    private TableColumn<TeacherData, String> columntechername;

    @FXML
    private TextField coursecode;

    @FXML
    private TextField coursename;

    @FXML
    private AnchorPane createScdsection;

    @FXML
    private Button creatschedulebtn;

    @FXML
    private TableColumn<ScheduleData, String> daycolumn;

    @FXML
    private Button deleteschedule;

    @FXML
    private Button deletestudentbtn;

    @FXML
    private Button deleteteacher;

    @FXML
    private Button deletteacher;

    @FXML
    private Button displayschedule;

    @FXML
    private Button displaystud;

    @FXML
    private Button displayteacher;

    @FXML
    private Button logoutbtn;

    @FXML
    private TableColumn<ScheduleData, String> sclasscolumn;

    @FXML
    private TableColumn<ScheduleData, String> ssectioncloumn;

    @FXML
    private TableColumn<ScheduleData, String> timecolumn;

    @FXML
    private TextField txtCouresnamee;

    @FXML
    private TextField txtstudLname;

    @FXML
    private TextField txtstudclass;

    @FXML
    private TextField txtstudid;

    @FXML
    private TextField txtstudname;

    @FXML
    private TextField txtstudsection;

    @FXML
    private ComboBox<String> txtstudsex;

    @FXML
    private TextField txtteacherLname;

    @FXML
    private TextField txtteacheremail;

    @FXML
    private TextField txtteacherfield;

    @FXML
    private TextField txtteacherid;

    @FXML
    private TextField txtteachername;

    @FXML
    private Button updateschedule;

    @FXML
    private Button updatestudbtn;

    @FXML
    private Button updateteacher;

    @FXML
    private Label username;

    // TableViews
    @FXML private TableView<StudentData> studentTable;
    @FXML private TableView<TeacherData> teacherTable;
    @FXML private TableView<CourseData> courseTable;
    @FXML private TableView<ScheduleData> scheduleTable;

    // Data lists
    private ObservableList<StudentData> studentList;
    private ObservableList<TeacherData> teacherList;
    private ObservableList<CourseData> courseList;
    private ObservableList<ScheduleData> scheduleList;

    public void displayUsername() {
        String user = Data.username;
        user = user.substring(0, 1).toUpperCase() + user.substring(1);
        username.setText(user);
    }

    @FXML
    public void initialize(URL location, ResourceBundle resource) {
        displayUsername();
        loadGenderComboBox();
        
        // Initialize database in background to avoid blocking UI
        initializeDatabaseAsync();
        
        // Setup table columns (they can work without data)
        setupTableColumns();
        
        // Load sample data initially, will be replaced when DB connects
        loadSampleData();
    }

    private void initializeDatabaseAsync() {
        new Thread(() -> {
            try {
                Thread.sleep(500); // Small delay to ensure UI is loaded
                initializeDatabase();
                
                // Once DB is connected, load real data
                javafx.application.Platform.runLater(() -> {
                    if (connect != null) {
                        displayStudentData();
                        displayTeacherData();
                        displayCourseData();
                        displayScheduleData();
                        showSuccessMessage("Database connected successfully!");
                    } else {
                        showInfoAlert("Demo Mode", "Running with sample data. Database features will be available when connected.");
                    }
                });
            } catch (Exception e) {
                javafx.application.Platform.runLater(() -> {
                    showInfoAlert("Demo Mode", "Running with sample data. Database features will be available when connected.");
                });
            }
        }).start();
    }

    private void initializeDatabase() {
        try {
            connect = DatabaseConnection.connectDb();
            if (connect != null && !connect.isClosed()) {
                System.out.println("Admin Dashboard: Database connected successfully!");
                createTablesIfNotExist();
            } else {
                System.out.println("Admin Dashboard: Database connection is null or closed");
            }
        } catch (Exception e) {
            System.err.println("Admin Dashboard: Database initialization failed: " + e.getMessage());
            connect = null;
        }
    }

    private void createTablesIfNotExist() {
        if (connect == null) {
            System.out.println("Admin Dashboard: No database connection for table creation");
            return;
        }
        
        String[] createTables = {
            "CREATE TABLE IF NOT EXISTS students (" +
            "id VARCHAR(50) PRIMARY KEY, " +
            "name VARCHAR(100), " +
            "last_name VARCHAR(100), " +
            "gender VARCHAR(10), " +
            "class VARCHAR(50), " +
            "section VARCHAR(50))",
            
            "CREATE TABLE IF NOT EXISTS teachers (" +
            "id VARCHAR(50) PRIMARY KEY, " +
            "name VARCHAR(100), " +
            "last_name VARCHAR(100), " +
            "email VARCHAR(100), " +
            "field VARCHAR(100))",
            
            "CREATE TABLE IF NOT EXISTS courses (" +
            "code VARCHAR(50) PRIMARY KEY, " +
            "name VARCHAR(100))",
            
            "CREATE TABLE IF NOT EXISTS schedule (" +
            "id INT AUTO_INCREMENT PRIMARY KEY, " +
            "course_name VARCHAR(100), " +
            "day VARCHAR(20), " +
            "time VARCHAR(20), " +
            "class VARCHAR(50), " +
            "section VARCHAR(50))"
        };
        
        try (Statement stmt = connect.createStatement()) {
            for (String sql : createTables) {
                stmt.execute(sql);
            }
            System.out.println("Admin Dashboard: Tables created/verified successfully!");
        } catch (SQLException e) {
            System.err.println("Admin Dashboard: Error creating tables: " + e.getMessage());
        }
    }

    private void setupTableColumns() {
        // Student table columns
        if (columnstudid != null) columnstudid.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (columnstudname != null) columnstudname.setCellValueFactory(new PropertyValueFactory<>("name"));
        if (columnstudLname != null) columnstudLname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        if (columnstudsex != null) columnstudsex.setCellValueFactory(new PropertyValueFactory<>("gender"));
        if (columnstudclass != null) columnstudclass.setCellValueFactory(new PropertyValueFactory<>("studentClass"));
        if (columnstudsection != null) columnstudsection.setCellValueFactory(new PropertyValueFactory<>("section"));

        // Teacher table columns
        if (columntecherid != null) columntecherid.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (columntechername != null) columntechername.setCellValueFactory(new PropertyValueFactory<>("name"));
        if (columntecherLname != null) columntecherLname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        if (columntecheremail != null) columntecheremail.setCellValueFactory(new PropertyValueFactory<>("email"));
        if (columntecherfield != null) columntecherfield.setCellValueFactory(new PropertyValueFactory<>("field"));

        // Course table columns
        if (columncoursecode != null) columncoursecode.setCellValueFactory(new PropertyValueFactory<>("code"));
        if (columncoursename != null) columncoursename.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Schedule table columns
        if (Coursenamecolumn != null) Coursenamecolumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        if (daycolumn != null) daycolumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        if (timecolumn != null) timecolumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        if (sclasscolumn != null) sclasscolumn.setCellValueFactory(new PropertyValueFactory<>("studentClass"));
        if (ssectioncloumn != null) ssectioncloumn.setCellValueFactory(new PropertyValueFactory<>("section"));
    }

    private void loadGenderComboBox() {
        if (txtstudsex != null) {
            txtstudsex.getItems().addAll("Male", "Female", "Other");
        }
    }

    private void loadSampleData() {
        // Load sample students
        studentList = FXCollections.observableArrayList();
        studentList.add(new StudentData("S001", "Alice", "Williams", "Female", "10", "A"));
        studentList.add(new StudentData("S002", "Bob", "Miller", "Male", "10", "A"));
        studentList.add(new StudentData("S003", "Carol", "Davis", "Female", "10", "B"));
        if (studentTable != null) studentTable.setItems(studentList);

        // Load sample teachers
        teacherList = FXCollections.observableArrayList();
        teacherList.add(new TeacherData("T001", "John", "Smith", "john.smith@school.com", "Mathematics"));
        teacherList.add(new TeacherData("T002", "Sarah", "Johnson", "sarah.johnson@school.com", "Physics"));
        if (teacherTable != null) teacherTable.setItems(teacherList);

        // Load sample courses
        courseList = FXCollections.observableArrayList();
        courseList.add(new CourseData("MATH101", "Mathematics"));
        courseList.add(new CourseData("PHYS101", "Physics"));
        courseList.add(new CourseData("CHEM101", "Chemistry"));
        if (courseTable != null) courseTable.setItems(courseList);

        // Load sample schedule
        scheduleList = FXCollections.observableArrayList();
        scheduleList.add(new ScheduleData("Mathematics", "Monday", "08:00-09:00", "10", "A"));
        scheduleList.add(new ScheduleData("Physics", "Tuesday", "10:00-11:00", "10", "A"));
        if (scheduleTable != null) scheduleTable.setItems(scheduleList);
    }

    // STUDENT MANAGEMENT
    @FXML
    void handleAddStudent(ActionEvent event) {
        if (!isDatabaseConnected()) return;
        
        if (validateStudentFields()) {
            String sql = "INSERT INTO students (id, name, last_name, gender, class, section) VALUES (?, ?, ?, ?, ?, ?)";
            
            try {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, txtstudid.getText());
                prepare.setString(2, txtstudname.getText());
                prepare.setString(3, txtstudLname.getText());
                prepare.setString(4, txtstudsex.getValue());
                prepare.setString(5, txtstudclass.getText());
                prepare.setString(6, txtstudsection.getText());
                
                prepare.executeUpdate();
                showSuccessMessage("Student added successfully!");
                clearStudentFields();
                displayStudentData();
                
            } catch (SQLException e) {
                showErrorAlert("Database Error", "Failed to add student: " + e.getMessage());
            }
        }
    }

    @FXML
    void handleupdateStudent(ActionEvent event) {
        if (!isDatabaseConnected()) return;
        
        if (studentTable.getSelectionModel().getSelectedItem() == null) {
            showErrorAlert("No Selection", "Please select a student to update");
            return;
        }
        
        String sql = "UPDATE students SET name=?, last_name=?, gender=?, class=?, section=? WHERE id=?";
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, txtstudname.getText());
            prepare.setString(2, txtstudLname.getText());
            prepare.setString(3, txtstudsex.getValue());
            prepare.setString(4, txtstudclass.getText());
            prepare.setString(5, txtstudsection.getText());
            prepare.setString(6, txtstudid.getText());
            
            prepare.executeUpdate();
            showSuccessMessage("Student updated successfully!");
            displayStudentData();
            
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to update student: " + e.getMessage());
        }
    }

    @FXML
    void handledeleteStudent(ActionEvent event) {
        if (!isDatabaseConnected()) return;
        
        if (studentTable.getSelectionModel().getSelectedItem() == null) {
            showErrorAlert("No Selection", "Please select a student to delete");
            return;
        }
        
        if (confirmAction("Delete Student", "Are you sure you want to delete this student?")) {
            String sql = "DELETE FROM students WHERE id=?";
            
            try {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, txtstudid.getText());
                prepare.executeUpdate();
                
                showSuccessMessage("Student deleted successfully!");
                clearStudentFields();
                displayStudentData();
                
            } catch (SQLException e) {
                showErrorAlert("Database Error", "Failed to delete student: " + e.getMessage());
            }
        }
    }

    @FXML
    void handleDisplayStudent(ActionEvent event) {
        displayStudentData();
    }

    // TEACHER MANAGEMENT
    @FXML
    void handleAddTeacher(ActionEvent event) {
        if (!isDatabaseConnected()) return;
        
        if (validateTeacherFields()) {
            String sql = "INSERT INTO teachers (id, name, last_name, email, field) VALUES (?, ?, ?, ?, ?)";
            
            try {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, txtteacherid.getText());
                prepare.setString(2, txtteachername.getText());
                prepare.setString(3, txtteacherLname.getText());
                prepare.setString(4, txtteacheremail.getText());
                prepare.setString(5, txtteacherfield.getText());
                
                prepare.executeUpdate();
                showSuccessMessage("Teacher added successfully!");
                clearTeacherFields();
                displayTeacherData();
                
            } catch (SQLException e) {
                showErrorAlert("Database Error", "Failed to add teacher: " + e.getMessage());
            }
        }
    }

    @FXML
    void handleUpdateTeacher(ActionEvent event) {
        if (!isDatabaseConnected()) return;
        
        if (teacherTable.getSelectionModel().getSelectedItem() == null) {
            showErrorAlert("No Selection", "Please select a teacher to update");
            return;
        }
        
        String sql = "UPDATE teachers SET name=?, last_name=?, email=?, field=? WHERE id=?";
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, txtteachername.getText());
            prepare.setString(2, txtteacherLname.getText());
            prepare.setString(3, txtteacheremail.getText());
            prepare.setString(4, txtteacherfield.getText());
            prepare.setString(5, txtteacherid.getText());
            
            prepare.executeUpdate();
            showSuccessMessage("Teacher updated successfully!");
            displayTeacherData();
            
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to update teacher: " + e.getMessage());
        }
    }

    @FXML
    void handleDeleteTeacher(ActionEvent event) {
        if (!isDatabaseConnected()) return;
        
        if (teacherTable.getSelectionModel().getSelectedItem() == null) {
            showErrorAlert("No Selection", "Please select a teacher to delete");
            return;
        }
        
        if (confirmAction("Delete Teacher", "Are you sure you want to delete this teacher?")) {
            String sql = "DELETE FROM teachers WHERE id=?";
            
            try {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, txtteacherid.getText());
                prepare.executeUpdate();
                
                showSuccessMessage("Teacher deleted successfully!");
                clearTeacherFields();
                displayTeacherData();
                
            } catch (SQLException e) {
                showErrorAlert("Database Error", "Failed to delete teacher: " + e.getMessage());
            }
        }
    }

    @FXML
    void handleDisplayTeacher(ActionEvent event) {
        displayTeacherData();
    }

    // COURSE MANAGEMENT
    @FXML
    void handleAddCourse(ActionEvent event) {
        if (!isDatabaseConnected()) return;
        
        if (validateCourseFields()) {
            String sql = "INSERT INTO courses (code, name) VALUES (?, ?)";
            
            try {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, coursecode.getText());
                prepare.setString(2, coursename.getText());
                
                prepare.executeUpdate();
                showSuccessMessage("Course added successfully!");
                clearCourseFields();
                displayCourseData();
                
            } catch (SQLException e) {
                showErrorAlert("Database Error", "Failed to add course: " + e.getMessage());
            }
        }
    }

    @FXML
    void handleUpdateCourse(ActionEvent event) {
        showInfoAlert("Update Course", "Update course functionality to be implemented.");
    }

    @FXML
    void handleDeleteCourse(ActionEvent event) {
        showInfoAlert("Delete Course", "Delete course functionality to be implemented.");
    }

    @FXML
    void handleDisplayCourse(ActionEvent event) {
        displayCourseData();
    }

    // SCHEDULE MANAGEMENT
    @FXML
    void handleAddSchedule(ActionEvent event) {
        showInfoAlert("Add Schedule", "Add schedule functionality to be implemented.");
    }

    @FXML
    void handleUpdateSchedule(ActionEvent event) {
        showInfoAlert("Update Schedule", "Update schedule functionality to be implemented.");
    }

    @FXML
    void handleDeleteSchedule(ActionEvent event) {
        showInfoAlert("Delete Schedule", "Delete schedule functionality to be implemented.");
    }

    @FXML
    void handleDisplaySchedule(ActionEvent event) {
        displayScheduleData();
    }

    // DATA DISPLAY METHODS
    private void displayStudentData() {
        if (!isDatabaseConnected()) {
            showInfoAlert("Demo Mode", "Showing sample data. Connect to database for real data.");
            return;
        }
        
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
            
            if (studentTable != null) {
                studentTable.setItems(studentList);
            }
            
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to load students: " + e.getMessage());
        }
    }

    private void displayTeacherData() {
        if (!isDatabaseConnected()) {
            showInfoAlert("Demo Mode", "Showing sample data. Connect to database for real data.");
            return;
        }
        
        teacherList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM teachers";
        
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while (result.next()) {
                teacherList.add(new TeacherData(
                    result.getString("id"),
                    result.getString("name"),
                    result.getString("last_name"),
                    result.getString("email"),
                    result.getString("field")
                ));
            }
            
            if (teacherTable != null) {
                teacherTable.setItems(teacherList);
            }
            
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to load teachers: " + e.getMessage());
        }
    }

    private void displayCourseData() {
        if (!isDatabaseConnected()) {
            showInfoAlert("Demo Mode", "Showing sample data. Connect to database for real data.");
            return;
        }
        
        courseList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM courses";
        
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while (result.next()) {
                courseList.add(new CourseData(
                    result.getString("code"),
                    result.getString("name")
                ));
            }
            
            if (courseTable != null) {
                courseTable.setItems(courseList);
            }
            
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to load courses: " + e.getMessage());
        }
    }

    private void displayScheduleData() {
        if (!isDatabaseConnected()) {
            showInfoAlert("Demo Mode", "Showing sample data. Connect to database for real data.");
            return;
        }
        
        scheduleList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM schedule";
        
        try {
            prepare = connect.prepareStatement(sql);
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
            
            if (scheduleTable != null) {
                scheduleTable.setItems(scheduleList);
            }
            
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to load schedule: " + e.getMessage());
        }
    }

    // VALIDATION METHODS
    private boolean validateStudentFields() {
        if (txtstudid.getText().isEmpty() || txtstudname.getText().isEmpty() || 
            txtstudLname.getText().isEmpty() || txtstudsex.getValue() == null ||
            txtstudclass.getText().isEmpty() || txtstudsection.getText().isEmpty()) {
            showErrorAlert("Validation Error", "Please fill all student fields");
            return false;
        }
        return true;
    }

    private boolean validateTeacherFields() {
        if (txtteacherid.getText().isEmpty() || txtteachername.getText().isEmpty() || 
            txtteacherLname.getText().isEmpty() || txtteacheremail.getText().isEmpty() ||
            txtteacherfield.getText().isEmpty()) {
            showErrorAlert("Validation Error", "Please fill all teacher fields");
            return false;
        }
        return true;
    }

    private boolean validateCourseFields() {
        if (coursecode.getText().isEmpty() || coursename.getText().isEmpty()) {
            showErrorAlert("Validation Error", "Please fill all course fields");
            return false;
        }
        return true;
    }

    // DATABASE CONNECTION CHECK
    private boolean isDatabaseConnected() {
        if (connect == null) {
            showErrorAlert("Database Not Connected", 
                "No database connection available. Please check:\n" +
                "1. MySQL server is running\n" +
                "2. Database 'edulearning' exists\n" +
                "3. Correct password in DatabaseConnection.java\n\n" +
                "Running in demo mode with sample data.");
            return false;
        }
        return true;
    }

    // CLEAR METHODS
    private void clearStudentFields() {
        if (txtstudid != null) txtstudid.clear();
        if (txtstudname != null) txtstudname.clear();
        if (txtstudLname != null) txtstudLname.clear();
        if (txtstudsex != null) txtstudsex.setValue(null);
        if (txtstudclass != null) txtstudclass.clear();
        if (txtstudsection != null) txtstudsection.clear();
    }

    private void clearTeacherFields() {
        if (txtteacherid != null) txtteacherid.clear();
        if (txtteachername != null) txtteachername.clear();
        if (txtteacherLname != null) txtteacherLname.clear();
        if (txtteacheremail != null) txtteacheremail.clear();
        if (txtteacherfield != null) txtteacherfield.clear();
    }

    private void clearCourseFields() {
        if (coursecode != null) coursecode.clear();
        if (coursename != null) coursename.clear();
    }

    // NAVIGATION METHODS
    @FXML
    void addCourse(ActionEvent event) {
        addteachersection.setVisible(false);
        addstudentsection.setVisible(false);
        createScdsection.setVisible(false);
        addcoursesection.setVisible(true);
    }

    @FXML
    void addStudent(ActionEvent event) {
        addcoursesection.setVisible(false);
        addteachersection.setVisible(false);
        createScdsection.setVisible(false);
        addstudentsection.setVisible(true);
    }

    @FXML
    void addTeacher(ActionEvent event) {
        addstudentsection.setVisible(false);
        createScdsection.setVisible(false);
        addcoursesection.setVisible(false);
        addteachersection.setVisible(true);
    }

    @FXML
    void creatSchedule(ActionEvent event) {
        addcoursesection.setVisible(false);
        addteachersection.setVisible(false);
        addstudentsection.setVisible(false);
        createScdsection.setVisible(true);
    }

    // UTILITY METHODS
    private boolean confirmAction(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private boolean confirmSignOut() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        alert.setHeaderText("Confirm Sign Out");
        alert.setContentText("Are you sure you want to sign out?");
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    @FXML
    private void signOut(ActionEvent event) {
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

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}