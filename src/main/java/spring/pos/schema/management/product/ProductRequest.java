package spring.pos.schema.management.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ProductRequest {

   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public static class CreateRequest {
      @Schema(description = "Name of the product", example = "mie goreng")
      private String name;

      @Schema(description = "Price of the product", example = "10000")
      private Integer price;

      @Schema(description = "Stock of the product", example = "10")
      private Integer stock;
   }

   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public static class UpdateRequest {

      @Schema(description = "Updated name of the product", example = "mie goreng pedas")
      private String name;

      @Schema(description = "Updated price of the product", example = "12000")
      private Integer price;

      @Schema(description = "Updated stock of the product", example = "15")
      private Integer stock;
   }

   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public static class DeleteRequest {
      @Schema(description = "ID of the product to delete", example = "1")
      private Long productId;
   }
}