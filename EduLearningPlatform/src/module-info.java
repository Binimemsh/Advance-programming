module EduLearningPlatform {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires jdk.jdi;
	requires java.sql;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
	exports application;
}
