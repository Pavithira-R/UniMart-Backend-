package lk.ac.kln.unimart.service;

import lk.ac.kln.unimart.common.exception.ResourceNotFoundException;
import lk.ac.kln.unimart.dto.ListingCreateRequest;
import lk.ac.kln.unimart.dto.ListingResponse;
import lk.ac.kln.unimart.dto.ListingUpdateRequest;
import lk.ac.kln.unimart.entity.Category;
import lk.ac.kln.unimart.entity.Listing;
import lk.ac.kln.unimart.entity.ListingStatus;
import lk.ac.kln.unimart.entity.User;
import lk.ac.kln.unimart.repository.CategoryRepository;
import lk.ac.kln.unimart.repository.ListingRepository;
import lk.ac.kln.unimart.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ListingService {

    private final ListingRepository listingRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ListingService(ListingRepository listingRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<ListingResponse> getListings(String q, Long categoryId, String statusStr, int page, int size) {
        // Enforce max page size of 50
        int pageSize = Math.min(size, 50);
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").descending());

        ListingStatus status = null;
        if (statusStr != null && !statusStr.isBlank()) {
            try {
                status = ListingStatus.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status: " + statusStr);
            }
        } else {
            // Default to AVAILABLE listings only if status is not provided
            status = ListingStatus.AVAILABLE;
        }

        String query = (q != null && !q.isBlank()) ? q : null;

        return listingRepository.searchListings(status, categoryId, query, pageable)
                .map(ListingResponse::new);
    }

    @Transactional(readOnly = true)
    public ListingResponse getListingById(Long id) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found with id " + id));
        if (listing.getStatus() == ListingStatus.ARCHIVED) {
            throw new ResourceNotFoundException("Listing is archived");
        }
        return new ListingResponse(listing);
    }

    public ListingResponse createListing(ListingCreateRequest request, Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + request.getCategoryId()));

        Listing listing = new Listing();
        listing.setSeller(seller);
        listing.setCategory(category);
        listing.setTitle(request.getTitle());
        listing.setDescription(request.getDescription());
        listing.setPrice(request.getPrice());
        listing.setStatus(ListingStatus.AVAILABLE);

        Listing saved = listingRepository.save(listing);
        return new ListingResponse(saved);
    }

    public ListingResponse updateListing(Long id, ListingUpdateRequest request, Long sellerId) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found with id " + id));

        if (!listing.getSeller().getId().equals(sellerId)) {
            throw new AccessDeniedException("You do not own this listing");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + request.getCategoryId()));

        listing.setCategory(category);
        listing.setTitle(request.getTitle());
        listing.setDescription(request.getDescription());
        listing.setPrice(request.getPrice());

        try {
            listing.setStatus(ListingStatus.valueOf(request.getStatus().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + request.getStatus());
        }

        Listing saved = listingRepository.save(listing);
        return new ListingResponse(saved);
    }

    public void deleteListing(Long id, Long sellerId) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Listing not found with id " + id));

        if (!listing.getSeller().getId().equals(sellerId)) {
            throw new AccessDeniedException("You do not own this listing");
        }

        // Soft-delete by setting status to ARCHIVED
        listing.setStatus(ListingStatus.ARCHIVED);
        listingRepository.save(listing);
    }
}
