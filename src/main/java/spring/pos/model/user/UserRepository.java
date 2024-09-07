package spring.pos.model.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository
      extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

   List<UserEntity> findByUsername(String username);

   Optional<UserEntity> findByUsernameAndPassword(String username, String password);

}
