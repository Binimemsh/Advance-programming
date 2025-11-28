package application;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseSetup {
    public static void initialize() {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                fname TEXT NOT NULL,
                sname TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                grade TEXT,
                section TEXT,
                password TEXT NOT NULL,
                role TEXT DEFAULT 'Student'
            );
        """;

        try (Connection conn = DatabaseConnection.connectDb();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ Users table ready.");
        } catch (Exception e) {
            System.out.println("❌ Table creation failed: " + e.getMessage());
        }
    }
}
