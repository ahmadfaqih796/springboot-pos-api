package spring.pos.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import spring.pos.util.MD5PasswordEncoder;

@Entity
@Table(name = "agent_tc")
public class UserEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long agent_id;

   @Column(name = "username", length = 100, nullable = false, unique = true)
   private String username;

   @Column(name = "password", length = 100, nullable = false)
   private String password;

   @Column(name = "full_name", length = 100, nullable = false)
   private String full_name;

   @Column(name = "position", length = 100)
   private String position;

   @Column(name = "telephone", length = 100)
   private Integer telephone;

   // @ManyToOne
   // @JoinColumn(name = "address_id")
   // private AddressEntity addressEntity;

   public UserEntity() {
   }

   public UserEntity(Long agent_id, String username, String password, String full_name, String position,
         Integer telephone) {
      this.agent_id = agent_id;
      this.username = username;
      this.password = password;
      this.full_name = full_name;
      this.position = position;
      this.telephone = telephone;
   }

   public Long getAgent_id() {
      return agent_id;
   }

   public void setAgent_id(Long agent_id) {
      this.agent_id = agent_id;
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

   public String getFull_name() {
      return full_name;
   }

   public void setFull_name(String full_name) {
      this.full_name = full_name;
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

   // public AddressEntity getAddressEntity() {
   // return addressEntity;
   // }

   // public void setAddressEntity(AddressEntity addressEntity) {
   // this.addressEntity = addressEntity;
   // }

}