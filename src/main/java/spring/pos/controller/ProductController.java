package spring.pos.controller;

import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import spring.pos.model.product.ProductEntity;
import spring.pos.model.product.ProductSpecification;
import spring.pos.schema.management.product.ProductRequest;
import spring.pos.schema.management.product.ProductResponse;
import spring.pos.service.ProductService;
import spring.pos.util.ResponseHandler;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {

   @Autowired
   private ProductService productService;

   @GetMapping("/all")
   public ResponseEntity<List<ProductEntity>> getAllProducts(
         @RequestParam(value = "keyword", defaultValue = "") String keyword) {
      // Implement logic to return all roles
      return ResponseEntity.ok(productService.getAll(keyword));
   }

   @GetMapping()
   public ResponseEntity<ProductResponse> getProducts(
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size,
         @RequestParam(value = "sortField", defaultValue = "productId") String sortField,
         @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
         @RequestParam(value = "keyword", defaultValue = "") String keyword,
         @RequestParam(value = "disabledPagination", defaultValue = "false") boolean disabledPagination) {

      ProductSpecification.containsKeyword(keyword);
      Map<String, Object> response = productService.get(page, size, sortField, sortDir, keyword, disabledPagination);
      return ResponseHandler.generateResponse("success get products", HttpStatus.OK, response);
   }

   @PostMapping()
   public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest.CreateRequest productRequest) {
      return productService.create(productRequest);
   }

   @PutMapping("/{productId}")
   public ResponseEntity<ProductResponse> updateProduct(
         @PathVariable Long productId,
         @RequestBody ProductRequest.UpdateRequest productRequest) {
      return productService.update(productId, productRequest);
   }

   @DeleteMapping("/{productId}")
   public ResponseEntity<ProductResponse> deleteProduct(@PathVariable Long productId) {
      return productService.delete(productId);
   }

   @GetMapping("/export/excel")
   public ResponseEntity<byte[]> exportProductsToExcel(
         @RequestParam(value = "keyword", defaultValue = "") String keyword) {
      List<ProductEntity> products = productService.getAll(keyword);
      return productService.exportToExcel(products);
   }

   @GetMapping("/export/pdf")
   public ResponseEntity<byte[]> exportProductsToPdf(
         @RequestParam(value = "keyword", defaultValue = "") String keyword) {
      List<ProductEntity> products = productService.getAll(keyword);
      return productService.exportToPdf(products);
   }

}
