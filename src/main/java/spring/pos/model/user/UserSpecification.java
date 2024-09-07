package spring.pos.model.user;

import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
   public static Specification<UserEntity> containsKeyword(String keyword) {
      return (root, query, builder) -> {
         String pattern = "%" + keyword.toLowerCase() + "%";
         return builder.or(
               builder.like(builder.lower(root.get("full_name")), pattern),
               builder.like(builder.lower(root.get("position")), pattern),
               builder.like(builder.toString(root.get("agent_id")), pattern));
      };
   }
}
