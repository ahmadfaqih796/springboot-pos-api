package spring.pos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.TransactionScoped;
import spring.pos.model.user.UserEntity;
import spring.pos.model.user.UserRepository;

@Service
@TransactionScoped
public class UserService {

   @Autowired
   private UserRepository userRepository;

   public Iterable<UserEntity> findAll() {
      return userRepository.findAll();
   }

}
