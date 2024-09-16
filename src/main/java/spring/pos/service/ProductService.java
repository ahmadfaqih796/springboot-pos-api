package spring.pos.service;

import java.util.List;
import java.util.Map;

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
import spring.pos.model.user.UserEntity;
import spring.pos.model.user.UserRepository;
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
   private UserRepository userRepository;

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

   public ResponseEntity<ProductResponse> create(ProductRequest req) {
      try {
         ProductEntity payload = new ProductEntity();
         payload.setName(req.getName());
         payload.setPrice(req.getPrice());
         payload.setStock(req.getStock());
         payload.setCreatedAt(req.getCreatedAt());
         payload.setUpdatedAt(req.getUpdatedAt());
         if (req.getUserId() != null) {
            UserEntity userEntity = userRepository.findById(req.getUserId())
                  .orElseThrow(() -> new IllegalArgumentException(
                        "user not found"));
            payload.setUserEntity(userEntity);
         }
         ProductEntity res = productRepository.save(payload);

         ProductData data = new ProductData();
         CreatedBy createdBy = null;
         if (res.getUserEntity() != null) {
            createdBy = new CreatedBy(
                  res.getUserEntity().getAgentId(),
                  res.getUserEntity().getFullName());
         }
         data.setProductId(res.getProductId());
         data.setName(res.getName());
         data.setPrice(res.getPrice());
         data.setStock(res.getStock());
         data.setCreatedAt(res.getCreatedAt());
         data.setUpdatedAt(res.getUpdatedAt());
         data.setCreatedBy(createdBy);
         return ResponseHandler.generateResponse("success create product", HttpStatus.OK, data);
      } catch (Exception e) {
         throw new IllegalArgumentException(e.getMessage());
      }
   }

}
