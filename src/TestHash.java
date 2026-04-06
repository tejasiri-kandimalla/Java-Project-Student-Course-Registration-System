import java.security.MessageDigest;
import java.util.Base64;

public class TestHash {
    public static void main(String[] args) throws Exception {
        String pw = "12345";
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(pw.getBytes("UTF-8"));
        String b64 = Base64.getEncoder().encodeToString(digest);
        System.out.println("SHA-256: " + b64);
    }
}
