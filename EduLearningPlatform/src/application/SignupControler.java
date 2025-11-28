
package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SignupControler {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private PasswordField confirmpassword;

    @FXML
    private TextField emailid;

    @FXML
    private TextField fnameid;

    @FXML
    private TextField gradeid;

    @FXML
    private Pane headerpane;

    @FXML
    private PasswordField passwordid;

    @FXML
    private TextField sectionid;

    @FXML
    private Button signupbtn;

    @FXML
    private TextField snameid;

    // Go back to Login Page
    @FXML
    void backToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Handle Signup button click
    @FXML
    void handleLogin(ActionEvent event) throws IOException {
        String fname = fnameid.getText().trim();
        String sname = snameid.getText().trim();
        String email = emailid.getText().trim();
        String grade = gradeid.getText().trim();
        String section = sectionid.getText().trim();
        String password = passwordid.getText().trim();
        String confirm = confirmpassword.getText().trim();

        // Basic validation
        if (fname.isEmpty() || sname.isEmpty() || email.isEmpty() || grade.isEmpty() ||
            section.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill all fields.");
            return;
        }

        // Email validation
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert(Alert.AlertType.WARNING, "Invalid Email", "Please enter a valid email address.");
            return;
        }

        // Password match check
        if (!password.equals(confirm)) {
            showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match!");
            return;
        }

        // Password strength check
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[-_@$!%*?&])[A-Za-z\\d-_@$!%*?&]{8,}$")) {
            showAlert(Alert.AlertType.WARNING, "Weak Password",
                    "Password must be at least 8 characters long and include uppercase, lowercase, number, and special character.");
            return;
        }

        // If everything is OK
        showAlert(Alert.AlertType.INFORMATION, "Account Created", 
                  "Account successfully created for " + fname + " " + sname + "!");

        // Redirect to login page
        Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();}
   
       
      /*  try (Connection conn = DatabaseConnection.connect()) {
            String sql = "INSERT INTO users (fname, sname, email, grade, section, password, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, fname);
            stmt.setString(2, sname);
            stmt.setString(3, email);
            stmt.setString(4, grade);
            stmt.setString(5, section);
            stmt.setString(6, password);
            stmt.setString(7, "Student"); // default role

            stmt.executeUpdate();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully!");
            
            // Go back to login
             root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not create account: " + e.getMessage());
        }
    }*/

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

    
  
