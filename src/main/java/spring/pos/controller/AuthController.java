package spring.pos.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import spring.pos.model.user.UserEntity;
import spring.pos.model.user.UserRepository;
import spring.pos.service.AuthService;
import spring.pos.util.JwtUtil;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {
   // @Autowired
   // private AuthenticationManager authenticationManager;

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private AuthService authService;

   @Autowired
   private JwtUtil jwtUtil;

   @PostMapping("/register")
   public UserEntity register(@RequestBody UserEntity users) {
      System.out.println("Login request received for user: ");
      return userRepository.save(users);
   }

   // @PostMapping("/login")
   // public ResponseEntity<String> login(@RequestBody UserEntity users) {
   // System.out.println("Login request received for user: " +
   // users.getUsername());
   // boolean loginSuccessful = authService.login(
   // users.getUsername(),
   // users.getPassword());
   // if (loginSuccessful) {
   // return ResponseEntity.ok("Login successful!");
   // } else {
   // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username
   // or password");
   // }
   // }

   @PostMapping("/login2")
   public ResponseEntity<Map<String, Object>> login(@RequestBody UserEntity users) {
      List<UserEntity> existingUsers = authService.checkUser(users);
      // logger.info("username: {}", users.getUsername());
      // logger.info("password: {}", users.getPassword());
      System.out.println("Login request received for user: " + existingUsers);
      if (!existingUsers.isEmpty()) {
         boolean loginSuccessful = authService.login(users.getUsername(), users.getPassword());
         if (loginSuccessful) {

            String token = jwtUtil.generateToken(users.getUsername());

            UserEntity loggedInUser = existingUsers.get(0);
            // logger.info("User found: {}", token);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("status", HttpStatus.OK.value());
            responseData.put("message", "berhasil login");

            Map<String, Object> userData = new HashMap<>();
            userData.put("full_name", loggedInUser.getFullName());
            userData.put("position", loggedInUser.getPosition());
            userData.put("telephone", loggedInUser.getTelephone());
            userData.put("username", loggedInUser.getUsername());
            userData.put("agent_id", loggedInUser.getAgentId());
            userData.put("token", token);
            responseData.put("data", userData);
            return ResponseEntity.ok(responseData);
         } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.UNAUTHORIZED.value());
            errorResponse.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
         }
      } else {
         Map<String, Object> errorResponse = new HashMap<>();
         errorResponse.put("status", HttpStatus.NOT_FOUND.value());
         errorResponse.put("message", "User not found");
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
      }
   }

}
