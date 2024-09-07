package spring.pos.model.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

   List<UserEntity> findByUsername(String username);

   Optional<UserEntity> findByUsernameAndPassword(String username, String password);

}
