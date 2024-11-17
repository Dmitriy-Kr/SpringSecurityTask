package edu.java.springsecuritytask.repository;

import edu.java.springsecuritytask.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    @Query("SELECT t FROM Trainer t JOIN t.user u WHERE u.username = ?1")
    Optional<Trainer> findByUsername(String username);
}
