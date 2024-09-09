package spring.pos.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.transaction.TransactionScoped;
import spring.pos.helper.PaginationHelper;
import spring.pos.helper.ValidationHelper;
import spring.pos.model.role.RoleEntity;
import spring.pos.model.role.RoleRepository;
import spring.pos.model.role.RoleSpecification;
import spring.pos.model.user.UserRepository;

@Service
@TransactionScoped
public class RoleService {

   @Autowired
   private RoleRepository roleRepository;

   @Autowired
   private UserRepository userRepository;

   public Map<String, Object> get(
         int page,
         int size,
         String sortField,
         String sortDir,
         String keyword,
         boolean disabledPagination) {
      Specification<RoleEntity> spec = RoleSpecification.containsKeyword(keyword);
      return PaginationHelper.buildResponse(page, size, sortField, sortDir, disabledPagination, spec, roleRepository);
      // Map<String, Object> response = new HashMap<>();
      // Specification<RoleEntity> spec = RoleSpecification.containsKeyword(keyword);
      // if (disabledPagination) {
      // List<RoleEntity> roles = roleRepository.findAll(spec);
      // response.put("content", roles);
      // response.put("totalElements", roles.size());
      // } else {
      // Pageable pageable = PaginationHelper.createPageable(page, size, sortField,
      // sortDir);
      // Page<RoleEntity> roles = PaginationHelper.fetchPageData(pageable, spec,
      // roleRepository, !keyword.isEmpty(),
      // keyword);
      // response = PaginationHelper.responsePagination(roles);
      // }
      // return response;
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
      Map<String, Object> nameRules = Map.of("minLength", 3, "maxLength", 10);
      ValidationHelper.validateField("Name", role.getName(), nameRules);
   }
}
