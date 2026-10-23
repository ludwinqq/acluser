package bo.quispe.edwin.presentation.common;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(
            MethodParameter returnType,
            Class<? extends HttpMessageConverter<?>> converterType) {

        // No intervenir cuando Spring necesita escribir un arreglo de bytes.
        // Evita el error ApiResponse cannot be cast to byte[].
        if (ByteArrayHttpMessageConverter.class.isAssignableFrom(converterType)) {
            return false;
        }

        // No envolver las respuestas generadas por Springdoc.
        String packageName = returnType.getContainingClass()
                .getPackageName();

        if (packageName.startsWith("org.springdoc.")) {
            return false;
        }

        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        // No envolver respuestas que ya tienen el formato estándar.
        if (body instanceof ApiResponse<?>) {
            return body;
        }

        // Obtener el código HTTP real de la respuesta.
        int statusCode = ((ServletServerHttpResponse) response)
                .getServletResponse()
                .getStatus();

        String codeDescription =
                statusCode + " " + getStatusDescription(statusCode);

        return ApiResponse.success(
                "Operación exitosa",
                codeDescription,
                body
        );
    }

    private String getStatusDescription(int statusCode) {
        if (statusCode == 200) return "OK";
        if (statusCode == 201) return "Created";
        if (statusCode == 204) return "No Content";
        return "SUCCESS";
    }
}
/*@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Evita volver a envolver si ya es un ApiResponse o un ResponseEntity
        return true; 
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedContentTypeHeader,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        
        
        if (body instanceof ApiResponse) {
            return body;
        }


        int statusCode = ((ServletServerHttpResponse) response).getServletResponse().getStatus();
        String codeDescription = statusCode + " " + getStatusDescription(statusCode);


        return ApiResponse.success("Operación exitosa", codeDescription, body);
    }

    private String getStatusDescription(int statusCode) {
        if (statusCode == 200) return "OK";
        if (statusCode == 201) return "Created";
        return "SUCCESS";
    }
}*/
