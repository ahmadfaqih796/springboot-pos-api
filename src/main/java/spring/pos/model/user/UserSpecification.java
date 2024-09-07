package spring.pos.model.user;

import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
   public static Specification<UserEntity> containsKeyword(String keyword) {
      return (root, query, builder) -> {
         String pattern = "%" + keyword.toLowerCase() + "%";
         return builder.or(
               builder.like(builder.lower(root.get("fullName")), pattern),
               builder.like(builder.lower(root.get("position")), pattern),
               builder.like(builder.toString(root.get("agentId")), pattern));

      };
   }

   public static Specification<UserEntity> containsAgentId(String agentId) {
      return (root, query, builder) -> {
         return builder.equal(root.get("agentId"), agentId);
      };
   }

}
