package spring.pos.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring.pos.model.user.UserEntity;
import spring.pos.model.user.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

   @Autowired
   private UserRepository userRepository;

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      List<UserEntity> users = userRepository.findByUsername(username);
      if (users.isEmpty()) {
         throw new UsernameNotFoundException("User not found");
      }
      UserEntity user = users.get(0);
      return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .build();
   }
}
