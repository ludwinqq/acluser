package bo.quispe.edwin.infrastructure.utils.crypto;

import org.springframework.stereotype.Component;

import bo.quispe.edwin.domain.utils.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Component 
public class BCryptPasswordEncoderAdapter implements PasswordEncoder {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // Constructor sin parámetros para que Spring lo levante limpiamente
    public BCryptPasswordEncoderAdapter() {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder(); // Usa el costo por defecto de 10
    }

    @Override
    public String encode(String rawPassword) {
        if ( rawPassword == null || rawPassword.trim().isEmpty() ) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }
        return bCryptPasswordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }
    
}
