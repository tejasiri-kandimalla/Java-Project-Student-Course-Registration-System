import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import studentcourseregistrationsystem.DatabaseConnection;

public class CheckDB {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users;");
             ResultSet rs = pstmt.executeQuery()) {
             
            System.out.println("--- USERS TABLE ---");
            while (rs.next()) {
                System.out.println("Username: " + rs.getString("username"));
                System.out.println("Password: " + rs.getString("password"));
                System.out.println("Role:     " + rs.getString("role"));
                System.out.println("-------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
