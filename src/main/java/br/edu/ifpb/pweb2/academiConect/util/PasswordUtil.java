package br.edu.ifpb.pweb2.academiConect.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

// Validação da Senha do usuário
public abstract class PasswordUtil {
    
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static boolean checkPass(String plainPassword, String hashedPassword) {
        if (BCrypt.checkpw(plainPassword, hashedPassword))
            return true;
        else
            return false;
    }
}
