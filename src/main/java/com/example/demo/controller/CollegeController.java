package com.example.demo.controller;

import com.example.demo.entity.College;
import com.example.demo.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colleges")
@CrossOrigin(origins = "http://localhost:3000")
public class CollegeController {

    @Autowired
    private CollegeService collegeService;

    @PostMapping
    public ResponseEntity<College> addCollege(@RequestBody College college) {
        try {
            College savedCollege = collegeService.addCollege(college);
            return new ResponseEntity<>(savedCollege, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<College>> getAllColleges() {
        try {
            List<College> colleges = collegeService.getAllColleges();
            return ResponseEntity.ok(colleges);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<College> getCollegeById(@PathVariable Long id) {
        try {
            College college = collegeService.getCollegeById(id);
            return ResponseEntity.ok(college);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<College>> searchColleges(@RequestParam String keyword) {
        try {
            List<College> colleges = collegeService.searchColleges(keyword);
            return ResponseEntity.ok(colleges);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<College>> getTopRatedColleges() {
        try {
            List<College> colleges = collegeService.getTopRatedColleges();
            return ResponseEntity.ok(colleges);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}