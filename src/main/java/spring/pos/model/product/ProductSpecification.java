package spring.pos.model.product;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.JoinType;

public class ProductSpecification {

   public static Specification<ProductEntity> containsKeyword(String keyword) {
      return (root, query, builder) -> {
         String pattern = "%" + keyword.toLowerCase() + "%";

         var userJoin = root.join("userEntity", JoinType.LEFT);
         var roleJoin = userJoin.join("roleEntity", JoinType.LEFT);

         return builder.or(
               builder.like(builder.toString(root.get("productId")), pattern),
               builder.like(builder.lower(root.get("name")), pattern),
               builder.like(builder.lower(userJoin.get("fullName")), pattern),
               builder.like(builder.toString(roleJoin.get("name")), pattern));
      };
   }

}
