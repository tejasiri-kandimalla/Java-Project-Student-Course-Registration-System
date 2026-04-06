import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import studentcourseregistrationsystem.DatabaseConnection;
import java.io.PrintWriter;
import java.io.FileWriter;

public class CheckDBHex {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users;");
             ResultSet rs = pstmt.executeQuery();
             PrintWriter out = new PrintWriter(new FileWriter("out_java.txt"))) {
             
            out.println("--- USERS TABLE ---");
            while (rs.next()) {
                out.println("Username: " + rs.getString("username"));
                String pass = rs.getString("password");
                out.println("Password: " + pass);
                out.println("Password Length: " + (pass!=null?pass.length():0));
                
                StringBuilder hex = new StringBuilder();
                if (pass != null) {
                    for (char c : pass.toCharArray()) {
                        hex.append(String.format("%02x ", (int)c));
                    }
                }
                out.println("Password Hex: " + hex.toString());
                out.println("Role:     " + rs.getString("role"));
                out.println("-------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
