package com.clothingstore.ClothingStorePaymentServer.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "bank_account")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_number", nullable = false)
    private Long cardNumber;

    @Column(name = "balance")
    private BigDecimal balance;
}
