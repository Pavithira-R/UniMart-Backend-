package lk.ac.kln.unimart.repository;

import lk.ac.kln.unimart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUniversityEmail(String email);
    boolean existsByUniversityEmail(String email);
}
