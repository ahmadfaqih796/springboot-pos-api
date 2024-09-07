package spring.pos.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

   @ExceptionHandler(IllegalArgumentException.class)
   public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
      Map<String, Object> body = new HashMap<>();
      body.put("timestamp", LocalDateTime.now());
      body.put("status", HttpStatus.BAD_REQUEST.value());
      body.put("error", "Bad Request");
      body.put("message", ex.getMessage()); // Mengambil pesan error dari exception
      body.put("path", request.getDescription(false).substring(4));

      return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(DataIntegrityViolationException.class)
   public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
         WebRequest request) {
      Map<String, Object> body = new HashMap<>();
      body.put("timestamp", LocalDateTime.now());
      body.put("status", HttpStatus.CONFLICT.value());
      body.put("error", "Data Integrity Violation");
      body.put("message", "Cannot delete: Data is still being used.");
      body.put("path", request.getDescription(false).substring(4));

      return new ResponseEntity<>(body, HttpStatus.CONFLICT);
   }
}