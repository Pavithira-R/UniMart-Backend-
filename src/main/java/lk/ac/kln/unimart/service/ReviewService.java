package lk.ac.kln.unimart.service;

import lk.ac.kln.unimart.common.exception.ConflictException;
import lk.ac.kln.unimart.common.exception.ResourceNotFoundException;
import lk.ac.kln.unimart.dto.ReviewCreateRequest;
import lk.ac.kln.unimart.dto.ReviewResponse;
import lk.ac.kln.unimart.dto.ReviewUpdateRequest;
import lk.ac.kln.unimart.entity.Order;
import lk.ac.kln.unimart.entity.OrderStatus;
import lk.ac.kln.unimart.entity.Review;
import lk.ac.kln.unimart.repository.ListingRepository;
import lk.ac.kln.unimart.repository.OrderRepository;
import lk.ac.kln.unimart.repository.ReviewRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final ListingRepository listingRepository;

    public ReviewService(ReviewRepository reviewRepository, OrderRepository orderRepository, ListingRepository listingRepository) {
        this.reviewRepository = reviewRepository;
        this.orderRepository = orderRepository;
        this.listingRepository = listingRepository;
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByListing(Long listingId) {
        if (!listingRepository.existsById(listingId)) {
            throw new ResourceNotFoundException("Listing not found with id " + listingId);
        }
        return reviewRepository.findByListingId(listingId)
                .stream()
                .map(ReviewResponse::new)
                .toList();
    }

    public ReviewResponse createReview(ReviewCreateRequest request, Long userId) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + request.getOrderId()));

        // Check if the authenticated user is the buyer of the order
        if (!order.getBuyer().getId().equals(userId)) {
            throw new AccessDeniedException("You are not the buyer of this order");
        }

        // Check if the order status is COMPLETED
        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new ConflictException("Review requires a COMPLETED order");
        }

        // Check if review already exists for this order (enforce single review per order)
        if (reviewRepository.existsByOrderId(request.getOrderId())) {
            throw new ConflictException("A review already exists for this order");
        }

        Review review = new Review();
        review.setOrder(order);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review saved = reviewRepository.save(review);
        return new ReviewResponse(saved);
    }

    public ReviewResponse updateReview(Long id, ReviewUpdateRequest request, Long userId) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + id));

        // Check if the authenticated user is the reviewer (order buyer)
        if (!review.getOrder().getBuyer().getId().equals(userId)) {
            throw new AccessDeniedException("You do not own this review");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review saved = reviewRepository.save(review);
        return new ReviewResponse(saved);
    }

    public void deleteReview(Long id, Long userId) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + id));

        // Check if the authenticated user is the reviewer (order buyer)
        if (!review.getOrder().getBuyer().getId().equals(userId)) {
            throw new AccessDeniedException("You do not own this review");
        }

        reviewRepository.delete(review);
    }
}
