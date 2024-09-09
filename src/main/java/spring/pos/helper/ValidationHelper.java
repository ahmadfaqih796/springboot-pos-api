package spring.pos.helper;

import java.util.Map;

public class ValidationHelper {

   public static void validateField(String key, String value, Map<String, Object> rules) {

      if (value == null || value.isEmpty()) {
         throw new IllegalArgumentException(key + " cannot be empty");
      }

      if (rules == null) {
         return;
      }

      if (rules.containsKey("minLength")) {
         int minLength = (int) rules.get("minLength");
         if (value.length() < minLength) {
            throw new IllegalArgumentException(key + " must be at least " + minLength + " characters");
         }
      }

      if (rules.containsKey("maxLength")) {
         int maxLength = (int) rules.get("maxLength");
         if (value.length() > maxLength) {
            throw new IllegalArgumentException(key + " must be at most " + maxLength + " characters");
         }

      }

   }

}
