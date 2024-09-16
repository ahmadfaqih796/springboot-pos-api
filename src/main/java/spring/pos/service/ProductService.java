package spring.pos.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.TransactionScoped;
import spring.pos.helper.PaginationHelper;
import spring.pos.model.product.ProductEntity;
import spring.pos.model.product.ProductRepository;
import spring.pos.model.product.ProductSpecification;
import spring.pos.schema.management.product.ProductRequest;
import spring.pos.schema.management.product.ProductResponse;
import spring.pos.schema.management.product.ProductResponse.ProductData;
import spring.pos.schema.management.product.ProductResponse.ProductData.CreatedBy;
import spring.pos.util.ResponseHandler;

@Service
@TransactionScoped
public class ProductService {

   @Autowired
   private ProductRepository productRepository;

   @Autowired
   private SessionService sessionService;

   public List<ProductEntity> getAll(String keyword) {
      Specification<ProductEntity> spec = ProductSpecification.containsKeyword(keyword);
      if (spec != null) {
         return productRepository.findAll(spec);
      }
      return productRepository.findAll();
   }

   public Map<String, Object> get(int page,
         int size,
         String sortField,
         String sortDir,
         String keyword,
         boolean disabledPagination) {
      Specification<ProductEntity> spec = ProductSpecification.containsKeyword(keyword);
      return PaginationHelper.buildResponse(page, size, sortField, sortDir, disabledPagination, spec,
            productRepository);
   }

   public ResponseEntity<ProductResponse> create(ProductRequest.CreateRequest req) {
      try {
         ProductEntity productEntity = new ProductEntity(
               req.getName(),
               req.getPrice(),
               req.getStock(),
               LocalDateTime.now(),
               LocalDateTime.now(),
               sessionService.getUserSession());

         ProductEntity savedProduct = productRepository.save(productEntity);

         CreatedBy createdBy = Optional.ofNullable(savedProduct.getUserEntity())
               .map(user -> new CreatedBy(user.getAgentId(), user.getFullName()))
               .orElse(null);

         ProductData productData = new ProductData(
               savedProduct.getProductId(),
               savedProduct.getName(),
               savedProduct.getPrice(),
               savedProduct.getStock(),
               savedProduct.getCreatedAt(),
               savedProduct.getUpdatedAt(),
               createdBy);

         return ResponseHandler.generateResponse("Product successfully created", HttpStatus.OK, productData);

      } catch (Exception e) {
         throw new IllegalArgumentException(e.getMessage());
      }
   }

   public ResponseEntity<ProductResponse> update(Long productId, ProductRequest.UpdateRequest req) {

      ProductEntity checkedProduct = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

      Optional<ProductEntity> existingProduct = productRepository.findByName(req.getName());
      if (existingProduct.isPresent() && existingProduct.get().getProductId() != productId) {
         throw new IllegalArgumentException("Product name already exists");
      }

      try {
         ProductEntity productEntity = new ProductEntity(
               productId,
               req.getName(),
               req.getPrice(),
               req.getStock(),
               checkedProduct.getCreatedAt(),
               LocalDateTime.now(),
               sessionService.getUserSession());
         ProductEntity updatedProduct = productRepository.save(productEntity);

         CreatedBy createdBy = Optional.ofNullable(updatedProduct.getUserEntity())
               .map(user -> new CreatedBy(user.getAgentId(), user.getFullName()))
               .orElse(null);
         ProductData productData = new ProductData(
               updatedProduct.getProductId(),
               updatedProduct.getName(),
               updatedProduct.getPrice(),
               updatedProduct.getStock(),
               checkedProduct.getCreatedAt(),
               updatedProduct.getUpdatedAt(),
               createdBy);

         return ResponseHandler.generateResponse("Product successfully updated", HttpStatus.OK, productData);
      } catch (Exception e) {
         throw new IllegalArgumentException(e.getMessage());
      }
   }

}
