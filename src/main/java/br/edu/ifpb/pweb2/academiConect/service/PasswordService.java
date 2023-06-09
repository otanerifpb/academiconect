package br.edu.ifpb.pweb2.academiConect.service;

import org.springframework.security.crypto.bcrypt.BCrypt;

// REQFUNC 13 - Autenticação e Autorização
// Validação da Senha do usuário
public abstract class PasswordService {
    
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
