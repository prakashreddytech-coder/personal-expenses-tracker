package com.expense.tracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "credit_cards")
@Data
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @NotBlank(message = "Card name is required")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "Total limit is required")
    @Min(value = 0, message = "Total limit must be positive")
    private BigDecimal totalLimit;

    @Column(nullable = false)
    private BigDecimal usedLimit;
}
