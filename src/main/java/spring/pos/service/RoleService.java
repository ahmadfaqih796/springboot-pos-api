package spring.pos.service;

import java.util.List;
import java.util.Optional;

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

   public RoleEntity create(RoleEntity role) throws IllegalArgumentException {
      Optional<RoleEntity> existingRole = roleRepository.findByName(role.getName());
      if (existingRole.isPresent()) {
         throw new IllegalArgumentException("Role already exists");
      }
      validateRole(role);
      return roleRepository.save(role);
   }

   private void validateRole(RoleEntity role) throws IllegalArgumentException {
      if (role.getName() == null || role.getName().isEmpty()) {
         throw new IllegalArgumentException("Role name cannot be empty");
      }
   }
}
