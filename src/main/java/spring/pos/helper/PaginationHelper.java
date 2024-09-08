package spring.pos.helper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public class PaginationHelper {

   public static Pageable createPageable(int page, int size, String sortField, String sortDir) {
      Sort sort = Sort.by(sortField);
      if (sortDir.equalsIgnoreCase("asc")) {
         sort = sort.ascending();
      } else {
         sort = sort.descending();
      }
      return PageRequest.of(page, size, sort);
   }

   public static <T> Map<String, Object> responsePagination(Page<T> pageData) {
      Map<String, Object> response = new HashMap<>();
      response.put("content", pageData.getContent());
      response.put("totalElements", pageData.getTotalElements());
      response.put("totalPages", pageData.getTotalPages());
      response.put("size", pageData.getSize());
      response.put("number", pageData.getNumber());
      return response;
   }

   public static <T> Map<String, Object> responseDisabledPagination(Page<T> pageData) {
      Map<String, Object> response = new HashMap<>();
      response.put("content", pageData.getContent());
      response.put("totalElements", pageData.getTotalElements());
      return response;
   }

   public static <T> Page<T> fetchPageData(Pageable pageable, Specification<T> spec,
         JpaSpecificationExecutor<T> repository,
         boolean hasKeyword, String keyword) {
      if (hasKeyword && keyword != null && !keyword.isEmpty()) {
         return repository.findAll(spec, pageable);
      }
      return repository.findAll(null, pageable);
   }
}