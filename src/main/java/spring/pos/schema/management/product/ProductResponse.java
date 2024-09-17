package spring.pos.schema.management.product;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

   @Schema(description = "HTTP status code", example = "200")
   private int status;

   @Schema(description = "Product status message", example = "OK")
   private String message;

   @Schema(description = "Data of the product")
   private ProductData data;

   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public static class ProductData {

      @Schema(description = "ID of the product", example = "1")
      private Long productId;

      @Schema(description = "Name of the product", example = "mie goreng")
      private String name;

      @Schema(description = "Price of the product", example = "10000")
      private Integer price;

      @Schema(description = "Stock of the product", example = "10")
      private Integer stock;

      @Schema(description = "Created at", example = "2022-01-01T00:00:00.000Z")
      private LocalDateTime createdAt;

      @Schema(description = "Updated at", example = "2022-01-01T00:00:00.000Z")
      private LocalDateTime updatedAt;

      @Schema(description = "Data of the user")
      private CreatedBy createdBy;

      @Data
      @AllArgsConstructor
      @NoArgsConstructor
      public static class CreatedBy {
         @Schema(description = "ID of the agent", example = "10")
         private Long agentId;

         @Schema(description = "Full name of the user", example = "John Doe")
         private String fullName;
      }
   }

}
