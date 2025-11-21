package com.expense.tracker.service;

import com.expense.tracker.entity.Bank;
import java.util.List;
import java.util.Optional;

public interface BankService {
    Bank addBank(Bank bank, Long userId);
    Bank updateBank(Long id, Bank bank);
    void deleteBank(Long id);
    List<Bank> getBanksByUserId(Long userId);
    Optional<Bank> getBankById(Long id);
}
