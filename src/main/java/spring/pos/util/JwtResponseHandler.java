package spring.pos.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

public class JwtResponseHandler {
   @Autowired
   private JwtUtil jwtUtil;

   @SuppressWarnings("deprecation")
   public ResponseEntity<Map<String, Object>> handleToken(String token, Object data) {
      try {
         String username = jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));
         System.out.println("Valid Token: " + username);
         Map<String, Object> responseData = new HashMap<>();
         responseData.put("status", HttpStatus.OK.value());
         responseData.put("message", "Berhasil dapatkan data role");
         responseData.put("data", data);
         return ResponseEntity.ok(responseData);
      } catch (ExpiredJwtException e) {
         System.out.println("Invalid Token: " + e);
         return buildUnauthorizedResponse("maaf session kamu habis, silahkan login");
      } catch (SignatureException e) {
         System.out.println("Invalid Token: " + e);
         return buildUnauthorizedResponse("silahkan login kembali");
      }
   }

   private ResponseEntity<Map<String, Object>> buildUnauthorizedResponse(String message) {
      Map<String, Object> responseData = new HashMap<>();
      responseData.put("status", HttpStatus.UNAUTHORIZED.value());
      responseData.put("message", message);
      responseData.put("data", null);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseData);
   }
}
