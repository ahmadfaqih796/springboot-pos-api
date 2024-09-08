package spring.pos.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import spring.pos.model.role.RoleEntity;
import spring.pos.service.RoleService;
import spring.pos.util.JwtResponseHandler;
import spring.pos.util.ResponseHandler;

@RestController
@RequestMapping("/roles")
public class RoleController {

   @Autowired
   private RoleService roleService;

   @Autowired
   private JwtResponseHandler jwtResponseHandler;

   @GetMapping()
   public ResponseEntity<Map<String, Object>> getRole(
         @RequestHeader("Authorization") String token,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size,
         @RequestParam(value = "sortField", defaultValue = "roleId") String sortField,
         @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
         @RequestParam(value = "keyword", defaultValue = "") String keyword,
         @RequestParam(value = "disabledPagination", defaultValue = "false") boolean disabledPagination) {

      // Pageable pageable = PaginationHelper.createPageable(page, size, sortField,
      // sortDir);
      // Specification<RoleEntity> spec = RoleSpecification.containsKeyword(keyword);
      // Page<RoleEntity> roles = PaginationHelper.fetchPageData(pageable, spec,
      // roleRepository, !keyword.isEmpty(),
      // keyword);
      // Map<String, Object> response = PaginationHelper.responsePagination(roles);

      Map<String, Object> response = roleService.get(page, size, sortField, sortDir, keyword, disabledPagination);
      return jwtResponseHandler.handleToken(token, response);
   }

   @PostMapping()
   public RoleEntity postRole(@RequestBody RoleEntity role) {
      return roleService.create(role);
   }

   @DeleteMapping("/{roleId}")
   public ResponseEntity<?> deleteRole(@PathVariable Long roleId) {
      roleService.delete(roleId);
      return ResponseHandler.generateResponse("Role deleted successfully", HttpStatus.OK, roleId);
      // try {
      // roleService.delete(roleId);
      // return ResponseHandler.generateResponse("Role deleted successfully",
      // HttpStatus.OK, roleId);
      // } catch (Exception e) {
      // return ResponseHandler.generateResponse("An error occurred while deleting the
      // role.",
      // HttpStatus.INTERNAL_SERVER_ERROR, null);
      // }
   }

}
