package lk.ac.kln.unimart.repository;

import lk.ac.kln.unimart.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
