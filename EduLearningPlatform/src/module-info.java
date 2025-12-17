module EduLearningPlatform {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.swing;
    requires javafx.base;
    requires java.desktop;
    requires java.sql;
    // Add PDFBox modules
    requires org.apache.pdfbox;
    requires org.apache.pdfbox.io;
	requires mysql.connector.j;
	requires java.net.http;
	requires java.mail;
	//requires javafx.swing;
    
    opens application to javafx.fxml, javafx.graphics;
    exports application;
}
