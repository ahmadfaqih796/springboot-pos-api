package spring.pos.model.product;

import java.util.List;
import java.util.stream.Collectors;

import spring.pos.schema.management.product.ProductResponse;

public class ProductMapper {

      public static ProductResponse.ProductData toResponse(ProductEntity productEntity) {
            ProductResponse.ProductData productData = new ProductResponse.ProductData();
            productData.setProductId(productEntity.getProductId());
            productData.setName(productEntity.getName());
            productData.setPrice(productEntity.getPrice());
            productData.setStock(productEntity.getStock());
            productData.setCreatedAt(productEntity.getCreatedAt());
            productData.setUpdatedAt(productEntity.getUpdatedAt());

            // Check if UserEntity is null
            if (productEntity.getUserEntity() != null) {
                  // Convert UserEntity to CreatedBy
                  ProductResponse.ProductData.CreatedBy createdBy = new ProductResponse.ProductData.CreatedBy(
                              productEntity.getUserEntity().getAgentId(),
                              productEntity.getUserEntity().getFullName());
                  productData.setCreatedBy(createdBy);
            } else {
                  // Handle case where UserEntity is null
                  productData.setCreatedBy(null); // or provide a default CreatedBy object if necessary
            }

            // Convert Set<TagEntity> to List<TagData>
            List<ProductResponse.ProductData.TagData> tagDataList = productEntity.getTags().stream()
                        .map(tag -> new ProductResponse.ProductData.TagData(tag.getTagId(), tag.getName()))
                        .collect(Collectors.toList());
            productData.setTags(tagDataList);

            return productData;
      }

}
