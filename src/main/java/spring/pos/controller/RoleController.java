package spring.pos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import spring.pos.model.role.RoleEntity;
import spring.pos.model.role.RoleRepository;
import spring.pos.service.RoleService;

@RestController
@RequestMapping("/roles")
public class RoleController {

   @Autowired
   private RoleRepository roleRepository;

   @Autowired
   private RoleService roleService;

   @GetMapping()
   public String getAll(@RequestParam String param) {
      Iterable<RoleEntity> roles = roleRepository.findAll();
      return roles.toString();
   }

   @PostMapping()
   public RoleEntity postRole(@RequestBody RoleEntity role) {
      return roleService.create(role);
   }
   // public ResponseEntity<Map<String, Object>>
   // findAll(@RequestHeader("Authorization") String token) {
   // Iterable<RoleEntity> roles = roleRepository.findAll();
   // return jwtResponseHandler.handleToken(token, roles);
   // }

}
