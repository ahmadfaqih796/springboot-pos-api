package spring.pos.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.transaction.TransactionScoped;
import spring.pos.helper.PaginationHelper;
import spring.pos.model.product.ProductEntity;
import spring.pos.model.product.ProductRepository;
import spring.pos.model.product.ProductSpecification;

@Service
@TransactionScoped
public class ProductService {

   @Autowired
   private ProductRepository productRepository;

   public Map<String, Object> get(int page, int size, String sortField, String sortDir, String keyword,
         boolean disabledPagination) {
      Specification<ProductEntity> spec = ProductSpecification.containsKeyword(keyword);
      return PaginationHelper.buildResponse(page, size, sortField, sortDir, disabledPagination, spec,
            productRepository);
   }

}
