package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginControler implements Initializable{
	private Stage stage;
	private Scene scene;
	private Parent root;

    @FXML
    private Hyperlink fgtpwd, sup;

    @FXML
    private Pane headerpane, loginpane;

    @FXML
    private Text hesderpane, hesderpane1;

   

    @FXML
    private Button loginbtn;

 

    @FXML
    private PasswordField passwordid;
    
    @FXML
    private ComboBox<String> rolecombo;

    @FXML
    private TextField usernameid;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	rolecombo.getItems().addAll("Student", "Teacher", "Admin");
    	rolecombo.setPromptText("Login As");
    }
    @FXML
    void creatAccount(ActionEvent event) throws IOException {
    	Parent  root = FXMLLoader.load(getClass().getResource("SignUpPage.fxml"));
   	   stage = (Stage)((Node)event.getSource()).getScene().getWindow();
   	   scene = new Scene(root);
          stage.setScene(scene);
          stage.show();
    }

     @FXML
    private void handleLogin(ActionEvent event) throws IOException {
    	 String username = usernameid.getText().trim();
    	  String role = rolecombo.getValue();
    	  Data.username= username;
    	 switchScene(event, role + "DashBoard.fxml");
    	 
     }
  

   /* @FXML
    private void handleLogin(ActionEvent event) throws IOException {
    	 String username = usernameid.getText().trim();
    	    String password = passwordid.getText().trim();
    	    String role = rolecombo.getValue();

    	    if (username.isEmpty() || password.isEmpty() || role == null) {
    	        showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill all fields.");
    	        return;
    	    }

    	    try (Connection conn = DatabaseConnection.connect()) {
    	        String sql = "SELECT * FROM users WHERE email = ? AND password = ? AND role = ?";
    	        PreparedStatement stmt = conn.prepareStatement(sql);
    	        stmt.setString(1, username);
    	        stmt.setString(2, password);
    	        stmt.setString(3, role);

    	        ResultSet rs = stmt.executeQuery();

    	        if (rs.next()) {
    	            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome " + rs.getString("fname") + "!");
    	          
    	            Data.username=rs.getString("fname");
    	            switchScene(event, role + "DashBoard.fxml");
    	        } else {
    	            showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect credentials or role.");
    	        }

    	    } catch (Exception e) {
    	        showAlert(Alert.AlertType.ERROR, "Database Error", "Error connecting to database: " + e.getMessage());
    	    }
    	}
         */
         
    
    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
    	Parent  root = FXMLLoader.load(getClass().getResource(fxmlFile));
  	   stage = (Stage)((Node)event.getSource()).getScene().getWindow();
  	   scene = new Scene(root);
         stage.setScene(scene);
         stage.show();
        
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
    	Alert alert = new Alert(type);
    	alert.setTitle(title);
    	alert.setHeaderText(null);
    	alert.setContentText(message);
    	alert.showAndWait();
    }
}
