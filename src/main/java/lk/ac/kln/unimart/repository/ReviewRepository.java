package lk.ac.kln.unimart.repository;

import lk.ac.kln.unimart.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByOrderId(Long orderId);
    boolean existsByOrderId(Long orderId);

    @Query("SELECT r FROM Review r WHERE r.order.listing.id = :listingId")
    List<Review> findByListingId(@Param("listingId") Long listingId);
}
