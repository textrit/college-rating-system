package com.example.demo.service;

import com.example.demo.entity.Review;
import com.example.demo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    // Add Review
    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }

    // Get Reviews By College Id
    public List<Review> getReviewsByCollege(Long collegeId) {
        return reviewRepository.findByCollegeId(collegeId);
    }
}