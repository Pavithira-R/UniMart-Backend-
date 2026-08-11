package lk.ac.kln.unimart.repository;

import lk.ac.kln.unimart.entity.Listing;
import lk.ac.kln.unimart.entity.ListingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ListingRepository extends JpaRepository<Listing, Long> {

    @Query("SELECT l FROM Listing l WHERE " +
           "(:status IS NULL OR l.status = :status) AND " +
           "(:categoryId IS NULL OR l.category.id = :categoryId) AND " +
           "(:q IS NULL OR LOWER(l.title) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(l.description) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Listing> searchListings(
            @Param("status") ListingStatus status,
            @Param("categoryId") Long categoryId,
            @Param("q") String q,
            Pageable pageable
    );
}
