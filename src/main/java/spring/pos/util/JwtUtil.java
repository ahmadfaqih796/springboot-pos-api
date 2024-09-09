package spring.pos.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

   private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
   // hitungannya mili detik
   private static final long EXPIRATION_TIME = 86400000; // 1 hari
   // private static final long EXPIRATION_TIME = 10000; // 10 detik

   public String generateToken(String username) {
      Date now = new Date();
      Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

      return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SECRET_KEY)
            .compact();
   }

   public String getUsernameFromToken(String token) {
      Jws<Claims> claims = Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token);

      return claims.getBody().getSubject();
   }

   public boolean validateToken(String token) {
      try {
         Jwts.parserBuilder()
               .setSigningKey(SECRET_KEY)
               .build()
               .parseClaimsJws(token);
         return true;
      } catch (Exception e) {
         return false;
      }
   }

   // public static void main(String[] args) {
   // String password = "gas";
   // String hashedPassword = generateToken(password);
   // System.out.println("MD5 Hashed Password: " + hashedPassword);
   // }
}
