package lk.ac.kln.unimart.repository;

import lk.ac.kln.unimart.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
