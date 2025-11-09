package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;




public class DatabaseConnection {

    public static Connection connect() {
        Connection conn = null;
        try {
            // Creates a database file "education.db" if it doesn’t exist
        	String url = "jdbc:mysql://localhost:3306/Edulearning";
        	String name1 = "root";
        	String pass = "";
            conn = DriverManager.getConnection(url, name1, pass);
            System.out.println("✅ Connected to SQLite database.");
        } catch (SQLException e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
        }
        return conn;
    }
}
