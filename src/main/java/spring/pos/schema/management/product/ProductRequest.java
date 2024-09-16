package spring.pos.schema.management.product;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
   @Schema(description = "Name of the product", example = "mie goreng")
   private String name;

   @Schema(description = "Price of the product", example = "10000")
   private Integer price;

   @Schema(description = "Stock of the product", example = "10")
   private Integer stock;

   @Schema(description = "Created by user id", example = "1")
   private Long userId;

   @Schema(description = "Created at", example = "2022-01-01T00:00:00.000Z")
   private LocalDateTime createdAt = LocalDateTime.now();

   @Schema(description = "Updated at", example = "2022-01-01T00:00:00.000Z")
   private LocalDateTime updatedAt = LocalDateTime.now();

}
