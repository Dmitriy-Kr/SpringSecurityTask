package edu.java.springsecuritytask.repository;

import edu.java.springsecuritytask.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Long> {
}
