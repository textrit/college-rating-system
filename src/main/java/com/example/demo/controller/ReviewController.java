package com.example.demo.controller;

import com.example.demo.entity.College;
import com.example.demo.entity.Review;
import com.example.demo.entity.User;
import com.example.demo.repository.CollegeRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CollegeRepository collegeRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserRepository userRepository;

    // Add review to a college
    @PostMapping("/{collegeId}")
    public ResponseEntity<?> addReview(
            @PathVariable Long collegeId,
            @RequestBody Review review,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        try {
            College college = collegeRepository.findById(collegeId)
                    .orElseThrow(() -> new RuntimeException("College not found with id: " + collegeId));
            
            review.setCollege(college);
            
            // Get username from token if available
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String username = jwtUtil.extractUsername(token);
                review.setUserName(username);
            }
            
            Review savedReview = reviewRepository.save(review);
            return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all reviews for a college
    @GetMapping("/college/{collegeId}")
    public ResponseEntity<List<Review>> getReviewsByCollege(@PathVariable Long collegeId) {
        List<Review> reviews = reviewRepository.findByCollegeId(collegeId);
        return ResponseEntity.ok(reviews);
    }
    
    // Get all reviews
    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return ResponseEntity.ok(reviews);
    }
    
    // Update review (only by the user who wrote it)
    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long reviewId,
            @RequestBody Map<String, Object> requestBody,
            @RequestHeader("Authorization") String authHeader) {
        
        try {
            System.out.println("Update review request received for ID: " + reviewId);
            
            Review existingReview = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));
            
            // Get username from token
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            System.out.println("Username from token: " + username);
            System.out.println("Review owner: " + existingReview.getUserName());
            
            // Check if user owns this review
            if (!existingReview.getUserName().equals(username)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "You can only edit your own reviews");
                return ResponseEntity.status(403).body(error);
            }
            
            // Get rating and comment from request
            Integer rating = (Integer) requestBody.get("rating");
            String comment = (String) requestBody.get("comment");
            
            if (rating != null) {
                existingReview.setRating(rating);
            }
            if (comment != null) {
                existingReview.setComment(comment);
            }
            
            Review saved = reviewRepository.save(existingReview);
            System.out.println("Review updated successfully");
            return ResponseEntity.ok(saved);
            
        } catch (Exception e) {
            System.err.println("Error updating review: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    // Delete review (by owner or admin)
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String authHeader) {
        
        try {
            System.out.println("Delete review request received for ID: " + reviewId);
            
            Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));
            
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            User user = userRepository.findByUsername(username).orElse(null);
            
            System.out.println("Username from token: " + username);
            System.out.println("Review owner: " + review.getUserName());
            System.out.println("User role: " + (user != null ? user.getRole() : "null"));
            
            // Allow if owner or admin
            if (!review.getUserName().equals(username) && 
                (user == null || !"ADMIN".equals(user.getRole()))) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "You can only delete your own reviews");
                return ResponseEntity.status(403).body(error);
            }
            
            reviewRepository.deleteById(reviewId);
            System.out.println("Review deleted successfully");
            Map<String, String> response = new HashMap<>();
            response.put("message", "Review deleted successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Error deleting review: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}