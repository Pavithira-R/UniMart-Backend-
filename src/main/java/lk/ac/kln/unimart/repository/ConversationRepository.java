package lk.ac.kln.unimart.repository;

import lk.ac.kln.unimart.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
