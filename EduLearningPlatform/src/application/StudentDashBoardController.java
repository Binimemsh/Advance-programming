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
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

public class StudentDashBoardController implements Initializable{
    
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

    // TableViews (add these to your FXML)
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/courses_view.fxml"));
            Parent root = loader.load();
             
            Stage stage = new Stage();
            stage.setTitle("Learning Materials - PDF Courses");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root, 1000, 700));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Cannot open learning materials");
        }
    }
    
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
        displayStudentSchedule();
        displayStudentResults();
    }

    private void initializeDatabase() {
        try {
            connect = DatabaseConnection.connectDb();
        } catch (Exception e) {
            showErrorAlert("Database Error", "Cannot connect to database: " + e.getMessage());
        }
    }

    private void setupTableColumns() {
        // Schedule table columns - assuming weekly schedule structure
        moncolumn.setCellValueFactory(new PropertyValueFactory<>("monday"));
        columntuw.setCellValueFactory(new PropertyValueFactory<>("tuesday"));
        columnwen.setCellValueFactory(new PropertyValueFactory<>("wednesday"));
        columnthu.setCellValueFactory(new PropertyValueFactory<>("thursday"));
        columnfri.setCellValueFactory(new PropertyValueFactory<>("friday"));
        coursecolumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));

        // Results table columns
        idcolumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        namecolumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        resultcolumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
    }

    private void displayStudentSchedule() {
        scheduleList = FXCollections.observableArrayList();
        
        // Get student's class and section from database
        String studentClass = getStudentClass();
        String studentSection = getStudentSection();
        
        if (studentClass != null && studentSection != null) {
            // Query schedule for the student's class and section
            String sql = "SELECT * FROM schedule WHERE class = ? AND section = ? ORDER BY day, time";
            
            try {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, studentClass);
                prepare.setString(2, studentSection);
                result = prepare.executeQuery();
                
                // Convert schedule data to weekly format
                scheduleList = convertToWeeklySchedule(result);
                scheduleTable.setItems(scheduleList);
                
            } catch (SQLException e) {
              
            }
        } else {
           
        }
    }

    private ObservableList<ScheduleData> convertToWeeklySchedule(ResultSet result) throws SQLException {
        ObservableList<ScheduleData> weeklySchedule = FXCollections.observableArrayList();
        
        // Initialize days
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        
        for (String day : days) {
            // Reset result set to beginning for each day
            result.beforeFirst();
            
            StringBuilder daySchedule = new StringBuilder();
            while (result.next()) {
                if (result.getString("day").equalsIgnoreCase(day)) {
                    if (daySchedule.length() > 0) {
                        daySchedule.append("\n");
                    }
                    daySchedule.append(result.getString("course_name"))
                              .append(" (")
                              .append(result.getString("time"))
                              .append(")");
                }
            }
            
            // Create schedule data for this day
            ScheduleData scheduleData = new ScheduleData();
            scheduleData.setCourseName("Day: " + day);
            
            // Set the appropriate day field
            switch (day.toLowerCase()) {
                case "monday":
                    scheduleData.setMonday(daySchedule.toString());
                    break;
                case "tuesday":
                    scheduleData.setTuesday(daySchedule.toString());
                    break;
                case "wednesday":
                    scheduleData.setWednesday(daySchedule.toString());
                    break;
                case "thursday":
                    scheduleData.setThursday(daySchedule.toString());
                    break;
                case "friday":
                    scheduleData.setFriday(daySchedule.toString());
                    break;
            }
            
            weeklySchedule.add(scheduleData);
        }
        
        return weeklySchedule;
    }


    private void displayStudentResults() {
        gradeList = FXCollections.observableArrayList();
        
        // Query grades for the current student
        String sql = "SELECT g.course_code, g.grade, c.name as course_name " +
                    "FROM grades g " +
                    "LEFT JOIN courses c ON g.course_code = c.code " +
                    "WHERE g.student_id = ? " +
                    "ORDER BY g.course_code";
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, Data.username); // Using username as student ID
            result = prepare.executeQuery();
            
            while (result.next()) {
                gradeList.add(new GradeData(
                    Data.username,
                    result.getString("course_name") != null ? 
                        result.getString("course_name") : result.getString("course_code"),
                    "", // Empty for last name
                    result.getString("course_code"),
                    result.getString("grade")
                ));
            }
            
            resultTable.setItems(gradeList);
            
            if (gradeList.isEmpty()) {
              
            }
            
        } catch (SQLException e) {
          
        }
    }

  
    private String getStudentClass() {
        String sql = "SELECT class FROM students WHERE id = ?";
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, Data.username);
            result = prepare.executeQuery();
            
            if (result.next()) {
                return result.getString("class");
            }
        } catch (SQLException e) {
            System.err.println("Error getting student class: " + e.getMessage());
        }
        
        return null;
    }

    private String getStudentSection() {
        String sql = "SELECT section FROM students WHERE id = ?";
        
        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, Data.username);
            result = prepare.executeQuery();
            
            if (result.next()) {
                return result.getString("section");
            }
        } catch (SQLException e) {
            System.err.println("Error getting student section: " + e.getMessage());
        }
        
        return null;
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