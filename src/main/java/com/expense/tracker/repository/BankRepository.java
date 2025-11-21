package com.expense.tracker.repository;

import com.expense.tracker.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BankRepository extends JpaRepository<Bank, Long> {
    List<Bank> findByUserId(Long userId);
}
