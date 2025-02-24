package com.rakhimov.homework.repository;

import com.rakhimov.homework.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
