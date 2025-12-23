package application;

public class Data {
    public static String username;
    public static String role; // "admin", "teacher", "student"
    
    public static void setUser(String user, String userRole) {
        username = user;
        role = userRole;
    }
    
    public static void clear() {
        username = null;
        role = null;
    }
}