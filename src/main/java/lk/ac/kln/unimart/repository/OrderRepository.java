package lk.ac.kln.unimart.repository;

import lk.ac.kln.unimart.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByIdAndBuyerId(Long id, Long buyerId);
}
