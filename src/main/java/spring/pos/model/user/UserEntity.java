package spring.pos.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import spring.pos.model.role.RoleEntity;
import spring.pos.util.MD5PasswordEncoder;

@Entity
@Table(name = "agent_tc")
public class UserEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long agentId;

   @Column(name = "username", length = 100, nullable = false, unique = true)
   private String username;

   @Column(name = "password", length = 100, nullable = false)
   private String password;

   @Column(name = "full_name", length = 100, nullable = false)
   private String fullName;

   @Column(name = "position", length = 100)
   private String position;

   @Column(name = "telephone", length = 100)
   private Integer telephone;

   @ManyToOne
   @JoinColumn(name = "role_id")
   private RoleEntity roleEntity;

   public UserEntity() {
   }

   public UserEntity(Long agentId, String username, String password, String fullName, String position,
         Integer telephone, RoleEntity roleEntity) {
      this.agentId = agentId;
      this.username = username;
      this.password = password;
      this.fullName = fullName;
      this.position = position;
      this.telephone = telephone;
      this.roleEntity = roleEntity;
   }

   public Long getAgentId() {
      return agentId;
   }

   public void setAgentId(Long agentId) {
      this.agentId = agentId;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = MD5PasswordEncoder.encode(password);
   }

   public String getFullName() {
      return fullName;
   }

   public void setFullName(String fullName) {
      this.fullName = fullName;
   }

   public String getPosition() {
      return position;
   }

   public void setPosition(String position) {
      this.position = position;
   }

   public Integer getTelephone() {
      return telephone;
   }

   public void setTelephone(Integer telephone) {
      this.telephone = telephone;
   }

   // public User roleId(String roleId) {
   // setRoleId(roleId);
   // return this;
   // }

   @JsonProperty("roleData")
   public RoleEntity getRoleEntity() {
      if (roleEntity == null) {
         return null;
      }
      return new RoleEntity(roleEntity.getRoleId(), roleEntity.getName());
   }
   // public RoleDTO getRoleDTO() {
   // if (roleEntity == null) {
   // return null;
   // }
   // String[] fields = { "roleId", "name" };
   // return new RoleDTO(roleEntity, fields);
   // }

   public void setRoleId(Long roleId) {
      RoleEntity roleModel = new RoleEntity();
      roleModel.setRoleId(roleId);
      roleModel.getName();
      this.roleEntity = roleModel;
   }

   public void setRoleEntity(RoleEntity roleEntity) {
      this.roleEntity = roleEntity;
   }

}