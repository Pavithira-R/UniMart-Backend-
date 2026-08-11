package lk.ac.kln.unimart.repository;

import lk.ac.kln.unimart.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
