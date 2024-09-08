package spring.pos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import spring.pos.model.role.RoleEntity;
import spring.pos.service.RoleService;
import spring.pos.util.JwtResponseHandler;

@WebMvcTest(RoleController.class)
public class RoleControllerTest {

   private MockMvc mockMvc;

   @InjectMocks
   private RoleController roleController;

   @Mock
   private RoleService roleService;

   @Mock
   private JwtResponseHandler jwtResponseHandler;

   @BeforeEach
   public void setup() {
      MockitoAnnotations.openMocks(this);
      mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
   }

   @Test
   public void testFindAllWithPagination() throws Exception {
      Page<RoleEntity> roles = new PageImpl<>(
            Arrays.asList(
                  new RoleEntity(1L, "admin", LocalDateTime.now(), LocalDateTime.now()),
                  new RoleEntity(2L, "spv", LocalDateTime.parse("2024-09-08T02:23:05"),
                        LocalDateTime.parse("2024-09-08T02:23:05"))));
      Map<String, Object> response = new HashMap<>();
      response.put("content", roles.getContent());
      response.put("totalElements", roles.getTotalElements());
      response.put("totalPages", roles.getTotalPages());

      when(roleService.get(0, 10, "roleId", "asc", "",
            false)).thenReturn(response);
      when(jwtResponseHandler.handleToken(any(String.class), any(Map.class)))
            .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

      // Perform GET request
      mockMvc.perform(get("/roles")
            .header("Authorization", "Bearer token")
            .param("page", "0")
            .param("size", "10")
            .param("sortField", "roleId")
            .param("sortDir", "asc"))
            .andExpect(status().isOk());
   }

   @Test
   public void testFindAllWithoutPagination() throws Exception {
      Map<String, Object> response = new HashMap<>();
      Page<RoleEntity> roles = new PageImpl<>(
            Arrays.asList(
                  new RoleEntity(1L, "admin", LocalDateTime.now(), LocalDateTime.now()),
                  new RoleEntity(2L, "spv", LocalDateTime.parse("2024-09-08T02:23:05"),
                        LocalDateTime.parse("2024-09-08T02:23:05"))));
      response.put("content", roles.getContent());
      response.put("totalElements", roles.getTotalElements());

      when(roleService.get(0, 10, "roleId", "asc", "", true)).thenReturn(response);
      when(jwtResponseHandler.handleToken(any(String.class), any(Map.class)))
            .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

      mockMvc.perform(get("/roles")
            .header("Authorization", "Bearer token")
            .param("disabledPagination", "true"))
            .andExpect(status().isOk());
   }

   @Test
   public void testFindAllRoles() throws Exception {
      // Sample data
      Page<RoleEntity> roles = new PageImpl<>(
            Arrays.asList(
                  new RoleEntity(1L, "admin", LocalDateTime.parse("2024-09-08T02:23:05"),
                        LocalDateTime.parse("2024-09-08T02:23:05")),
                  new RoleEntity(2L, "spv", LocalDateTime.parse("2024-09-08T02:23:05"),
                        LocalDateTime.parse("2024-09-08T02:23:05"))));

      Map<String, Object> response = new HashMap<>();
      response.put("content", roles.getContent());
      response.put("totalElements", roles.getTotalElements());
      response.put("totalPages", roles.getTotalPages());
      response.put("size", roles.getSize());

      when(roleService.get(0, 10, "roleId", "asc", "",
            false))
            .thenReturn(response);

      mockMvc.perform(get("/roles")
            .header("Authorization", "Bearer token")
            .param("page", "0")
            .param("size", "10")
            .param("sortField", "roleId")
            .param("sortDir", "asc"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.message").value("Successfully Get Data"))
            .andExpect(jsonPath("$.data.content[0].roleId").value(1))
            .andExpect(jsonPath("$.data.content[1].roleId").value(2))
            .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
   }
}