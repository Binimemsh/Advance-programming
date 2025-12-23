package application;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

public class StudentDashBoardController implements Initializable {
    
    // Database connection
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    
    @FXML
    private Label username;
    
    @FXML
    private Button clschedulebtn;

    @FXML
    private TableColumn<ScheduleData, String> columnfri;

    @FXML
    private TableColumn<ScheduleData, String> columnthu;

    @FXML
    private TableColumn<ScheduleData, String> columntuw;

    @FXML
    private TableColumn<ScheduleData, String> columnwen;

    @FXML
    private TableColumn<ScheduleData, String> coursecolumn;

    @FXML
    private TableColumn<GradeData, String> idcolumn;

    @FXML
    private Button materialbtn;

    @FXML
    private TableColumn<ScheduleData, String> moncolumn;

    @FXML
    private TableColumn<GradeData, String> namecolumn;

    @FXML
    private Button resultbtn;

    @FXML
    private TableColumn<GradeData, String> resultcolumn;

    @FXML
    private AnchorPane resultsection;

    @FXML
    private AnchorPane schedulesection;

    // TableViews
    @FXML private TableView<ScheduleData> scheduleTable;
    @FXML private TableView<GradeData> resultTable;

    // Data lists
    private ObservableList<ScheduleData> scheduleList;
    private ObservableList<GradeData> gradeList;

    @FXML
    void handleDisplayResult(ActionEvent event) {
        schedulesection.setVisible(false);
        resultsection.setVisible(true);
        displayStudentResults();
    }

    @FXML
    void handleDisplaySchedule(ActionEvent event) {
        resultsection.setVisible(false);
        schedulesection.setVisible(true);
        displayStudentSchedule();
    }

    @FXML
    void handleMaterial(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("courses_view.fxml"));
            Parent root = loader.load();
             
            Stage stage = new Stage();
            stage.setTitle("Learning Materials - PDF Courses");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root, 1000, 700));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Cannot open learning materials: " + e.getMessage());
        }
    }
    
    public void displayUsername() {
        String user = Data.username;
        if (user != null && !user.isEmpty()) {
            user = user.substring(0, 1).toUpperCase() + user.substring(1);
            username.setText(user);
        } else {
            username.setText("Student");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        displayUsername();
        initializeDatabase();
        setupTableColumns();
        
        // Load initial data
        displayStudentSchedule();
        displayStudentResults();
        
        // Show schedule section by default
        resultsection.setVisible(false);
        schedulesection.setVisible(true);
    }

    private void initializeDatabase() {
        try {
            // Use direct database connection similar to other controllers
            String url = "jdbc:mysql://localhost:3306/EduLearning";
            String user = "root";
            String password = "";
            
            connect = DriverManager.getConnection(url, user, password);
            System.out.println("Database connected successfully in Student Dashboard!");
        } catch (Exception e) {
            showErrorAlert("Database Error", "Cannot connect to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupTableColumns() {
        // Schedule table columns - Using property names that match ScheduleData getters
        coursecolumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        moncolumn.setCellValueFactory(new PropertyValueFactory<>("monday"));
        columntuw.setCellValueFactory(new PropertyValueFactory<>("tuesday"));
        columnwen.setCellValueFactory(new PropertyValueFactory<>("wednesday"));
        columnthu.setCellValueFactory(new PropertyValueFactory<>("thursday"));
        columnfri.setCellValueFactory(new PropertyValueFactory<>("friday"));

        // Results table columns - FIXED: Use correct property names for GradeData
        idcolumn.setCellValueFactory(new PropertyValueFactory<>("courseCode")); // Changed from "studentId"
        namecolumn.setCellValueFactory(new PropertyValueFactory<>("courseName")); // Changed from "courseName"
        resultcolumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
    }

    private void displayStudentSchedule() {
        scheduleList = FXCollections.observableArrayList();
        
        if (connect == null) {
            showErrorAlert("Database Error", "No database connection");
            addSampleScheduleData(); // Show sample data
            return;
        }
        
        // Get student's class and section based on username (which is student ID)
        String studentClass = getStudentClass();
        String studentSection = getStudentSection();
        
        if (studentClass == null || studentSection == null) {
            showInfoAlert("Information", "Class or section information not found. Showing sample schedule.");
            addSampleScheduleData();
            return;
        }
        
        String sql = "SELECT course_name, day, time FROM schedule WHERE class = ? AND section = ? ORDER BY day, time";
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, studentClass);
            prepare.setString(2, studentSection);
            result = prepare.executeQuery();
            
            scheduleList = convertToWeeklySchedule(result);
            scheduleTable.setItems(scheduleList);
            
            if (scheduleList.isEmpty()) {
                showInfoAlert("Schedule", "No schedule found for your class and section. Showing sample data.");
                addSampleScheduleData();
            }
            
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Error loading schedule: " + e.getMessage());
            e.printStackTrace();
            
            // Add sample data for testing
            addSampleScheduleData();
        } finally {
            closeDatabaseResources();
        }
    }

    private ObservableList<ScheduleData> convertToWeeklySchedule(ResultSet result) throws SQLException {
        ObservableList<ScheduleData> weeklySchedule = FXCollections.observableArrayList();
        
        // Create a map to organize by day
        Map<String, StringBuilder> daySchedules = new HashMap<>();
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        
        // Initialize the map
        for (String day : days) {
            daySchedules.put(day, new StringBuilder());
        }
        
        // Process the result set
        while (result.next()) {
            String day = result.getString("day");
            String courseName = result.getString("course_name");
            String time = result.getString("time");
            
            if (day != null && daySchedules.containsKey(day)) {
                StringBuilder schedule = daySchedules.get(day);
                if (schedule.length() > 0) {
                    schedule.append("\n");
                }
                schedule.append(courseName).append(" (").append(time).append(")");
            }
        }
        
        // Create ScheduleData objects for each day
        for (String day : days) {
            // Create weekly schedule data
            ScheduleData scheduleData = new ScheduleData();
            scheduleData.setCourseName(day); // Using day as course name for display
            
            String scheduleText = daySchedules.get(day).toString();
            if (scheduleText.isEmpty()) {
                scheduleText = "No classes scheduled";
            }
            
            // Set the appropriate day field
            switch (day) {
                case "Monday": 
                    scheduleData.setMonday(scheduleText); 
                    break;
                case "Tuesday": 
                    scheduleData.setTuesday(scheduleText); 
                    break;
                case "Wednesday": 
                    scheduleData.setWednesday(scheduleText); 
                    break;
                case "Thursday": 
                    scheduleData.setThursday(scheduleText); 
                    break;
                case "Friday": 
                    scheduleData.setFriday(scheduleText); 
                    break;
            }
            
            weeklySchedule.add(scheduleData);
        }
        
        return weeklySchedule;
    }
    
    private void displayStudentResults() {
        gradeList = FXCollections.observableArrayList();
        
        if (connect == null) {
            showErrorAlert("Database Error", "No database connection");
            return;
        }
        
        // Query grades for the current student using student ID from Data.username
        // First, let's find the student ID from username
        String studentId = getStudentIdFromUsername();
        if (studentId == null) {
            showErrorAlert("Error", "Student ID not found. Please contact administrator.");
            return;
        }
        
        String sql = "SELECT g.course_code, g.grade, c.name AS course_name " +
                    "FROM grades g " +
                    "LEFT JOIN courses c ON g.course_code = c.code " +
                    "WHERE g.student_id = ? " +
                    "ORDER BY g.course_code";
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, studentId);
            result = prepare.executeQuery();
            
            while (result.next()) {
                String courseName = result.getString("course_name");
                String courseCode = result.getString("course_code");
                String grade = result.getString("grade");
                
                // If course name is null, use course code
                if (courseName == null || courseName.isEmpty()) {
                    courseName = courseCode;
                }
                
                // Create GradeData object
                GradeData gradeData = new GradeData(
                    studentId,  // studentId
                    "",         // studentName (not needed for student view)
                    "",         // studentLastName (not needed)
                    courseCode, // courseCode
                    grade       // grade (numeric 0-100)
                );
                
                // Set course name for display
                gradeData.setCourseName(courseName);
                
                gradeList.add(gradeData);
            }
            
            resultTable.setItems(gradeList);
            
            if (gradeList.isEmpty()) {
                showInfoAlert("Results", "No grades found for your account yet.");
            }
            
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Error loading results: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeDatabaseResources();
        }
    }

    // NEW METHOD: Get student ID from username
    private String getStudentIdFromUsername() {
        if (connect == null) return null;
        
        String sql = "SELECT id FROM students WHERE id = ? OR name = ?";
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, Data.username);
            prepare.setString(2, Data.username);
            result = prepare.executeQuery();
            
            if (result.next()) {
                return result.getString("id");
            }
        } catch (SQLException e) {
            System.err.println("Error getting student ID: " + e.getMessage());
        } finally {
            closeDatabaseResources();
        }
        
        return null;
    }

    private String getStudentClass() {
        if (connect == null) return null;
        
        // First try to find by ID (username might be student ID)
        String sql = "SELECT class FROM students WHERE id = ?";
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, Data.username);
            result = prepare.executeQuery();
            
            if (result.next()) {
                return result.getString("class");
            }
            
            // If not found by ID, try by name
            sql = "SELECT class FROM students WHERE name = ?";
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, Data.username);
            result = prepare.executeQuery();
            
            if (result.next()) {
                return result.getString("class");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting student class: " + e.getMessage());
        } finally {
            closeDatabaseResources();
        }
        
        return null;
    }

    private String getStudentSection() {
        if (connect == null) return null;
        
        String sql = "SELECT section FROM students WHERE id = ?";
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, Data.username);
            result = prepare.executeQuery();
            
            if (result.next()) {
                return result.getString("section");
            }
            
            // If not found by ID, try by name
            sql = "SELECT section FROM students WHERE name = ?";
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, Data.username);
            result = prepare.executeQuery();
            
            if (result.next()) {
                return result.getString("section");
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting student section: " + e.getMessage());
        } finally {
            closeDatabaseResources();
        }
        
        return null;
    }

    @FXML
    void signOut(ActionEvent event) {
        try {
            if (confirmSignOut()) {
                closeDatabaseResources();
                
                // Close database connection
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
    
    // Sample data methods for testing
    private void addSampleScheduleData() {
        scheduleList = FXCollections.observableArrayList();
        
        // Create sample weekly schedule
        ScheduleData monday = new ScheduleData();
        monday.setCourseName("Monday");
        monday.setMonday("Mathematics (09:00-10:00)\nPhysics (11:00-12:00)");
        
        ScheduleData tuesday = new ScheduleData();
        tuesday.setCourseName("Tuesday");
        tuesday.setTuesday("Chemistry (10:00-11:00)\nBiology (14:00-15:00)");
        
        ScheduleData wednesday = new ScheduleData();
        wednesday.setCourseName("Wednesday");
        wednesday.setWednesday("English (09:00-10:00)\nHistory (13:00-14:00)");
        
        ScheduleData thursday = new ScheduleData();
        thursday.setCourseName("Thursday");
        thursday.setThursday("Computer Science (10:00-11:00)\nMathematics (14:00-15:00)");
        
        ScheduleData friday = new ScheduleData();
        friday.setCourseName("Friday");
        friday.setFriday("Physics (09:00-10:00)\nChemistry (11:00-12:00)");
        
        scheduleList.addAll(monday, tuesday, wednesday, thursday, friday);
        scheduleTable.setItems(scheduleList);
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

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}