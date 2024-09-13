package spring.pos.schema.auth.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @Schema(description = "Username of the user", example = "v")
    private String username;

    @Schema(description = "Password of the user", example = "1234")
    private String password;
}
