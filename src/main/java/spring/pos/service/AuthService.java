package spring.pos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.TransactionScoped;
import spring.pos.model.user.UserEntity;
import spring.pos.model.user.UserRepository;

@Service
@TransactionScoped
public class AuthService {

   @Autowired
   private UserRepository userRepository;

   // Register User
   public UserEntity register(UserEntity users) {
      // String encodedPassword = MD5PasswordEncoder.encode(users.getPassword());
      // users.setPassword(encodedPassword);
      return userRepository.save(users);
   }

   public List<UserEntity> checkUser(UserEntity users) {
      return userRepository.findByUsername(users.getUsername());
   }

   public boolean login(String username, String password) {
      // String encodedPassword = MD5PasswordEncoder.encode(password);
      return userRepository.findByUsernameAndPassword(username, password).isPresent();
   }

}
