package application;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

public class StudentDashBoardController implements Initializable{
	
	  @FXML
	    private Label username;
	    @FXML
	    private Button clschedulebtn;

	    @FXML
	    private TableColumn<?, ?> columnfri;

	    @FXML
	    private TableColumn<?, ?> columnthu;

	    @FXML
	    private TableColumn<?, ?> columntuw;

	    @FXML
	    private TableColumn<?, ?> columnwen;

	    @FXML
	    private TableColumn<?, ?> coursecolumn;

	    @FXML
	    private TableColumn<?, ?> idcolumn;

	    @FXML
	    private Button materialbtn;

	    @FXML
	    private TableColumn<?, ?> moncolumn;

	    @FXML
	    private TableColumn<?, ?> namecolumn;

	    @FXML
	    private Button resultbtn;

	    @FXML
	    private TableColumn<?, ?> resultcolumn;

	    @FXML
	    private AnchorPane resultsection;

	    @FXML
	    private AnchorPane schedulesection;

	

	    @FXML
	    void handleDisplayResult(ActionEvent event) {
	    	
	    	schedulesection.setVisible(false);
	    	resultsection.setVisible(true);
	    }

	    @FXML
	    void handleDisplaySchedule(ActionEvent event) {
	    	
	    	resultsection.setVisible(false);
	    	schedulesection.setVisible(true);
	    }

	    @FXML
	    void handleMaterial(ActionEvent event) {
	    	 try {
	             FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/courses_view.fxml"));
	             Parent root = loader.load();
                 
	             Stage stage = new Stage();
	             stage.setTitle("Admin Panel - PDF Management");
	             stage.initModality(Modality.APPLICATION_MODAL);
	             stage.setScene(new Scene(root, 1000, 700));
	            // Scene scene = new Scene(root, 1000, 700);
	            // stage.getStyle().add(getClass().getResource("/application/application.css").toExternalForm());
	             stage.show();

	         } catch (Exception e) {
	             e.printStackTrace();
	             showErrorAlert( "Cannot open admin panel");
	         }
	    }
	  
	 public void displayUsername() {
	    	String user = Data.username;
	    	user = user.substring(0, 1).toUpperCase()+ user.substring(1);
	    	username.setText(user);
	    }

	    public void initialize(URL location, ResourceBundle resource) {
	    	
	    	displayUsername();
	    }

	    @FXML
	    void signOut(ActionEvent event) {
	    	  
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

	    private boolean confirmSignOut() {
	        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	        alert.setTitle("Sign Out");
	        alert.setHeaderText("Confirm Sign Out");
	        alert.setContentText("Are you sure you want to sign out?");
	        
	        Optional<ButtonType> result = alert.showAndWait();
	        return result.isPresent() && result.get() == ButtonType.OK;
	    }
	    
	    private void showErrorAlert( String message) {
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.showAndWait();
	    }
}
