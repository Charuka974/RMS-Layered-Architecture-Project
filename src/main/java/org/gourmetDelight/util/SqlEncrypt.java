package org.gourmetDelight.util;
import org.mindrot.jbcrypt.BCrypt;


public class SqlEncrypt {
    public static String hashPassword(String password){
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(password,salt);
        return hashedPassword;
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (hashedPassword == null || hashedPassword.isEmpty()) {
            throw new IllegalArgumentException("Hashed password cannot be null or empty");
        }

        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid password hash format: " + hashedPassword);
            return false;
        }
    }


}
