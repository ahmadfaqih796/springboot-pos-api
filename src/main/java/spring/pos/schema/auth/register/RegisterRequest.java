package spring.pos.schema.auth.register;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

   @Schema(description = "Username of the user", example = "v")
   private String username;

   @Schema(description = "Password of the user", example = "1234")
   private String password;

   @Schema(description = "Full name of the user", example = "Vivian")
   private String fullName;

   @Schema(description = "Position of the user", example = "Manager")
   private String position;

   @Schema(description = "Telephone number of the user", example = "12345678")
   private Integer telephone;

   @Schema(description = "Role of the user", example = "1L")
   private Long roleId;

}
