package lk.ac.kln.unimart.controller;

import jakarta.validation.Valid;
import lk.ac.kln.unimart.dto.ReviewCreateRequest;
import lk.ac.kln.unimart.dto.ReviewResponse;
import lk.ac.kln.unimart.dto.ReviewUpdateRequest;
import lk.ac.kln.unimart.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/listings/{listingId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getReviewsByListing(@PathVariable Long listingId) {
        return ResponseEntity.ok(reviewService.getReviewsByListing(listingId));
    }

    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponse> createReview(
            @Valid @RequestBody ReviewCreateRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long userId = ((Number) jwt.getClaims().get("userId")).longValue();
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(request, userId));
    }

    @PutMapping("/reviews/{id}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewUpdateRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long userId = ((Number) jwt.getClaims().get("userId")).longValue();
        return ResponseEntity.ok(reviewService.updateReview(id, request, userId));
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long userId = ((Number) jwt.getClaims().get("userId")).longValue();
        reviewService.deleteReview(id, userId);
        return ResponseEntity.noContent().build();
    }
}
