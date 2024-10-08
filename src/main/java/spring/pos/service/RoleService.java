package spring.pos.service;

import java.util.List;
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

   public List<RoleEntity> getAll() {
      return roleRepository.findAll();
   }

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

   public RoleEntity update(Long roleId, RoleEntity payload) {
      // Fetch the role by its ID or throw an exception if it doesn't exist
      RoleEntity roleEntity = roleRepository.findById(roleId)
            .orElseThrow(() -> new IllegalArgumentException("Role not found"));

      // Check if another role with the same name exists and isn't the one being
      // updated
      Optional<RoleEntity> existingRole = roleRepository.findByName(payload.getName());
      if (existingRole.isPresent() && !existingRole.get().getRoleId().equals(roleId)) {
         throw new IllegalArgumentException("Role name already exists");
      }

      // Validate the payload (e.g., role name not empty, max length, etc.)
      validateRole(payload);

      // Apply changes from the payload to the fetched role entity
      roleEntity.setName(payload.getName());
      // Apply any other fields from payload as needed, e.g., createdAt, updatedAt,
      // etc.

      // Save the updated role entity
      return roleRepository.save(roleEntity);
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
