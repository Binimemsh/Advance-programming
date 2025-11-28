package application;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static Connection connectDb() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/edulearning", "root", "");
            System.out.println("Database connected successfully!");
            return connect;
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }
}