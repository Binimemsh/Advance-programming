package application;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AdminDashBoardController {
	 @FXML
	    private TableColumn<?, ?> Coursenamecolumn;

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
	    private TableColumn<?, ?> columncoursecode;

	    @FXML
	    private TableColumn<?, ?> columncoursename;

	    @FXML
	    private TableColumn<?, ?> columnstudLname;

	    @FXML
	    private TableColumn<?, ?> columnstudclass;

	    @FXML
	    private TableColumn<?, ?> columnstudid;

	    @FXML
	    private TableColumn<?, ?> columnstudname;

	    @FXML
	    private TableColumn<?, ?> columnstudsection;

	    @FXML
	    private TableColumn<?, ?> columnstudsex;

	    @FXML
	    private TableColumn<?, ?> columntecherLname;

	    @FXML
	    private TableColumn<?, ?> columntecheremail;

	    @FXML
	    private TableColumn<?, ?> columntecherfield;

	    @FXML
	    private TableColumn<?, ?> columntecherid;

	    @FXML
	    private TableColumn<?, ?> columntechername;

	    @FXML
	    private TextField coursecode;

	    @FXML
	    private TextField coursename;

	    @FXML
	    private AnchorPane createScdsection;

	    @FXML
	    private Button creatschedulebtn;

	    @FXML
	    private TableColumn<?, ?> daycolumn;

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
	    private TableColumn<?, ?> sclasscolumn;

	    @FXML
	    private TableColumn<?, ?> ssectioncloumn;

	    @FXML
	    private TableColumn<?, ?> timecolumn;

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
	    private ComboBox<?> txtstudsex;

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


    public void displayUsername() {
    	String user = Data.username;
    	user = user.substring(0, 1).toUpperCase()+ user.substring(1);
    	username.setText(user);
    }

    public void initialize(URL location, ResourceBundle resource) {
    	
    	displayUsername();
    }



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

    @FXML
    void handlUpdateTeacher(ActionEvent event) {

    }

    @FXML
    void handleAddSchedule(ActionEvent event) {

    }

    @FXML
    void handleAddStudent(ActionEvent event) {

    }

    @FXML
    void handleAddTeacher(ActionEvent event) {

    }

    @FXML
    void handleDeleteSchedule(ActionEvent event) {

    }

    @FXML
    void handleDeleteTeacher(ActionEvent event) {

    }

    @FXML
    void handleDisplaySchedule(ActionEvent event) {

    }

    @FXML
    void handleDisplayStudent(ActionEvent event) {

    }

    @FXML
    void handleDisplayTeacher(ActionEvent event) {

    }

    @FXML
    void handleUpdateSchedule(ActionEvent event) {

    }

    @FXML
    void handleUpdateTeacher(ActionEvent event) {

    }

    @FXML
    void handledeleteStudent(ActionEvent event) {

    }

    @FXML
    void handleupdateStudent(ActionEvent event) {

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
        	if(confirmSignOut()){
        	 Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
             stage.setScene(scene);
             stage.show();
            stage.centerOnScreen();
            }else {
            	showErrorAlert("Something happen! you are not Signed out.");
            }
            
        } catch (IOException e) {
            //logger.error("Error loading login screen", e);
            showErrorAlert("Cannot load login screen.");
        }
    }

   

    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
