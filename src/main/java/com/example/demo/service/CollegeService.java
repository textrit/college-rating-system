package com.example.demo.service;

import com.example.demo.entity.College;
import com.example.demo.repository.CollegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollegeService {

    @Autowired
    private CollegeRepository collegeRepository;

    public College addCollege(College college) {
        return collegeRepository.save(college);
    }

    public List<College> getAllColleges() {
        return collegeRepository.findAll();
    }

    public College getCollegeById(Long id) {
        return collegeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("College not found with id: " + id));
    }

    public List<College> searchColleges(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return collegeRepository.findAll();
        }
        return collegeRepository.findByNameContainingIgnoreCase(keyword);
    }

    public List<College> getTopRatedColleges() {
        List<College> allColleges = collegeRepository.findAll();
        
        return allColleges.stream()
                .sorted((c1, c2) -> Double.compare(c2.getAverageRating(), c1.getAverageRating()))
                .limit(5)
                .collect(Collectors.toList());
    }
}