package spring.pos.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import jakarta.transaction.TransactionScoped;
import spring.pos.helper.PaginationHelper;
import spring.pos.model.product.ProductEntity;
import spring.pos.model.product.ProductMapper;
import spring.pos.model.product.ProductRepository;
import spring.pos.model.product.ProductSpecification;
import spring.pos.model.tag.TagEntity;
import spring.pos.model.tag.TagRepository;
import spring.pos.schema.management.product.ProductRequest;
import spring.pos.schema.management.product.ProductResponse;
import spring.pos.schema.management.product.ProductResponseV2;
import spring.pos.schema.management.product.ProductResponse.ProductData;
import spring.pos.schema.management.product.ProductResponse.ProductData.CreatedBy;
import spring.pos.util.ResponseHandler;

@Service
@TransactionScoped
public class ProductService {

   @Autowired
   private ProductRepository productRepository;

   @Autowired
   private TagRepository tagRepository;

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
      return PaginationHelper.buildResponse(page, size, sortField, sortDir,
            disabledPagination, spec,
            productRepository,
            ProductMapper::toResponse);
   }

   public ResponseEntity<ProductResponseV2.Post> create(ProductRequest.CreateRequest req) {
      try {

         Optional<ProductEntity> existingProduct = productRepository.findByName(req.getName());
         if (existingProduct.isPresent()) {
            throw new IllegalArgumentException("Product name already exists");
         }

         Set<TagEntity> tags = new HashSet<>();
         if (req.getTagIds() != null && !req.getTagIds().isEmpty()) {
            List<TagEntity> tagList = tagRepository.findAllById(req.getTagIds());
            tags.addAll(tagList);
         }

         ProductEntity productEntity = new ProductEntity(
               req.getName(),
               req.getPrice(),
               req.getStock(),
               LocalDateTime.now(),
               LocalDateTime.now(),
               sessionService.getUserSession());
         productEntity.setTags(tags);

         ProductEntity savedProduct = productRepository.save(productEntity);
         ProductData productData = responseProductData(savedProduct);

         // CreatedBy createdBy = Optional.ofNullable(savedProduct.getUserEntity())
         // .map(user -> new CreatedBy(user.getAgentId(), user.getFullName()))
         // .orElse(null);

         // // Siapkan data tag untuk response
         // List<ProductResponse.ProductData.TagData> tagDataList =
         // savedProduct.getTags().stream()
         // .map(tag -> new ProductResponse.ProductData.TagData(tag.getTagId(),
         // tag.getName()))
         // .collect(Collectors.toList());

         // ProductData productData = new ProductData(
         // savedProduct.getProductId(),
         // savedProduct.getName(),
         // savedProduct.getPrice(),
         // savedProduct.getStock(),
         // savedProduct.getCreatedAt(),
         // savedProduct.getUpdatedAt(),
         // createdBy,
         // tagDataList);

         return ResponseHandler.generateResponse("Product successfully created", HttpStatus.OK, productData);

      } catch (Exception e) {
         throw new IllegalArgumentException(e.getMessage());
      }
   }

   public ResponseEntity<ProductResponseV2.Update> update(Long productId, ProductRequest.UpdateRequest req) {

      ProductEntity checkedProduct = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

      Optional<ProductEntity> existingProduct = productRepository.findByName(req.getName());
      if (existingProduct.isPresent() && existingProduct.get().getProductId() != productId) {
         throw new IllegalArgumentException("Product name already exists");
      }

      try {
         Set<TagEntity> tags = new HashSet<>();
         if (req.getTagIds() != null && !req.getTagIds().isEmpty()) {
            List<TagEntity> tagList = tagRepository.findAllById(req.getTagIds());
            tags.addAll(tagList);
         }
         ProductEntity productEntity = new ProductEntity(
               productId,
               req.getName(),
               req.getPrice(),
               req.getStock(),
               checkedProduct.getCreatedAt(),
               LocalDateTime.now(),
               sessionService.getUserSession());
         productEntity.setTags(tags);

         ProductEntity updatedProduct = productRepository.save(productEntity);
         ProductData productData = responseProductData(updatedProduct);

         return ResponseHandler.generateResponse("Product successfully updated", HttpStatus.OK, productData);
      } catch (Exception e) {
         throw new IllegalArgumentException(e.getMessage());
      }
   }

   public ResponseEntity<ProductResponseV2.Delete> delete(Long productId) {

      ProductEntity checkedProduct = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

      try {
         productRepository.delete(checkedProduct);
         ProductData productData = responseProductData(checkedProduct);

         return ResponseHandler.generateResponse("Product successfully deleted", HttpStatus.OK, productData);
      } catch (Exception e) {
         throw new IllegalArgumentException(e.getMessage());
      }
   }

   public ProductData responseProductData(ProductEntity productEntity) {
      // Build CreatedBy object
      CreatedBy createdBy = Optional.ofNullable(productEntity.getUserEntity())
            .map(user -> new CreatedBy(user.getAgentId(), user.getFullName()))
            .orElse(null);

      // Build TagData list
      List<ProductResponse.ProductData.TagData> tagDataList = productEntity.getTags().isEmpty()
            ? null
            : productEntity.getTags().stream()
                  .map(tag -> new ProductResponse.ProductData.TagData(tag.getTagId(), tag.getName()))
                  .collect(Collectors.toList());

      // Build and return ProductData
      return new ProductData(
            productEntity.getProductId(),
            productEntity.getName(),
            productEntity.getPrice(),
            productEntity.getStock(),
            productEntity.getCreatedAt(),
            productEntity.getUpdatedAt(),
            createdBy,
            tagDataList);
   }

   public ResponseEntity<byte[]> exportToPdf(List<ProductEntity> products) {
      try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
         PdfWriter writer = new PdfWriter(out);
         Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

         document.add(new Paragraph("Product List"));

         for (ProductEntity product : products) {
            document.add(new Paragraph(
                  "ID: " + product.getProductId() +
                        ", Name: " + product.getName() +
                        ", Price: " + product.getPrice() +
                        ", Stock: " + product.getStock()));
         }

         document.close();
         byte[] bytes = out.toByteArray();

         HttpHeaders headersPdf = new HttpHeaders();
         headersPdf.add("Content-Disposition", "attachment; filename=products.pdf");

         return new ResponseEntity<>(bytes, headersPdf, HttpStatus.OK);

      } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
   }

   public ResponseEntity<byte[]> exportToExcel(List<ProductEntity> products) {
      try (Workbook workbook = new XSSFWorkbook()) {
         Sheet sheet = workbook.createSheet("Products");
         Row headerRow = sheet.createRow(0);

         // Create header
         String[] headers = { "No", "Name", "Price", "Stock" };
         for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
         }

         // Add data rows
         int rowIdx = 1;
         for (ProductEntity product : products) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(rowIdx - 1);
            row.createCell(1).setCellValue(product.getName());
            row.createCell(2).setCellValue(product.getPrice());
            row.createCell(3).setCellValue(product.getStock());
         }

         // Write to byte array
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         workbook.write(out);
         byte[] bytes = out.toByteArray();

         HttpHeaders headersExcel = new HttpHeaders();
         headersExcel.add("Content-Disposition", "attachment; filename=products.xlsx");

         return new ResponseEntity<>(bytes, headersExcel, HttpStatus.OK);

      } catch (IOException e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
   }
}
