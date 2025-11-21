package com.expense.tracker.service.impl;

import com.expense.tracker.entity.Bank;
import com.expense.tracker.entity.CreditCard;
import com.expense.tracker.entity.Expense;
import com.expense.tracker.entity.User;
import com.expense.tracker.repository.BankRepository;
import com.expense.tracker.repository.CreditCardRepository;
import com.expense.tracker.repository.ExpenseRepository;
import com.expense.tracker.repository.UserRepository;
import com.expense.tracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Override
    @Transactional
    public Expense addExpense(Expense expense, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        expense.setUser(user);

        // Update balances
        updateBalances(expense, false);

        return expenseRepository.save(expense);
    }

    @Override
    @Transactional
    public Expense updateExpense(Long id, Expense expenseDetails) {
        Expense existingExpense = expenseRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense not found"));
        
        // Reverse old transaction effect
        updateBalances(existingExpense, true);

        // Update fields
        existingExpense.setTitle(expenseDetails.getTitle());
        existingExpense.setDescription(expenseDetails.getDescription());
        existingExpense.setAmount(expenseDetails.getAmount());
        existingExpense.setType(expenseDetails.getType());
        existingExpense.setMode(expenseDetails.getMode());
        existingExpense.setBank(expenseDetails.getBank());
        existingExpense.setCard(expenseDetails.getCard());
        existingExpense.setPlatform(expenseDetails.getPlatform());
        existingExpense.setDateTime(expenseDetails.getDateTime());

        // Apply new transaction effect
        updateBalances(existingExpense, false);

        return expenseRepository.save(existingExpense);
    }

    @Override
    @Transactional
    public void deleteExpense(Long id) {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense not found"));
        
        // Reverse transaction effect
        updateBalances(expense, true);

        expenseRepository.delete(expense);
    }

    @Override
    public List<Expense> getExpensesByUserId(Long userId) {
        return expenseRepository.findByUserId(userId);
    }

    @Override
    public List<Expense> searchExpenses(Long userId, java.time.LocalDate startDate, java.time.LocalDate endDate, String type, String mode, String platformName) {
        java.time.LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        java.time.LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(23, 59, 59) : null;
        
        // Convert empty strings to null for proper query handling
        String typeParam = (type != null && !type.trim().isEmpty()) ? type : null;
        String modeParam = (mode != null && !mode.trim().isEmpty()) ? mode : null;
        String platformParam = (platformName != null && !platformName.trim().isEmpty()) ? platformName : null;
        
        return expenseRepository.searchExpenses(userId, startDateTime, endDateTime, typeParam, modeParam, platformParam);
    }

    @Override
    public List<Expense> getExpensesByBankId(Long bankId) {
        return expenseRepository.findByBankId(bankId);
    }

    @Override
    public List<Expense> getExpensesByCardId(Long cardId) {
        return expenseRepository.findByCardId(cardId);
    }

    private void updateBalances(Expense expense, boolean reverse) {
        BigDecimal amount = expense.getAmount();
        if (reverse) {
            // If reversing, we negate the amount logic effectively
            // But simpler to just think: if it was Debit, we Credit back.
            // Or just negate the amount passed to the logic below? 
            // No, logic depends on Type.
            // Let's handle it by checking Type and Mode.
        }

        if ("BANK".equalsIgnoreCase(expense.getMode())) {
            Bank bank = bankRepository.findById(expense.getBank().getId())
                    .orElseThrow(() -> new RuntimeException("Bank not found"));
            
            if ("DEBIT".equalsIgnoreCase(expense.getType())) {
                // Debit from Bank -> reduces balance
                if (reverse) {
                    bank.setCurrentBalance(bank.getCurrentBalance().add(amount));
                } else {
                    bank.setCurrentBalance(bank.getCurrentBalance().subtract(amount));
                }
            } else if ("CREDIT".equalsIgnoreCase(expense.getType())) {
                // Credit to Bank -> increases balance
                if (reverse) {
                    bank.setCurrentBalance(bank.getCurrentBalance().subtract(amount));
                } else {
                    bank.setCurrentBalance(bank.getCurrentBalance().add(amount));
                }
            }
            bankRepository.save(bank);

        } else if ("CARD".equalsIgnoreCase(expense.getMode())) {
            CreditCard card = creditCardRepository.findById(expense.getCard().getId())
                    .orElseThrow(() -> new RuntimeException("Card not found"));

            if ("DEBIT".equalsIgnoreCase(expense.getType())) {
                // Debit from Card -> increases used limit
                if (reverse) {
                    card.setUsedLimit(card.getUsedLimit().subtract(amount));
                } else {
                    card.setUsedLimit(card.getUsedLimit().add(amount));
                }
            } else if ("CREDIT".equalsIgnoreCase(expense.getType())) {
                // Credit to Card (Payment) -> decreases used limit
                if (reverse) {
                    card.setUsedLimit(card.getUsedLimit().add(amount));
                } else {
                    card.setUsedLimit(card.getUsedLimit().subtract(amount));
                }
            }
            creditCardRepository.save(card);
        }
    }
}
