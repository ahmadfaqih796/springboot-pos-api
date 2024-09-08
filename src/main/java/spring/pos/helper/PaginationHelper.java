package spring.pos.helper;

import java.util.HashMap;
import java.util.List;
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

   public static <T> Map<String, Object> responseDisabledPagination(List<T> content) {
      Map<String, Object> response = new HashMap<>();
      response.put("content", content);
      response.put("totalElements", content.size());
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

   public static <T> Object fetchData(
         int page,
         int size,
         String sortField,
         String sortDir,
         boolean disabledPagination,
         Specification<T> spec,
         JpaSpecificationExecutor<T> repository) {
      Sort sort = Sort.by(sortField);
      if (sortDir.equalsIgnoreCase("asc")) {
         sort = sort.ascending();
      } else {
         sort = sort.descending();
      }

      if (disabledPagination) {
         return repository.findAll(spec, sort);
      } else {
         Pageable pageable = PageRequest.of(page, size, sort);
         return repository.findAll(spec, pageable);
      }
   }

   @SuppressWarnings("unchecked")
   public static <T> Map<String, Object> buildResponse(
         int page,
         int size,
         String sortField,
         String sortDir,
         boolean disabledPagination,
         Specification<T> spec,
         JpaSpecificationExecutor<T> repository) {
      if (disabledPagination) {
         List<T> allData = (List<T>) fetchData(page, size, sortField, sortDir, true, spec, repository);
         return responseDisabledPagination(allData);
      } else {
         Page<T> paginatedData = (Page<T>) fetchData(page, size, sortField, sortDir, false, spec, repository);
         return responsePagination(paginatedData);
      }
   }
}