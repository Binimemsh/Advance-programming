package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AdminDashBoardController implements Initializable{
	

    @FXML
    private Label username;
    

    @FXML
    private TableColumn<?, ?> CourseCodecolumn;

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
    private Button addstudentbtn11;

    @FXML
    private Button addstudentbtn111;

    @FXML
    private Button addstudentbtn112;

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
    private TableColumn<?, ?> columncrhour;

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
    private Button creatschedulebtn1;

    @FXML
    private TextField credithour;

    @FXML
    private TableColumn<?, ?> daycolumn;

    @FXML
    private Button deleteschedule;

    @FXML
    private Button deleteteacher;

    @FXML
    private Button displayschedule;

    @FXML
    private Button displayteacher;

    @FXML
    private TableColumn<?, ?> sclasscolumn;

    @FXML
    private TableColumn<?, ?> ssectioncloumn;

    @FXML
    private TableColumn<?, ?> timecolumn;

    @FXML
    private TextField txtCouresCode;

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
    private Button updateteacher;



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
    void handleDisplayTeacher(ActionEvent event) {

    }

    @FXML
    void handleUpdateSchedule(ActionEvent event) {

    }

    @FXML
    void handleUpdateTeacher(ActionEvent event) {

    }
	 
    public void displayUsername() {
    	String user = Data.username;
    	user = user.substring(0, 1).toUpperCase()+ user.substring(1);
    	username.setText(user);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resource) {
    	//txtstudsex.getItems().addAll("Mail", "Femail");
    	//txtstudsex.setPromptText("Gender");
    	displayUsername();
    }
}
