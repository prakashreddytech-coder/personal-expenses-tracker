package com.expense.tracker.service.impl;

import com.expense.tracker.entity.Bank;
import com.expense.tracker.entity.CreditCard;
import com.expense.tracker.entity.Expense;
import com.expense.tracker.repository.BankRepository;
import com.expense.tracker.repository.CreditCardRepository;
import com.expense.tracker.repository.ExpenseRepository;
import com.expense.tracker.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public Map<String, Object> getDashboardStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();

        List<Bank> banks = bankRepository.findByUserId(userId);
        List<CreditCard> cards = creditCardRepository.findByUserId(userId);

        // 1. Total Amount from Banks
        BigDecimal totalBankBalance = banks.stream()
                .map(b -> b.getCurrentBalance() != null ? b.getCurrentBalance() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("totalBankBalance", totalBankBalance);

        // 2. Total Amount from Credit Cards (Available)
        BigDecimal totalCardLimit = cards.stream()
                .map(c -> c.getTotalLimit() != null ? c.getTotalLimit() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCardUsed = cards.stream()
                .map(c -> c.getUsedLimit() != null ? c.getUsedLimit() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCardAvailable = totalCardLimit.subtract(totalCardUsed);
        stats.put("totalCardAvailable", totalCardAvailable);

        // 3. Current Balance of each Bank
        stats.put("banks", banks);

        // 4. Current Available Limit of each Card
        // We can just return the cards list, frontend can calculate available or we can enrich it.
        // For simplicity, returning cards list.
        stats.put("cards", cards);

        // 5. Current Month Analytics
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        List<Expense> expenses = expenseRepository.findByUserId(userId); // Should optimize to filter by date in DB
        
        BigDecimal monthlyBankSpend = BigDecimal.ZERO;
        BigDecimal monthlyCardSpend = BigDecimal.ZERO;

        for (Expense expense : expenses) {
            if (expense.getDateTime() != null && expense.getAmount() != null &&
                    expense.getDateTime().isAfter(startOfMonth) && "DEBIT".equalsIgnoreCase(expense.getType())) {
                if ("BANK".equalsIgnoreCase(expense.getMode())) {
                    monthlyBankSpend = monthlyBankSpend.add(expense.getAmount());
                } else if ("CARD".equalsIgnoreCase(expense.getMode())) {
                    monthlyCardSpend = monthlyCardSpend.add(expense.getAmount());
                }
            }
        }

        stats.put("monthlyBankSpend", monthlyBankSpend);
        stats.put("monthlyCardSpend", monthlyCardSpend);
        stats.put("monthlyTotalSpend", monthlyBankSpend.add(monthlyCardSpend));

        // 6. Recent Transactions (Top 5)
        List<Expense> recentExpenses = expenses.stream()
                .sorted((e1, e2) -> e2.getDateTime().compareTo(e1.getDateTime()))
                .limit(5)
                .toList();
        stats.put("recentExpenses", recentExpenses);

        return stats;
    }
}
