package spring.pos.model.role;

import org.springframework.data.jpa.domain.Specification;

public class RoleSpecification {

   public static Specification<RoleEntity> containsKeyword(String keyword) {
      return (root, query, builder) -> {
         String pattern = "%" + keyword.toLowerCase() + "%";
         return builder.or(
               builder.like(builder.lower(root.get("name")), pattern));
      };
   }

}
