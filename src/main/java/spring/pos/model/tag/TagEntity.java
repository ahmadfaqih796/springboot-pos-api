package spring.pos.model.tag;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import spring.pos.model.product.ProductEntity;

@Entity
@Table(name = "tag")
public class TagEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id", nullable = false)
   private Long tagId;

   @Column(name = "name", length = 100, nullable = false, unique = true)
   private String name;

   @ManyToMany(mappedBy = "tags")
   private Set<ProductEntity> products = new HashSet<>();

   public TagEntity() {
   }

   public TagEntity(Long tagId, String name, Set<ProductEntity> products) {
      this.tagId = tagId;
      this.name = name;
      this.products = products;
   }

   public Long getTagId() {
      return tagId;
   }

   public void setTagId(Long tagId) {
      this.tagId = tagId;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Set<ProductEntity> getProducts() {
      return products;
   }

   public void setProducts(Set<ProductEntity> products) {
      this.products = products;
   }

}
