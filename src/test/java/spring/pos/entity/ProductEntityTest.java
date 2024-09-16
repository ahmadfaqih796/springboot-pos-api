package spring.pos.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import spring.pos.model.product.ProductEntity;

public class ProductEntityTest {

   @BeforeEach
   public void setUp() {
   }

   @Test
   public void testProductEntity() {
      ProductEntity product = new ProductEntity();

      product.setProductId(1L);
      product.setName("Mie Goreng");
      product.setPrice(100);
      product.setStock(10);
      product.setCreatedAt(null);
      product.setUpdatedAt(null);
      product.setUserEntity(null);

      assert product.getProductId() == 1L;
      assert product.getName().equals("Mie Goreng");
      assert product.getPrice() == 100;
      assert product.getStock() == 10;
      assert product.getCreatedAt() == null;
      assert product.getUpdatedAt() == null;
      assert product.getUserEntity() == null;
   }
}
