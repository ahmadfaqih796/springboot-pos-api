package spring.pos.util;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {

   @SuppressWarnings("unchecked")
   public static <T> ResponseEntity<T> generateResponse(String message, HttpStatus status, Object data) {
      Map<String, Object> response = new HashMap<>();
      response.put("status", status.value());
      response.put("message", message);
      response.put("data", data);
      response.put("timestamp", LocalDateTime.now());

      return new ResponseEntity<>((T) response, status);
   }

   // public static ResponseEntity<Object> generateResponse(String message,
   // HttpStatus status, Object data) {
   // Map<String, Object> response = new HashMap<>();
   // response.put("status", status.value());
   // response.put("message", message);
   // response.put("data", data);
   // response.put("timestamp", LocalDateTime.now());

   // return new ResponseEntity<>( response, status);
   // }

   public static <T> Map<String, Object> buildResponseData(Page<T> pageData) {
      Map<String, Object> response = new HashMap<>();
      response.put("content", pageData.getContent());
      response.put("totalElements", pageData.getTotalElements());
      response.put("totalPages", pageData.getTotalPages());
      response.put("size", pageData.getSize());
      response.put("number", pageData.getNumber());
      return response;
   }

   public ResponseEntity<Map<String, Object>> buildUnauthorizedResponse(String message) {
      Map<String, Object> responseData = new HashMap<>();
      responseData.put("status", HttpStatus.UNAUTHORIZED.value());
      responseData.put("message", message);
      responseData.put("data", null);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseData);
   }
}
