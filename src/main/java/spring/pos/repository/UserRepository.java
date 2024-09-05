package spring.pos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.pos.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

   Optional<User> findByUsername(String username);

}
