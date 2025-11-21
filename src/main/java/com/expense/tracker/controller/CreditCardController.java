package com.expense.tracker.controller;

import com.expense.tracker.entity.CreditCard;
import com.expense.tracker.service.CreditCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/credit-cards")
@Tag(name = "Credit Card Management", description = "APIs for managing Credit Cards")
public class CreditCardController {

    @Autowired
    private CreditCardService creditCardService;

    @PostMapping
    @Operation(summary = "Add a new credit card")
    public ResponseEntity<CreditCard> addCreditCard(@Valid @RequestBody CreditCard card, @RequestParam Long userId) {
        return ResponseEntity.ok(creditCardService.addCreditCard(card, userId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update credit card details")
    public ResponseEntity<CreditCard> updateCreditCard(@PathVariable Long id, @RequestBody CreditCard card) {
        return ResponseEntity.ok(creditCardService.updateCreditCard(id, card));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a credit card")
    public ResponseEntity<Void> deleteCreditCard(@PathVariable Long id) {
        creditCardService.deleteCreditCard(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all credit cards for a user")
    public ResponseEntity<List<CreditCard>> getCreditCardsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(creditCardService.getCreditCardsByUserId(userId));
    }
}
