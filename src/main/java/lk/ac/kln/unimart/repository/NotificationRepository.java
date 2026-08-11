package lk.ac.kln.unimart.repository;

import lk.ac.kln.unimart.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
