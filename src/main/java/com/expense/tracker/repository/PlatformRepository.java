package com.expense.tracker.repository;

import com.expense.tracker.entity.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlatformRepository extends JpaRepository<Platform, Long> {
    List<Platform> findByUserId(Long userId);
}
