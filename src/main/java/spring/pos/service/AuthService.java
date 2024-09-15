package spring.pos.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.TransactionScoped;
import spring.pos.model.role.RoleEntity;
import spring.pos.model.role.RoleRepository;
import spring.pos.model.user.UserEntity;
import spring.pos.model.user.UserRepository;
import spring.pos.schema.auth.login.LoginRequest;
import spring.pos.schema.auth.login.LoginResponse;
import spring.pos.schema.auth.register.RegisterRequest;
import spring.pos.schema.auth.register.RegisterResponse;
import spring.pos.schema.auth.register.RegisterResponse.UserData;
import spring.pos.schema.auth.register.RegisterResponse.UserData.RoleData;
import spring.pos.util.JwtUtil;
import spring.pos.util.MD5PasswordEncoder;
import spring.pos.util.ResponseHandler;

@Service
@TransactionScoped
public class AuthService {

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private RoleRepository roleRepository;

   @Autowired
   private JwtUtil jwtUtil;

   // Register User
   public ResponseEntity<RegisterResponse> register(RegisterRequest user) {
      try {
         if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
            UserEntity newUser = new UserEntity();
            newUser.setUsername(user.getUsername());
            newUser.setPassword(user.getPassword());
            newUser.setFullName(user.getFullName());
            newUser.setPosition(user.getPosition());
            newUser.setTelephone(user.getTelephone());
            if (user.getRoleId() != null) {
               RoleEntity roleEntity = roleRepository.findById(user.getRoleId())
                     .orElseThrow(() -> new IllegalArgumentException("Role not found"));
               newUser.setRoleEntity(roleEntity);
            }
            UserEntity userResponse = userRepository.save(newUser);

            RoleData roleData = null;

            if (userResponse.getRoleEntity() != null) {
               roleData = new RoleData(
                     userResponse.getRoleEntity().getRoleId(),
                     userResponse.getRoleEntity().getName());
            }

            UserData userData = new UserData(
                  userResponse.getAgentId(),
                  roleData,
                  userResponse.getFullName(),
                  userResponse.getPosition(),
                  userResponse.getUsername(),
                  userResponse.getTelephone());

            return ResponseHandler.generateResponse("User registered successfully", HttpStatus.OK, userData);
         } else {
            throw new IllegalArgumentException("Username already exists");
         }
      } catch (Exception e) {
         // Log the exception
         System.err.println("Error during registration: " + e.getMessage());
         throw e;
      }
   }

   public ResponseEntity<Object> register(UserEntity user) {
      // String encodedPassword = MD5PasswordEncoder.encode(user.getPassword());
      // user.setPassword(encodedPassword);
      // return userRepository.save(user);
      try {
         if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
            UserEntity userResponse = userRepository.save(user);
            return ResponseHandler.generateResponse("User registered successfully", HttpStatus.OK, userResponse);
         } else {
            throw new IllegalArgumentException("Username already exists");
         }
      } catch (Exception e) {
         // Log the exception
         System.err.println("Error during registration: " + e.getMessage());
         throw e;
      }
   }

   public List<UserEntity> checkUser(UserEntity users) {
      return userRepository.findByUsername(users.getUsername());
   }

   public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
      List<UserEntity> existingUsers = userRepository.findByUsername(loginRequest.getUsername());
      if (!existingUsers.isEmpty()) {
         String password = MD5PasswordEncoder.encode(loginRequest.getPassword());
         boolean loginSuccessful = userRepository.findByUsernameAndPassword(
               loginRequest.getUsername(), password).isPresent();
         System.out.println(
               "Login request received for user: " + loginRequest.getUsername() + " - " + password);
         if (loginSuccessful) {
            String token = jwtUtil.generateToken(loginRequest.getUsername());
            UserEntity loggedInUser = existingUsers.get(0);

            LoginResponse.UserData.RoleData roleData = new LoginResponse.UserData.RoleData(
                  loggedInUser.getRoleEntity().getRoleId(),
                  loggedInUser.getRoleEntity().getName());

            LoginResponse.UserData userData = new LoginResponse.UserData(
                  loggedInUser.getAgentId(), roleData, loggedInUser.getFullName(),
                  loggedInUser.getPosition(), loggedInUser.getUsername(),
                  loggedInUser.getTelephone(), token);

            LoginResponse response = LoginResponse.builder()
                  .status(HttpStatus.OK.value())
                  .message("berhasil login")
                  .data(userData)
                  .build();

            return ResponseEntity.ok(response);
         } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                  .body(new LoginResponse(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password", null));
         }
      } else {
         return ResponseEntity.status(HttpStatus.NOT_FOUND)
               .body(new LoginResponse(HttpStatus.NOT_FOUND.value(), "User not found", null));
      }
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
            userData.put("roleData", loggedInUser.getRoleEntity());
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
