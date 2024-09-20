package spring.pos.schema.management.product;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class ProductResponseV2 {

   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public static class Get {
      @Schema(description = "HTTP status code", example = "200")
      private int status;

      @Schema(description = "Product status message", example = "success get products")
      private String message;

      @Schema(description = "Data of the product")
      private PaginationData data;

      @Data
      @AllArgsConstructor
      @NoArgsConstructor
      public static class PaginationData {
         @Schema(description = "Number of the page", example = "0")
         private Integer number;

         @Schema(description = "Size of the page", example = "10")
         private Integer size;

         @Schema(description = "Total number of pages", example = "1")
         private Integer totalPages;

         @Schema(description = "Paginated content of the product")
         private List<ProductData> content;

         @Schema(description = "Total number of data", example = "1")
         private Integer totalElements;
      }

      @Schema(description = "Timestamp of the request", example = "2022-01-01T00:00:00.000Z")
      private LocalDateTime timestamp;

   }

   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public static class Post {
      @Schema(description = "HTTP status code", example = "201")
      private int status;

      @Schema(description = "Product status message", example = "Created")
      private String message;

      @Schema(description = "Details of the created product")
      private ProductData data;
   }

   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public static class Update {
      @Schema(description = "HTTP status code", example = "200")
      private int status;

      @Schema(description = "Product status message", example = "Updated")
      private String message;

      @Schema(description = "Details of the updated product")
      private ProductData data;
   }

   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public static class Delete {
      @Schema(description = "HTTP status code", example = "200")
      private int status;

      @Schema(description = "Product status message", example = "Deleted")
      private String message;
   }

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

      @Schema(description = "List of tags associated with the product")
      private List<TagData> tags;

      @Data
      @AllArgsConstructor
      @NoArgsConstructor
      public static class CreatedBy {
         @Schema(description = "ID of the agent", example = "10")
         private Long agentId;

         @Schema(description = "Full name of the user", example = "John Doe")
         private String fullName;
      }

      @Data
      @AllArgsConstructor
      @NoArgsConstructor
      public static class TagData {
         @Schema(description = "ID of the tag", example = "5")
         private Long tagId;

         @Schema(description = "Name of the tag", example = "Promo")
         private String name;
      }
   }
}
