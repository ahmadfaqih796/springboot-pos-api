package spring.pos.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import io.jsonwebtoken.security.SignatureException;

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
      body.put("message", ex.getMessage());
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

   // Handle PropertyReferenceException (root cause in your case)
   @ExceptionHandler(PropertyReferenceException.class)
   public ResponseEntity<Object> handlePropertyReferenceException(PropertyReferenceException ex, WebRequest request) {
      Map<String, Object> body = new HashMap<>();
      body.put("timestamp", LocalDateTime.now());
      body.put("status", HttpStatus.BAD_REQUEST.value());
      body.put("error", "Invalid Property Reference");
      body.put("message", ex.getMessage());
      body.put("path", request.getDescription(false).substring(4));

      return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
   }

   @ExceptionHandler(Exception.class)
   public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
      Map<String, Object> body = new HashMap<>();
      body.put("timestamp", LocalDateTime.now());
      body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
      body.put("error", "Internal Server Error");
      body.put("message", ex.getMessage());
      body.put("path", request.getDescription(false).substring(4));

      return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
   }

   @ExceptionHandler(SignatureException.class)
   public ResponseEntity<Object> handleSignatureException(SignatureException ex, WebRequest request) {
      Map<String, Object> body = new HashMap<>();
      body.put("timestamp", LocalDateTime.now());
      body.put("status", HttpStatus.UNAUTHORIZED.value());
      body.put("error", "Invalid JWT Signature");
      body.put("message", "The JWT signature does not match the locally computed signature.");
      body.put("path", request.getDescription(false).substring(4));

      return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
   }

}
