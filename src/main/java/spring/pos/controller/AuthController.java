package spring.pos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import spring.pos.schema.auth.login.LoginRequest;
import spring.pos.schema.auth.login.LoginResponse;
import spring.pos.schema.auth.register.RegisterRequest;
import spring.pos.schema.auth.register.RegisterResponse;
import spring.pos.service.AuthService;

@CrossOrigin
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthController {

   @Autowired
   private AuthService authService;

   @PostMapping("/register")
   public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest users) {
      System.out.println("Login request received for user: " + users.getUsername());
      return authService.register(users);
   }
   // public ResponseEntity<Object> register(@RequestBody UserEntity users) {
   // System.out.println("Login request received for user: " +
   // users.getUsername());
   // return authService.register(users);
   // }

   // @PostMapping("/login")
   // public ResponseEntity<Map<String, Object>> loginUser(@RequestBody UserEntity
   // users) {
   // return authService.login(users);
   // }

   @PostMapping("/login")
   public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
      return authService.login(loginRequest);
   }

}
