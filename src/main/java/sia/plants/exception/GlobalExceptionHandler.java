package sia.plants.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArg(IllegalArgumentException ex) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("error", ex.getMessage());
        return ResponseEntity.status(400).body(errorBody);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException ex) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("error", ex.getMessage());
        return ResponseEntity.status(404).body(errorBody);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        ex.getRootCause();
        String message = ex.getRootCause().getMessage();

        Map<String, String> errorBody = new HashMap<>();

        if (message != null && message.contains("User and plant must belong to the same organization")) {
            errorBody.put("error", "User and plant must belong to the same organization");
            return ResponseEntity.status(403).body(errorBody);
        }

        // Обработка других возможных нарушений целостности, например email
        if (message != null && message.contains("users_email_key")) {
            errorBody.put("error", "User with this email already exists");
            return ResponseEntity.status(409).body(errorBody);
        }

        errorBody.put("error", "Data integrity violation: " + message);
        return ResponseEntity.status(400).body(errorBody);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntime(RuntimeException ex) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("error", ex.getMessage());
        return ResponseEntity.status(500).body(errorBody);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("error", ex.getMessage());
        return ResponseEntity.status(500).body(errorBody);
    }
}
