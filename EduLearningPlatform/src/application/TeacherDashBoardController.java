package application;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TeacherDashBoardController implements Initializable {
	  @FXML
	    private AnchorPane Schedulesection;
	  
	    @FXML
	    private AnchorPane studlistsection;
	    
	    @FXML
	    private AnchorPane smtgradesection;
	    
	    

	    @FXML
	    private Button addgradebtn;

	    @FXML
	    private TextField byclass;

	    @FXML
	    private TextField byid;

	    @FXML
	    private Button byidbtn;

	    @FXML
	    private TableColumn<?, ?> classcolumn;

	    @FXML
	    private TableColumn<?, ?> coursecolumn;

	    @FXML
	    private TableColumn<?, ?> daycolumn;

	    @FXML
	    private TableColumn<?, ?> fnamecolumn;

	    @FXML
	    private TableColumn<?, ?> gradecolumn;

	    @FXML
	    private TableColumn<?, ?> idcolumn;

	    @FXML
	    private Button liststudbtn;

	    @FXML
	    private Button logoutbtn;

	    @FXML
	    private TableColumn<?, ?> namecolumn;

	    @FXML
	    private Button sbtgradebtn;

	    @FXML
	    private Button schedulebtn;

	    @FXML
	    private Button schedulebtn11;

	    @FXML
	    private TableColumn<?, ?> sectioncolumn;

	    @FXML
	    private TableColumn<?, ?> sexcolumn;

	    @FXML
	    private TableColumn<?, ?> studFnamecolumn;

	    @FXML
	    private TableColumn<?, ?> studnamecolumn;

	    @FXML
	    private TextField texstudid;

	    @FXML
	    private TableColumn<?, ?> timecolumn;

	    @FXML
	    private TextField txtcourscode;

	    @FXML
	    private TextField txtgrade;

	    @FXML
	    private Button updategrade;
        
	    @FXML
	    private Button addmaterial;
	    

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
	   
	   
	    @FXML
	    void studentList(ActionEvent event) {
	    	Schedulesection.setVisible(false);
	    	smtgradesection.setVisible(false);
	    	studlistsection.setVisible(true);
	    }

	    @FXML
	    void Schedule(ActionEvent event) {
	    	
	    	studlistsection.setVisible(false);
	    	smtgradesection.setVisible(false);
	    	Schedulesection.setVisible(true);
	    }

	    @FXML
	    void gradeSubmit(ActionEvent event) {
	    	Schedulesection.setVisible(false);
	    	studlistsection.setVisible(false);
	    	smtgradesection.setVisible(true);
	    }
	    
	    @FXML
	    void HandleSearchByClass(ActionEvent event) {

	    }

	    @FXML
	    void handleAddGrade(ActionEvent event) {
	    
	    }

	    @FXML
	    void handleSearchById(ActionEvent event) {

	    }

	    @FXML
	    void handleUpdateGrade(ActionEvent event) {

	    }
	    
	    @FXML
	    void handleAdMaterial(ActionEvent event) {
	    	 try {
	             FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/admin_panel.fxml"));
	             Parent root = loader.load();

	             Stage stage = new Stage();
	             stage.setTitle("Admin Panel - PDF Management");
	             stage.initModality(Modality.APPLICATION_MODAL);
	             stage.setScene(new Scene(root, 600, 400));
	             stage.show();

	         } catch (Exception e) {
	             e.printStackTrace();
	             showErrorAlert( "Cannot open admin panel");
	         }
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
