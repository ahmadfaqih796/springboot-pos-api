package spring.pos.model.role;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDTO {

   private Long roleId;
   private String name;
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;

   public RoleDTO() {
   }

   public RoleDTO(RoleEntity roleEntity, String[] fields) {
      Map<String, Boolean> fieldsMap = new HashMap<>();
      Arrays.stream(fields).forEach(field -> fieldsMap.put(field, true));

      if (fieldsMap.containsKey("roleId")) {
         this.roleId = roleEntity.getRoleId();
      }

      if (fieldsMap.containsKey("name")) {
         this.name = roleEntity.getName();
      }
      if (fieldsMap.containsKey("createdAt")) {
         this.createdAt = roleEntity.getCreatedAt();
      }
      if (fieldsMap.containsKey("updatedAt")) {
         this.updatedAt = roleEntity.getUpdatedAt();
      }
   }

   public LocalDateTime getCreatedAt() {
      return createdAt;
   }

   public void setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
   }

   public LocalDateTime getUpdatedAt() {
      return updatedAt;
   }

   public void setUpdatedAt(LocalDateTime updatedAt) {
      this.updatedAt = updatedAt;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Long getRoleId() {
      return roleId;
   }

   public void setRoleId(Long roleId) {
      this.roleId = roleId;
   }
}
