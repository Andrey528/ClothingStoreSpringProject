package com.clothingstore.ClothingStorePaymentServer.services;

import com.clothingstore.ClothingStorePaymentServer.dtos.PaymentInvoice;
import com.clothingstore.ClothingStorePaymentServer.exceptions.BalanceException;
import com.clothingstore.ClothingStorePaymentServer.models.BankAccount;
import com.clothingstore.ClothingStorePaymentServer.models.User;
import com.clothingstore.ClothingStorePaymentServer.repositories.AccountRepository;
import com.clothingstore.ClothingStorePaymentServer.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PaymentService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final AccountRepository accountRepository;

    @Transactional
    public void transaction(PaymentInvoice paymentInvoice) {
        // Извлекаем из переданных данных банковский аккаунт пользователя по его username
        BankAccount account = userRepository.findByUsername(paymentInvoice.getCustomerUsername()).getBankAccount();

        if (account.getBalance().compareTo(paymentInvoice.getSum()) < 0)
            throw new BalanceException();

        account.setBalance(
                account.getBalance().subtract(paymentInvoice.getSum())
        );
    }

    @Transactional
    public void rollbackTransaction(PaymentInvoice paymentInvoice) {
        // Извлекаем из переданных данных банковский аккаунт пользователя по его username
        BankAccount account = userRepository.findByUsername(paymentInvoice.getCustomerUsername()).getBankAccount();

        account.setBalance(
                account.getBalance().add(paymentInvoice.getSum())
        );
    }
}
