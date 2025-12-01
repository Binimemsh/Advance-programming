package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminDashBoardController implements Initializable {
    
    // Database connection
    private Connection connection;
    
    // Student Section
    @FXML private AnchorPane addstudentsection;
    @FXML private TextField txtstudname;
    @FXML private TextField txtstudLname;
    @FXML private TextField txtstudid;
    @FXML private TextField txtstudclass;
    @FXML private TextField txtstudsection;
    @FXML private ComboBox<String> txtstudsex;
    
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, Integer> columnstudid;
    @FXML private TableColumn<Student, String> columnstudname;
    @FXML private TableColumn<Student, String> columnstudLname;
    @FXML private TableColumn<Student, String> columnstudsex;
    @FXML private TableColumn<Student, String> columnstudclass;
    @FXML private TableColumn<Student, String> columnstudsection;
    
    // Teacher Section
    @FXML private AnchorPane addteachersection;
    @FXML private TextField txtteachername;
    @FXML private TextField txtteacherLname;
    @FXML private TextField txtteacherid;
    @FXML private TextField txtteacherfield;
    @FXML private TextField txtteacheremail;
    
    @FXML private TableView<Teacher> teacherTable;
    @FXML private TableColumn<Teacher, Integer> columntecherid;
    @FXML private TableColumn<Teacher, String> columntechername;
    @FXML private TableColumn<Teacher, String> columntecherLname;
    @FXML private TableColumn<Teacher, String> columntecherfield;
    @FXML private TableColumn<Teacher, String> columntecheremail;
    
    // Course Section
    @FXML private AnchorPane addcoursesection;
    @FXML private TextField coursecode;
    @FXML private TextField coursename;
    
    @FXML private TableView<Course> courseTable;
    @FXML private TableColumn<Course, String> columncoursecode;
    @FXML private TableColumn<Course, String> columncoursename;
    
    // Schedule Section
    @FXML private AnchorPane createScdsection;
    @FXML private TextField txtCouresnamee;
    @FXML private TextField txtDay;
    @FXML private TextField txtTime;
    @FXML private TextField txtClass;
    @FXML private TextField txtSection;
    
    @FXML private TableView<Schedule> scheduleTable;
    @FXML private TableColumn<Schedule, String> Coursenamecolumn;
    @FXML private TableColumn<Schedule, String> daycolumn;
    @FXML private TableColumn<Schedule, String> timecolumn;
    @FXML private TableColumn<Schedule, String> sclasscolumn;
    @FXML private TableColumn<Schedule, String> ssectioncloumn;
    
    // Other UI elements
    @FXML private Label username;
    @FXML private Button logoutbtn;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Admin Dashboard Controller Initialized");
        
        // Connect to database
        connectToDatabase();
        
        // Initialize student table
        initializeStudentTable();
        
        // Initialize teacher table
        initializeTeacherTable();
        
        // Initialize course table
        initializeCourseTable();
        
        // Initialize schedule table
        initializeScheduleTable();
        
        // Show student section by default
        showStudentSection();
        
        // Load initial data
        handleDisplayStudent();
        handleDisplayTeacher();
        handleDisplayCourse();
        handleDisplaySchedule();
    }
    
    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/EduLearning";
            String user = "root";
            String password = ""; // Your password here
            
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected successfully in Admin Dashboard!");
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            showAlert("Database Error", "Cannot connect to database: " + e.getMessage());
        }
    }
    
    private void initializeStudentTable() {
        if (studentTable != null) {
            columnstudid.setCellValueFactory(new PropertyValueFactory<>("id"));
            columnstudname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            columnstudLname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            columnstudsex.setCellValueFactory(new PropertyValueFactory<>("gender"));
            columnstudclass.setCellValueFactory(new PropertyValueFactory<>("className"));
            columnstudsection.setCellValueFactory(new PropertyValueFactory<>("section"));
            
            // Initialize gender combo box
            if (txtstudsex != null) {
                txtstudsex.getItems().addAll("Male", "Female", "Other");
            }
        }
    }
    
    private void initializeTeacherTable() {
        if (teacherTable != null) {
            columntecherid.setCellValueFactory(new PropertyValueFactory<>("id"));
            columntechername.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            columntecherLname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            columntecherfield.setCellValueFactory(new PropertyValueFactory<>("specialization"));
            columntecheremail.setCellValueFactory(new PropertyValueFactory<>("email"));
        }
    }
    
    private void initializeCourseTable() {
        if (courseTable != null) {
            columncoursecode.setCellValueFactory(new PropertyValueFactory<>("code"));
            columncoursename.setCellValueFactory(new PropertyValueFactory<>("name"));
        }
    }
    
    private void initializeScheduleTable() {
        if (scheduleTable != null) {
            Coursenamecolumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
            daycolumn.setCellValueFactory(new PropertyValueFactory<>("day"));
            timecolumn.setCellValueFactory(new PropertyValueFactory<>("time"));
            sclasscolumn.setCellValueFactory(new PropertyValueFactory<>("className"));
            ssectioncloumn.setCellValueFactory(new PropertyValueFactory<>("section"));
        }
    }
    
    // Navigation methods
    @FXML
    private void addStudent() {
        showStudentSection();
        clearStudentForm();
    }
    
    @FXML
    private void addTeacher() {
        showTeacherSection();
        clearTeacherForm();
    }
    
    @FXML
    private void addCourse() {
        showCourseSection();
        clearCourseForm();
    }
    
    @FXML
    private void creatSchedule() {
        showScheduleSection();
        clearScheduleForm();
    }
    
    private void showStudentSection() {
        addstudentsection.setVisible(true);
        addteachersection.setVisible(false);
        addcoursesection.setVisible(false);
        createScdsection.setVisible(false);
    }
    
    private void showTeacherSection() {
        addstudentsection.setVisible(false);
        addteachersection.setVisible(true);
        addcoursesection.setVisible(false);
        createScdsection.setVisible(false);
    }
    
    private void showCourseSection() {
        addstudentsection.setVisible(false);
        addteachersection.setVisible(false);
        addcoursesection.setVisible(true);
        createScdsection.setVisible(false);
    }
    
    private void showScheduleSection() {
        addstudentsection.setVisible(false);
        addteachersection.setVisible(false);
        addcoursesection.setVisible(false);
        createScdsection.setVisible(true);
    }
    
    // Clear form methods
    private void clearStudentForm() {
        txtstudname.clear();
        txtstudLname.clear();
        txtstudid.clear();
        txtstudclass.clear();
        txtstudsection.clear();
        txtstudsex.getSelectionModel().clearSelection();
    }
    
    private void clearTeacherForm() {
        txtteachername.clear();
        txtteacherLname.clear();
        txtteacherid.clear();
        txtteacherfield.clear();
        txtteacheremail.clear();
    }
    
    private void clearCourseForm() {
        coursecode.clear();
        coursename.clear();
    }
    
    private void clearScheduleForm() {
        txtCouresnamee.clear();
        txtDay.clear();
        txtTime.clear();
        txtClass.clear();
        txtSection.clear();
    }
    
    // Student CRUD methods
    @FXML
    private void handleAddStudent() {
        if (validateStudentForm()) {
            try {
                String query = "INSERT INTO students (id, name, last_name, gender, class, section) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, Integer.parseInt(txtstudid.getText()));
                ps.setString(2, txtstudname.getText());
                ps.setString(3, txtstudLname.getText());
                ps.setString(4, txtstudsex.getValue());
                ps.setString(5, txtstudclass.getText());
                ps.setString(6, txtstudsection.getText());
                
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    showAlert("Success", "Student added successfully!");
                    clearStudentForm();
                    handleDisplayStudent();
                }
                ps.close();
            } catch (SQLException e) {
                showAlert("Database Error", "Error adding student: " + e.getMessage());
            } catch (NumberFormatException e) {
                showAlert("Input Error", "Student ID must be a number!");
            }
        }
    }
    
    @FXML
    private void handleupdateStudent() {
        if (studentTable != null) {
            Student selected = studentTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                // Populate form with selected student data
                txtstudid.setText(String.valueOf(selected.getId()));
                txtstudname.setText(selected.getFirstName());
                txtstudLname.setText(selected.getLastName());
                txtstudsex.setValue(selected.getGender());
                txtstudclass.setText(selected.getClassName());
                txtstudsection.setText(selected.getSection());
                
                // Change button text to indicate update mode
                // You might want to add a separate update button or handle differently
                showAlert("Update Mode", "Student data loaded for update. Make changes and click Update.");
            } else {
                showAlert("No Selection", "Please select a student to update.");
            }
        }
    }
    
    private void updateStudentInDatabase(Student student) {
        try {
            String query = "UPDATE students SET name = ?, last_name = ?, gender = ?, class = ?, section = ? WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getLastName());
            ps.setString(3, student.getGender());
            ps.setString(4, student.getClassName());
            ps.setString(5, student.getSection());
            ps.setString(6, student.getId());
            
            int rows = ps.executeUpdate();
            if (rows > 0) {
                showAlert("Success", "Student updated successfully!");
            }
            ps.close();
        } catch (SQLException e) {
            showAlert("Database Error", "Error updating student: " + e.getMessage());
        }
    }
    
    @FXML
    private void handledeleteStudent() {
        if (studentTable != null) {
            Student selected = studentTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirm Delete");
                confirm.setHeaderText("Delete Student");
                confirm.setContentText("Are you sure you want to delete student: " + selected.getFirstName() + " " + selected.getLastName() + "?");
                
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        deleteStudentFromDatabase(selected.getId());
                    }
                });
            } else {
                showAlert("No Selection", "Please select a student to delete.");
            }
        }
    }
    
    private void deleteStudentFromDatabase(String studentId) {
        try {
            String query = "DELETE FROM students WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, studentId);
            
            int rows = ps.executeUpdate();
            if (rows > 0) {
                showAlert("Success", "Student deleted successfully!");
                handleDisplayStudent();
            }
            ps.close();
        } catch (SQLException e) {
            showAlert("Database Error", "Error deleting student: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDisplayStudent() {
        if (studentTable != null) {
            ObservableList<Student> students = FXCollections.observableArrayList();
            
            try {
                String query = "SELECT * FROM students";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                
                while (rs.next()) {
                    students.add(new Student(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("last_name"),
                        rs.getString("gender"),
                        rs.getString("class"),
                        rs.getString("section")
                    ));
                }
                
                studentTable.setItems(students);
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                showAlert("Database Error", "Error loading students: " + e.getMessage());
            }
        }
    }
    
    private boolean validateStudentForm() {
        if (txtstudid.getText().isEmpty() || txtstudname.getText().isEmpty() || 
            txtstudLname.getText().isEmpty() || txtstudclass.getText().isEmpty() || 
            txtstudsection.getText().isEmpty() || txtstudsex.getValue() == null) {
            showAlert("Validation Error", "All fields are required!");
            return false;
        }
        
        try {
            Integer.parseInt(txtstudid.getText());
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Student ID must be a number!");
            return false;
        }
        
        return true;
    }
    
    // Teacher CRUD methods
    @FXML
    private void handleAddTeacher() {
        if (validateTeacherForm()) {
            try {
                String query = "INSERT INTO teachers (id, name, last_name, field, email) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, Integer.parseInt(txtteacherid.getText()));
                ps.setString(2, txtteachername.getText());
                ps.setString(3, txtteacherLname.getText());
                ps.setString(4, txtteacherfield.getText());
                ps.setString(5, txtteacheremail.getText());
                
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    showAlert("Success", "Teacher added successfully!");
                    clearTeacherForm();
                    handleDisplayTeacher();
                }
                ps.close();
            } catch (SQLException e) {
                showAlert("Database Error", "Error adding teacher: " + e.getMessage());
            } catch (NumberFormatException e) {
                showAlert("Input Error", "Teacher ID must be a number!");
            }
        }
    }
    
    @FXML
    private void handleUpdateTeacher() {
        if (teacherTable != null) {
            Teacher selected = teacherTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                // Populate form
                txtteacherid.setText(String.valueOf(selected.getId()));
                txtteachername.setText(selected.getFirstName());
                txtteacherLname.setText(selected.getLastName());
                txtteacherfield.setText(selected.getSpecialization());
                txtteacheremail.setText(selected.getEmail());
                
                showAlert("Update Mode", "Teacher data loaded for update.");
            } else {
                showAlert("No Selection", "Please select a teacher to update.");
            }
        }
    }
    
    @FXML
    private void handleDeleteTeacher() {
        if (teacherTable != null) {
            Teacher selected = teacherTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirm Delete");
                confirm.setHeaderText("Delete Teacher");
                confirm.setContentText("Are you sure you want to delete teacher: " + selected.getFirstName() + " " + selected.getLastName() + "?");
                
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        deleteTeacherFromDatabase(selected.getId());
                    }
                });
            } else {
                showAlert("No Selection", "Please select a teacher to delete.");
            }
        }
    }
    
    private void deleteTeacherFromDatabase(String teacherId) {
        try {
            String query = "DELETE FROM teachers WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, teacherId);
            
            int rows = ps.executeUpdate();
            if (rows > 0) {
                showAlert("Success", "Teacher deleted successfully!");
                handleDisplayTeacher();
            }
            ps.close();
        } catch (SQLException e) {
            showAlert("Database Error", "Error deleting teacher: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDisplayTeacher() {
        if (teacherTable != null) {
            ObservableList<Teacher> teachers = FXCollections.observableArrayList();
            
            try {
                String query = "SELECT * FROM teachers";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                
                while (rs.next()) {
                    teachers.add(new Teacher(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("last_name"),
                        rs.getString("field"),
                        rs.getString("email")
                    ));
                }
                
                teacherTable.setItems(teachers);
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                showAlert("Database Error", "Error loading teachers: " + e.getMessage());
            }
        }
    }
    
    private boolean validateTeacherForm() {
        if (txtteacherid.getText().isEmpty() || txtteachername.getText().isEmpty() || 
            txtteacherLname.getText().isEmpty() || txtteacherfield.getText().isEmpty() || 
            txtteacheremail.getText().isEmpty()) {
            showAlert("Validation Error", "All fields are required!");
            return false;
        }
        
        if (!txtteacheremail.getText().contains("@")) {
            showAlert("Validation Error", "Please enter a valid email address!");
            return false;
        }
        
        try {
            Integer.parseInt(txtteacherid.getText());
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Teacher ID must be a number!");
            return false;
        }
        
        return true;
    }
    
    // Course CRUD methods
    @FXML
    private void handleAddCourse() {
        if (validateCourseForm()) {
            try {
                String query = "INSERT INTO courses (code, name) VALUES (?, ?)";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, coursecode.getText());
                ps.setString(2, coursename.getText());
                
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    showAlert("Success", "Course added successfully!");
                    clearCourseForm();
                    handleDisplayCourse();
                }
                ps.close();
            } catch (SQLException e) {
                showAlert("Database Error", "Error adding course: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleUpdateCourse() {
        if (courseTable != null) {
            Course selected = courseTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                coursecode.setText(selected.getCode());
                coursename.setText(selected.getName());
                showAlert("Update Mode", "Course data loaded for update.");
            } else {
                showAlert("No Selection", "Please select a course to update.");
            }
        }
    }
    
    @FXML
    private void handleDeleteCourse() {
        if (courseTable != null) {
            Course selected = courseTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirm Delete");
                confirm.setHeaderText("Delete Course");
                confirm.setContentText("Are you sure you want to delete course: " + selected.getName() + "?");
                
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        deleteCourseFromDatabase(selected.getCode());
                    }
                });
            } else {
                showAlert("No Selection", "Please select a course to delete.");
            }
        }
    }
    
    private void deleteCourseFromDatabase(String courseCode) {
        try {
            String query = "DELETE FROM courses WHERE code = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, courseCode);
            
            int rows = ps.executeUpdate();
            if (rows > 0) {
                showAlert("Success", "Course deleted successfully!");
                handleDisplayCourse();
            }
            ps.close();
        } catch (SQLException e) {
            showAlert("Database Error", "Error deleting course: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDisplayCourse() {
        if (courseTable != null) {
            ObservableList<Course> courses = FXCollections.observableArrayList();
            
            try {
                String query = "SELECT * FROM courses";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                
                while (rs.next()) {
                    courses.add(new Course(
                        rs.getString("code"),
                        rs.getString("name")
                    ));
                }
                
                courseTable.setItems(courses);
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                showAlert("Database Error", "Error loading courses: " + e.getMessage());
            }
        }
    }
    
    private boolean validateCourseForm() {
        if (coursecode.getText().isEmpty() || coursename.getText().isEmpty()) {
            showAlert("Validation Error", "Both course code and name are required!");
            return false;
        }
        return true;
    }
    
    // Schedule CRUD methods
    @FXML
    private void handleAddSchedule() {
        if (validateScheduleForm()) {
            try {
                String query = "INSERT INTO schedule (course_name, day, time, class, section) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, txtCouresnamee.getText());
                ps.setString(2, txtDay.getText());
                ps.setString(3, txtTime.getText());
                ps.setString(4, txtClass.getText());
                ps.setString(5, txtSection.getText());
                
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    showAlert("Success", "Schedule added successfully!");
                    clearScheduleForm();
                    handleDisplaySchedule();
                }
                ps.close();
            } catch (SQLException e) {
                showAlert("Database Error", "Error adding schedule: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleUpdateSchedule() {
        if (scheduleTable != null) {
            Schedule selected = scheduleTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                txtCouresnamee.setText(selected.getCourseName());
                txtDay.setText(selected.getDay());
                txtTime.setText(selected.getTime());
                txtClass.setText(selected.getClassName());
                txtSection.setText(selected.getSection());
                showAlert("Update Mode", "Schedule data loaded for update.");
            } else {
                showAlert("No Selection", "Please select a schedule to update.");
            }
        }
    }
    
    @FXML
    private void handleDeleteSchedule() {
        if (scheduleTable != null) {
            Schedule selected = scheduleTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirm Delete");
                confirm.setHeaderText("Delete Schedule");
                confirm.setContentText("Are you sure you want to delete this schedule?");
                
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        deleteScheduleFromDatabase(selected);
                    }
                });
            } else {
                showAlert("No Selection", "Please select a schedule to delete.");
            }
        }
    }
    
    private void deleteScheduleFromDatabase(Schedule schedule) {
        try {
            String query = "DELETE FROM schedule WHERE course_name = ? AND day = ? AND time = ? AND class = ? AND section = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, schedule.getCourseName());
            ps.setString(2, schedule.getDay());
            ps.setString(3, schedule.getTime());
            ps.setString(4, schedule.getClassName());
            ps.setString(5, schedule.getSection());
            
            int rows = ps.executeUpdate();
            if (rows > 0) {
                showAlert("Success", "Schedule deleted successfully!");
                handleDisplaySchedule();
            }
            ps.close();
        } catch (SQLException e) {
            showAlert("Database Error", "Error deleting schedule: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleDisplaySchedule() {
        if (scheduleTable != null) {
            ObservableList<Schedule> schedules = FXCollections.observableArrayList();
            
            try {
                String query = "SELECT * FROM schedule";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                
                while (rs.next()) {
                    schedules.add(new Schedule(
                        rs.getString("course_name"),
                        rs.getString("day"),
                        rs.getString("time"),
                        rs.getString("class"),
                        rs.getString("section")
                    ));
                }
                
                scheduleTable.setItems(schedules);
                rs.close();
                stmt.close();
            } catch (SQLException e) {
                showAlert("Database Error", "Error loading schedules: " + e.getMessage());
            }
        }
    }
    
    private boolean validateScheduleForm() {
        if (txtCouresnamee.getText().isEmpty() || txtDay.getText().isEmpty() || 
            txtTime.getText().isEmpty() || txtClass.getText().isEmpty() || 
            txtSection.getText().isEmpty()) {
            showAlert("Validation Error", "All schedule fields are required!");
            return false;
        }
        return true;
    }
    
    // Logout method
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
            }
        } catch (IOException e) {
            showErrorAlert("Error", "Cannot load login screen: " + e.getMessage());
        }
    }

    private void showErrorAlert(String message) {
        showErrorAlert("Error", message);
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private boolean confirmSignOut() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        alert.setHeaderText("Confirm Sign Out");
        alert.setContentText("Are you sure you want to sign out?");
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    
    // Utility method
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    // Model classes (inner classes for simplicity - should be separate files in real project)
    public static class Student {
        private String id;
        private String firstName;
        private String lastName;
        private String gender;
        private String className;
        private String section;
        
        public Student(String id, String firstName, String lastName, String gender, String className, String section) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.gender = gender;
            this.className = className;
            this.section = section;
        }
        
        public String getId() { return id; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getGender() { return gender; }
        public String getClassName() { return className; }
        public String getSection() { return section; }
    }
    
    public static class Teacher {
        private String id;
        private String firstName;
        private String lastName;
        private String specialization;
        private String email;
        
        public Teacher(String id, String firstName, String lastName, String specialization, String email) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.specialization = specialization;
            this.email = email;
        }
        
        public String getId() { return id; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getSpecialization() { return specialization; }
        public String getEmail() { return email; }
    }
    
    public static class Course {
        private String code;
        private String name;
        
        public Course(String code, String name) {
            this.code = code;
            this.name = name;
        }
        
        public String getCode() { return code; }
        public String getName() { return name; }
    }
    
    public static class Schedule {
        private String courseName;
        private String day;
        private String time;
        private String className;
        private String section;
        
        public Schedule(String courseName, String day, String time, String className, String section) {
            this.courseName = courseName;
            this.day = day;
            this.time = time;
            this.className = className;
            this.section = section;
        }
        
        public String getCourseName() { return courseName; }
        public String getDay() { return day; }
        public String getTime() { return time; }
        public String getClassName() { return className; }
        public String getSection() { return section; }
    }
}