package bo.quispe.edwin.presentation.common;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonPropertyOrder({ "success", "message", "code", "timestamp", "data" }) 
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private String code; // <--- Nuevo campo para el código
    private String timestamp;
    private T data;

    // Constructor privado general
    private ApiResponse(boolean success, String message, String code, T data) {
        this.success = success;
        this.message = message;
        this.code = code;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        this.data = data;
    }

    // Métodos estáticos de éxito (Por defecto podemos ponerle "200 OK" o el que corresponda)
    public static <T> ApiResponse<T> success(String message, String code, T data) {
        return new ApiResponse<>(true, message, code, data);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Operación exitosa", "200 OK", data);
    }

    // Métodos estáticos de error
    public static <T> ApiResponse<T> error(String message, String code) {
        return new ApiResponse<>(false, message, code, null);
    }

    // --- GETTERS (Obligatorios para Jackson) ---
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public T getData() {
        return data;
    }
}
