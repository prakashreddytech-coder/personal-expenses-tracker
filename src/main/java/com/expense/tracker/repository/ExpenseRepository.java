package com.expense.tracker.repository;

import com.expense.tracker.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserId(Long userId);
    List<Expense> findByBankId(Long bankId);
    List<Expense> findByCardId(Long cardId);

    @org.springframework.data.jpa.repository.Query(
            value = "SELECT e.* FROM expenses e " +
                    "LEFT JOIN platforms p ON e.platform_id = p.id " +
                    "WHERE e.user_id = :userId " +
                    "AND (CAST(:startDate AS timestamp) IS NULL OR e.date_time >= CAST(:startDate AS timestamp)) " +
                    "AND (CAST(:endDate AS timestamp) IS NULL OR e.date_time <= CAST(:endDate AS timestamp)) " +
                    "AND (CAST(:type AS varchar) IS NULL OR e.type = CAST(:type AS varchar)) " +
                    "AND (CAST(:mode AS varchar) IS NULL OR e.mode = CAST(:mode AS varchar)) " +
                    "AND (CAST(:platformName AS varchar) IS NULL OR p.name = CAST(:platformName AS varchar))",
            nativeQuery = true)
    List<Expense> searchExpenses(Long userId, java.time.LocalDateTime startDate, java.time.LocalDateTime endDate, String type, String mode, String platformName);
}
