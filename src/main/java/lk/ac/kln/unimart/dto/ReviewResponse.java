package lk.ac.kln.unimart.dto;

import lk.ac.kln.unimart.entity.Review;
import java.time.Instant;

public class ReviewResponse {
    private Long id;
    private Long orderId;
    private Long listingId;
    private String listingTitle;
    private Long reviewerId;
    private String reviewerName;
    private Integer rating;
    private String comment;
    private Instant createdAt;
    private Instant updatedAt;

    public ReviewResponse() {}

    public ReviewResponse(Review review) {
        this.id = review.getId();
        this.orderId = review.getOrder().getId();
        this.listingId = review.getOrder().getListing().getId();
        this.listingTitle = review.getOrder().getListing().getTitle();
        this.reviewerId = review.getOrder().getBuyer().getId();
        this.reviewerName = review.getOrder().getBuyer().getFullName();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    public String getListingTitle() {
        return listingTitle;
    }

    public void setListingTitle(String listingTitle) {
        this.listingTitle = listingTitle;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
