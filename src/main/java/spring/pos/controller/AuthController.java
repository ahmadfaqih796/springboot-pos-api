package spring.pos.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import spring.pos.model.user.UserEntity;
import spring.pos.service.AuthService;

@CrossOrigin
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthController {

   @Autowired
   private AuthService authService;

   @Operation(summary = "Register a new user")
   @PostMapping("/register")
   public ResponseEntity<Object> register(@RequestBody UserEntity users) {
      System.out.println("Login request received for user: " + users.getUsername());
      return authService.register(users);
   }

   @Operation(summary = "Login an existing user")
   @PostMapping("/login")
   public ResponseEntity<Map<String, Object>> loginUser(@RequestBody UserEntity users) {
      return authService.login(users);
   }

}
