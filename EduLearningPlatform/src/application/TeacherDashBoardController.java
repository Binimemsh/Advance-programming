package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class TeacherDashBoardController implements Initializable {
/*
    @FXML
    private Button addbtn;

    @FXML
    private Button allstudbtn;

    @FXML
    private TableColumn<?, ?> classcolumn;

    @FXML
    private TextField classid;

    @FXML
    private TableColumn<?, ?> daycolumn;

    @FXML
    private TableColumn<?, ?> fanamecolumn;

    @FXML
    private Button findonebtn;

    @FXML
    private TableColumn<?, ?> idcolumn;

    @FXML
    private Button loutbtn;

    @FXML
    private TableColumn<?, ?> namecolumn;

    @FXML
    private TableColumn<?, ?> resultcolumn;

    @FXML
    private ComboBox<?> sctionid;

    @FXML
    private Button sdulbtn;

    @FXML
    private TableColumn<?, ?> sectioncolumn;

    @FXML
    private TableColumn<?, ?> sexcolumn;

    @FXML
    private Button slistbtn;

    @FXML
    private Button srchbtn;

    @FXML
    private TextField studid;

    @FXML
    private Button sugradebtn;

    @FXML
    private TableView<?> tablecolumn;

    @FXML
    private TableView<?> tableview;

    @FXML
    private TableColumn<?, ?> timecolumn;

    @FXML
    private Button uptbtn;*/

    @FXML
    private Label username;
    
    public void displayUsername() {
    	String user = Data.username;
    	user = user.substring(0, 1).toUpperCase()+ user.substring(1);
    	username.setText(user);
    }

    public void initialize(URL location, ResourceBundle resource) {
    	//rolecombo.getItems().addAll("Student", "Teacher", "Admin");
    //	rolecombo.setPromptText("Login As");
    	displayUsername();
    }
}
