package com.example.demo.repository;

import com.example.demo.entity.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CollegeRepository extends JpaRepository<College, Long> {
    
    List<College> findByNameContainingIgnoreCase(String keyword);
    
    // Optional: Add more useful queries
    List<College> findByLocationIgnoreCase(String location);
    
    @Query("SELECT c FROM College c WHERE c.name LIKE %:name%")
    List<College> searchByName(@Param("name") String name);
}