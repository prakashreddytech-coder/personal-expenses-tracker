package com.expense.tracker.service.impl;

import com.expense.tracker.entity.CreditCard;
import com.expense.tracker.entity.User;
import com.expense.tracker.repository.CreditCardRepository;
import com.expense.tracker.repository.UserRepository;
import com.expense.tracker.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CreditCardServiceImpl implements CreditCardService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public CreditCard addCreditCard(CreditCard card, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        card.setUser(user);
        if (card.getUsedLimit() == null) {
            card.setUsedLimit(java.math.BigDecimal.ZERO);
        }
        return creditCardRepository.save(card);
    }

    @Override
    public CreditCard updateCreditCard(Long id, CreditCard cardDetails) {
        CreditCard card = creditCardRepository.findById(id).orElseThrow(() -> new RuntimeException("Credit Card not found"));
        card.setName(cardDetails.getName());
        if (cardDetails.getTotalLimit() != null) {
            card.setTotalLimit(cardDetails.getTotalLimit());
        }
        return creditCardRepository.save(card);
    }

    @Override
    public void deleteCreditCard(Long id) {
        creditCardRepository.deleteById(id);
    }

    @Override
    public List<CreditCard> getCreditCardsByUserId(Long userId) {
        return creditCardRepository.findByUserId(userId);
    }

    @Override
    public Optional<CreditCard> getCreditCardById(Long id) {
        return creditCardRepository.findById(id);
    }
}
