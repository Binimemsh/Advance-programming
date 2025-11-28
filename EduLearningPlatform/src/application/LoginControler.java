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
    	
    	// Add input validation listeners
    	setupValidation();
    }
    
    private void setupValidation() {
        // Username validation - restrict to alphanumeric characters
        usernameid.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\w*")) {
                usernameid.setText(oldValue);
            }
        });
        
        // Password validation - restrict length and show strength
        passwordid.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 50) { // Reasonable password length limit
                passwordid.setText(oldValue);
            }
        });
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
    	  String password = passwordid.getText();
    	  
    	  // Input validation
    	  if (!validateInputs(username, role, password)) {
    		  return;
    	  }
    	  
    	  // Authentication validation
    	  if (!authenticateUser(username, password, role)) {
    		  showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username, password, or role.");
    		  return;
    	  }
    	  
    	  Data.username = username;
    	  switchScene(event, role + "DashBoard.fxml");
    }
    
    private boolean validateInputs(String username, String role, String password) {
        // Check for empty fields
        if (username.isEmpty() || role == null || role.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill all fields.");
            return false;
        }
        
        // Username validation
        if (!isValidUsername(username)) {
            showAlert(Alert.AlertType.WARNING, "Invalid Username", 
                     "Username must be 3-20 characters long and contain only letters, numbers, and underscores.");
            return false;
        }
        
        // Password validation
        if (!isValidPassword(password)) {
            showAlert(Alert.AlertType.WARNING, "Invalid Password", 
                     "Password must be at least 6 characters long.");
            return false;
        }
        
        // Role validation
        if (!isValidRole(role)) {
            showAlert(Alert.AlertType.WARNING, "Invalid Role", 
                     "Please select a valid role from the dropdown.");
            return false;
        }
        
        return true;
    }
    
    private boolean isValidUsername(String username) {
        // Username should be 3-20 characters, alphanumeric and underscores only
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }
    
    private boolean isValidPassword(String password) {
        // Password should be at least 6 characters
        return password.length() >= 6;
    }
    
    private boolean isValidRole(String role) {
        return role.equals("Student") || role.equals("Teacher") || role.equals("Admin");
    }
    
    private boolean authenticateUser(String username, String password, String role) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            // Get database connection (you'll need to implement your database connection)
            connection = DatabaseConnection.connectDb();
            
            // SQL query to check user credentials
            String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ? AND status = 'active'";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password); // You should hash passwords in real applications
            preparedStatement.setString(3, role);
            
            resultSet = preparedStatement.executeQuery();
            
            return resultSet.next(); // Returns true if user exists with given credentials
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to verify credentials. Please try again.");
            return false;
        } finally {
            // Close resources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private String hashPassword(String password) {
        // In a real application, use proper password hashing like BCrypt
        // This is a simple example - replace with proper hashing
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            return password; // Fallback - not secure
        }
    }
    
    // Method to clear form
    private void clearForm() {
        usernameid.clear();
        passwordid.clear();
        rolecombo.getSelectionModel().clearSelection();
        usernameid.requestFocus();
    }
    
    // Method to handle failed login attempts (optional security feature)
    private void handleFailedLogin() {
        // You can implement logic to track failed attempts and lock accounts
        showAlert(Alert.AlertType.WARNING, "Security Notice", 
                 "Multiple failed attempts may result in account temporary suspension.");
    }

    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
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