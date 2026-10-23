package bo.quispe.edwin.domain.utils;

public interface PasswordEncoder {

    // Cifra el texto plano
    String encode(String rawPassword);

    // Compara el texto plano contra el hash guardado (Útil para el Login futuro)
    boolean matches(String rawPassword, String encodedPassword);

}
