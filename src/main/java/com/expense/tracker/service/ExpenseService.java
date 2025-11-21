package com.expense.tracker.service;

import com.expense.tracker.entity.Expense;
import java.util.List;

public interface ExpenseService {
    Expense addExpense(Expense expense, Long userId);
    Expense updateExpense(Long id, Expense expense);
    void deleteExpense(Long id);
    List<Expense> getExpensesByUserId(Long userId);
    List<Expense> searchExpenses(Long userId, java.time.LocalDate startDate, java.time.LocalDate endDate, String type, String mode, String platformName);
    List<Expense> getExpensesByBankId(Long bankId);
    List<Expense> getExpensesByCardId(Long cardId);
}
