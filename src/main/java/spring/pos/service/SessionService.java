package spring.pos.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import spring.pos.model.user.UserEntity;
import spring.pos.model.user.UserRepository;

@Service
public class SessionService {

   private final UserRepository userRepository;

   public SessionService(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   public UserEntity getUserSession() {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String username = authentication.getName();

      List<UserEntity> users = userRepository.findByUsername(username);

      if (users.isEmpty()) {
         throw new IllegalArgumentException("User not found");
      }

      return users.get(0);
   }

}
