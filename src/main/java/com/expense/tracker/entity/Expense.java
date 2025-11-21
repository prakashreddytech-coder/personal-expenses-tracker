package com.expense.tracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Data
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @Column(nullable = false)
    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be positive")
    private BigDecimal amount;

    @Column(nullable = false)
    @NotBlank(message = "Type is required")
    private String type; // "DEBIT", "CREDIT"

    @Column(nullable = false)
    @NotBlank(message = "Mode is required")
    private String mode; // "BANK", "CARD"

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private CreditCard card;

    @ManyToOne
    @JoinColumn(name = "platform_id")
    private Platform platform;

    @Column(nullable = false)
    @NotNull(message = "Date and Time is required")
    private LocalDateTime dateTime;
}
