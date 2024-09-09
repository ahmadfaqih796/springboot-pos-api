package spring.pos.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import spring.pos.model.user.UserEntity;
import spring.pos.model.user.UserRepository;
import spring.pos.service.AuthService;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private AuthService authService;

   @PostMapping("/register")
   public UserEntity register(@RequestBody UserEntity users) {
      System.out.println("Login request received for user: ");
      return userRepository.save(users);
   }

   @PostMapping("/login")
   public ResponseEntity<Map<String, Object>> loginUser(@RequestBody UserEntity users) {
      return authService.login(users);
   }

}
