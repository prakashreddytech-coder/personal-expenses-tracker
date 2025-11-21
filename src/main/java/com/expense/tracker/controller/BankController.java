package com.expense.tracker.controller;

import com.expense.tracker.entity.Bank;
import com.expense.tracker.service.BankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/banks")
@Tag(name = "Bank Management", description = "APIs for managing Bank accounts")
public class BankController {

    @Autowired
    private BankService bankService;

    @PostMapping
    @Operation(summary = "Add a new bank")
    public ResponseEntity<Bank> addBank(@Valid @RequestBody Bank bank, @RequestParam Long userId) {
        return ResponseEntity.ok(bankService.addBank(bank, userId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update bank details")
    public ResponseEntity<Bank> updateBank(@PathVariable Long id, @RequestBody Bank bank) {
        return ResponseEntity.ok(bankService.updateBank(id, bank));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a bank")
    public ResponseEntity<Void> deleteBank(@PathVariable Long id) {
        bankService.deleteBank(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all banks for a user")
    public ResponseEntity<List<Bank>> getBanksByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(bankService.getBanksByUserId(userId));
    }
}
