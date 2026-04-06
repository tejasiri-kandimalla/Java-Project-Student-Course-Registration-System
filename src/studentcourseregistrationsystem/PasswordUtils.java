package studentcourseregistrationsystem;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
    
    // Hash a password using BCrypt
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean checkPassword(String password, String hashed) {
        if (hashed == null) {
            return false;
        }
        if (!hashed.startsWith("$2a$")) {
            // Fallback for legacy SHA-256 hashed passwords
            try {
                java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
                String salted = password + "SCRS_SECRET_SALT_2026";
                byte[] hash = digest.digest(salted.getBytes());
                String legacyHash = java.util.Base64.getEncoder().encodeToString(hash);
                return legacyHash.equals(hashed);
            } catch (java.security.NoSuchAlgorithmException e) {
                return false;
            }
        }
        return BCrypt.checkpw(password, hashed);
    }
}
