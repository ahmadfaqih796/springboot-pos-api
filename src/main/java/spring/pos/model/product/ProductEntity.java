package spring.pos.model.product;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import spring.pos.model.user.UserEntity;

@Entity
@Table(name = "product")
public class ProductEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long productId;

   @Column(name = "name", length = 100, nullable = false, unique = true)
   private String name;

   @Column(name = "price", nullable = false)
   private Integer price;

   @Column(name = "stock", nullable = false)
   private Integer stock;

   @Column(name = "created_at", nullable = false, updatable = false)
   private LocalDateTime createdAt;

   @Column(name = "updated_at", nullable = false)
   private LocalDateTime updatedAt;

   @ManyToOne
   @JoinColumn(name = "created_by")
   private UserEntity createdBy;

   public ProductEntity() {
   }

   public ProductEntity(Long productId, String name, Integer price, Integer stock, LocalDateTime createdAt,
         LocalDateTime updatedAt, UserEntity createdBy) {
      this.productId = productId;
      this.name = name;
      this.price = price;
      this.stock = stock;
      this.createdAt = createdAt;
      this.updatedAt = updatedAt;
      this.createdBy = createdBy;
   }

   public Long getProductId() {
      return productId;
   }

   public void setProductId(Long productId) {
      this.productId = productId;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Integer getPrice() {
      return price;
   }

   public void setPrice(Integer price) {
      this.price = price;
   }

   public Integer getStock() {
      return stock;
   }

   public void setStock(Integer stock) {
      this.stock = stock;
   }

   public LocalDateTime getCreatedAt() {
      return createdAt;
   }

   public void setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
   }

   public LocalDateTime getUpdatedAt() {
      return updatedAt;
   }

   public void setUpdatedAt(LocalDateTime updatedAt) {
      this.updatedAt = updatedAt;
   }

   public UserEntity getCreatedBy() {
      return createdBy;
   }

   public void setCreatedBy(UserEntity createdBy) {
      this.createdBy = createdBy;
   }

}
