package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class AdminDashBoardController implements Initializable{
	

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
