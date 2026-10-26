package bo.quispe.edwin.domain.utils;

import java.util.regex.Pattern;

public class PasswordValidator {

    // Expresión regular que exige: Mayúscula, Minúscula, Número, Carácter Especial y Mínimo 8 caracteres
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    /**
     * LÓGICA DE NEGOCIO PURA: Valida si la contraseña cumple con las políticas de seguridad.
     */
    public static void validate(String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }
        
        if (!pattern.matcher(rawPassword).matches()) {
            throw new IllegalArgumentException(
                "La contraseña es insegura. Debe contener al menos 8 caracteres, " +
                "una letra mayúscula, una letra minúscula, un número y un carácter especial (@$!%*?&)."
            );
        }
    }
}
