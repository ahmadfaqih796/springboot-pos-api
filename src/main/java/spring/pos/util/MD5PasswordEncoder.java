package spring.pos.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5PasswordEncoder {
   public static String encode(String password) {
      try {
         MessageDigest md = MessageDigest.getInstance("MD5");
         md.update(password.getBytes());
         byte[] bytes = md.digest();
         StringBuilder sb = new StringBuilder();
         for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
         }
         return sb.toString();
      } catch (NoSuchAlgorithmException e) {
         e.printStackTrace();
         return null;
      }
   }

   // public static void main(String[] args) {
   // String password = "1234";
   // String hashedPassword = encode(password);
   // System.out.println("MD5 Hashed Password: " + hashedPassword);
   // }
}
