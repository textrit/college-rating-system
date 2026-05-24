package com.example.demo.controller;

import com.example.demo.entity.Review;
import com.example.demo.entity.User;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    // Get all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
    
    // Ban/Unban user
    @PutMapping("/users/{userId}/ban")
    public ResponseEntity<?> toggleBanUser(@PathVariable Long userId, @RequestBody Map<String, Boolean> request) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        user.setBanned(request.get("banned"));
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "User ban status updated"));
    }
    
    // Delete any review (admin power)
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        reviewRepository.deleteById(reviewId);
        return ResponseEntity.ok(Map.of("message", "Review deleted successfully"));
    }
    
    // Get all reviews (admin view)
    @GetMapping("/reviews")
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewRepository.findAll());
    }
    
    // Get dashboard stats
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalReviews", reviewRepository.count());
        stats.put("totalColleges", 0);
        stats.put("activeUsers", userRepository.findAll().stream()
            .filter(u -> !u.isBanned()).count());
        return ResponseEntity.ok(stats);
    }
}
