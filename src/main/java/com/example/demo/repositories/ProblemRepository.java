package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.models.Problem;
import java.util.List;
import java.util.Optional;


public interface ProblemRepository extends JpaRepository<Problem, Long> {
   Optional<Problem> findById(Long id);
}