package spring.pos.schema.auth.register;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {

   @Schema(description = "HTTP status code", example = "200")
   private int status;

   @Schema(description = "Register status message", example = "User registered successfully")
   private String message;

   @Schema(description = "Data of the user")
   private UserData data;

   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public static class UserData {
      @Schema(description = "ID of the agent", example = "10")
      private Long agentId;

      @Schema(description = "Role data of the user")
      private RoleData roleData;

      @Schema(description = "Full name of the user", example = "John Doe")
      private String fullName;

      @Schema(description = "Position of the user", example = "Manager")
      private String position;

      @Schema(description = "Username of the user", example = "john_doe")
      private String username;

      @Schema(description = "Telephone of the user", example = "123456789")
      private Integer telephone;

      @Data
      @AllArgsConstructor
      @NoArgsConstructor
      public static class RoleData {
         @Schema(description = "ID of the role", example = "1")
         private Long roleId;

         @Schema(description = "Name of the role", example = "Admin")
         private String name;
      }
   }

}
