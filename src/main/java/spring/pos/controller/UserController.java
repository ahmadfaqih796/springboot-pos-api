package spring.pos.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import spring.pos.model.user.UserEntity;
import spring.pos.model.user.UserRepository;
import spring.pos.model.user.UserSpecification;
import spring.pos.util.JwtResponseHandler;

@RestController
@RequestMapping("/users")
public class UserController {
   @Autowired
   private UserRepository userRepository;

   @Autowired
   private JwtResponseHandler jwtResponseHandler;

   @GetMapping
   public ResponseEntity<Map<String, Object>> findAll(
         @RequestHeader("Authorization") String token,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size,
         @RequestParam(value = "sortField", defaultValue = "agentId") String sortField,
         @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
         @RequestParam(value = "keyword", defaultValue = "") String keyword) {

      Sort sort = Sort.by(sortField);
      if (sortDir.equals("asc")) {
         sort = sort.ascending();
      } else {
         sort = sort.descending();
      }
      Pageable pageable = PageRequest.of(page, size, sort);
      Page<UserEntity> users;
      if (keyword.isEmpty()) {
         users = userRepository.findAll(pageable);
      } else {
         Specification<UserEntity> spec = UserSpecification.containsKeyword(keyword);
         users = userRepository.findAll(spec, pageable);
      }
      return jwtResponseHandler.handleToken(token, buildResponseData(users));
   }

   private Map<String, Object> buildResponseData(Page<UserEntity> users) {
      Map<String, Object> response = new HashMap<>();
      response.put("content", users.getContent());
      response.put("totalElements", users.getTotalElements());
      response.put("totalPages", users.getTotalPages());
      response.put("size", users.getSize());
      response.put("number", users.getNumber());
      return response;
   }

}
