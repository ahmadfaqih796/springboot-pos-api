package spring.pos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.TransactionScoped;
import spring.pos.model.role.RoleEntity;
import spring.pos.model.role.RoleRepository;

@Service
@TransactionScoped
public class RoleService {

   @Autowired
   private RoleRepository roleRepository;

   public List<RoleEntity> findAll() {
      return roleRepository.findAll();
   }

   public RoleEntity create(RoleEntity role) {
      return roleRepository.save(role);
   }
}
