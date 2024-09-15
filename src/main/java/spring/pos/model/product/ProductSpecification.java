package spring.pos.model.product;

import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {

   public static Specification<ProductEntity> containsKeyword(String keyword) {
      return (root, query, builder) -> {
         String pattern = "%" + keyword.toLowerCase() + "%";

         var roleJoin = root.join("roleEntity");

         return builder.or(
               builder.like(builder.lower(root.get("fullName")), pattern),
               builder.like(builder.lower(root.get("position")), pattern),
               builder.like(builder.toString(root.get("agentId")), pattern),
               builder.like(builder.lower(roleJoin.get("name")), pattern));
      };
   }

}
