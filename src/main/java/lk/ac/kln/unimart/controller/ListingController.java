package lk.ac.kln.unimart.controller;

import jakarta.validation.Valid;
import lk.ac.kln.unimart.dto.ListingCreateRequest;
import lk.ac.kln.unimart.dto.ListingResponse;
import lk.ac.kln.unimart.dto.ListingUpdateRequest;
import lk.ac.kln.unimart.service.ListingService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/listings")
public class ListingController {

    private final ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @GetMapping
    public ResponseEntity<Page<ListingResponse>> getListings(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(listingService.getListings(q, categoryId, status, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListingResponse> getListingById(@PathVariable Long id) {
        return ResponseEntity.ok(listingService.getListingById(id));
    }

    @PostMapping
    public ResponseEntity<ListingResponse> createListing(
            @Valid @RequestBody ListingCreateRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long sellerId = ((Number) jwt.getClaims().get("userId")).longValue();
        return ResponseEntity.status(HttpStatus.CREATED).body(listingService.createListing(request, sellerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListingResponse> updateListing(
            @PathVariable Long id,
            @Valid @RequestBody ListingUpdateRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long sellerId = ((Number) jwt.getClaims().get("userId")).longValue();
        return ResponseEntity.ok(listingService.updateListing(id, request, sellerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListing(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Long sellerId = ((Number) jwt.getClaims().get("userId")).longValue();
        listingService.deleteListing(id, sellerId);
        return ResponseEntity.noContent().build();
    }
}
