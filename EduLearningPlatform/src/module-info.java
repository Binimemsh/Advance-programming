module EduLearningPlatform {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.swing;
    requires javafx.base;  // Fixed from "bases"
    requires java.desktop;
    requires java.sql;
    requires java.net.http;
    requires java.mail;
    
    // PDFBox 3.0.x modules (CORRECT NAMES)
    requires org.apache.pdfbox;
    requires org.apache.fontbox;
    
    // MySQL Connector - check your actual module name
    // Try one of these:
    requires mysql.connector.j;
    // OR if that doesn't work:
    // requires com.mysql.cj;
    
    opens application to javafx.fxml, javafx.graphics;
    exports application;
}