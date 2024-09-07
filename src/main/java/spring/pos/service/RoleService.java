package spring.pos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.TransactionScoped;
import spring.pos.model.role.RoleEntity;
import spring.pos.model.role.RoleRepository;
import spring.pos.model.user.UserRepository;

@Service
@TransactionScoped
public class RoleService {

   @Autowired
   private RoleRepository roleRepository;

   @Autowired
   private UserRepository userRepository;

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

   public void delete(Long roleId) {
      RoleEntity roleEntity = roleRepository.findById(roleId)
            .orElseThrow(() -> new IllegalArgumentException("Role not found"));

      boolean hasUser = userRepository.existsByRoleEntity(roleEntity);
      System.out.println("hasUser: " + hasUser);
      System.out.println("roleEntity: " + roleEntity);

      if (hasUser) {
         throw new IllegalArgumentException("Role has users");
      }

      roleRepository.delete(roleEntity);

   }

   private void validateRole(RoleEntity role) throws IllegalArgumentException {
      if (role.getName() == null || role.getName().isEmpty()) {
         throw new IllegalArgumentException("Role name cannot be empty");
      }
   }
}
