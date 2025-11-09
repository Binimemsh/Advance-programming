package application;

import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class StudentDashBoardController implements Initializable{
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
}
