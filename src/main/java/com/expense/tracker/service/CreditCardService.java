package com.expense.tracker.service;

import com.expense.tracker.entity.CreditCard;
import java.util.List;
import java.util.Optional;

public interface CreditCardService {
    CreditCard addCreditCard(CreditCard card, Long userId);
    CreditCard updateCreditCard(Long id, CreditCard card);
    void deleteCreditCard(Long id);
    List<CreditCard> getCreditCardsByUserId(Long userId);
    Optional<CreditCard> getCreditCardById(Long id);
}
