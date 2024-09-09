package spring.pos.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.TransactionScoped;
import spring.pos.model.user.UserEntity;
import spring.pos.model.user.UserRepository;
import spring.pos.util.JwtUtil;

@Service
@TransactionScoped
public class AuthService {

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private JwtUtil jwtUtil;

   // Register User
   public UserEntity register(UserEntity users) {
      // String encodedPassword = MD5PasswordEncoder.encode(users.getPassword());
      // users.setPassword(encodedPassword);
      return userRepository.save(users);
   }

   public List<UserEntity> checkUser(UserEntity users) {
      return userRepository.findByUsername(users.getUsername());
   }

   public ResponseEntity<Map<String, Object>> login(UserEntity users) {
      List<UserEntity> existingUsers = this.checkUser(users);
      if (!existingUsers.isEmpty()) {
         boolean loginSuccessful = userRepository.findByUsernameAndPassword(users.getUsername(), users.getPassword())
               .isPresent();
         if (loginSuccessful) {

            String token = jwtUtil.generateToken(users.getUsername());

            UserEntity loggedInUser = existingUsers.get(0);

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("status", HttpStatus.OK.value());
            responseData.put("message", "berhasil login");

            Map<String, Object> userData = new HashMap<>();
            userData.put("fullName", loggedInUser.getFullName());
            userData.put("position", loggedInUser.getPosition());
            userData.put("telephone", loggedInUser.getTelephone());
            userData.put("username", loggedInUser.getUsername());
            userData.put("agentId", loggedInUser.getAgentId());
            userData.put("roleData", loggedInUser.getRoleDTO());
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
