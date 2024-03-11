package com.clothingstore.ClothingStorePaymentServer.services;

import com.clothingstore.ClothingStorePaymentServer.dtos.PaymentInvoice;
import com.clothingstore.ClothingStorePaymentServer.exceptions.BalanceException;
import com.clothingstore.ClothingStorePaymentServer.models.BankAccount;
import com.clothingstore.ClothingStorePaymentServer.models.User;
import com.clothingstore.ClothingStorePaymentServer.repositories.AccountRepository;
import com.clothingstore.ClothingStorePaymentServer.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    public void transactionExpectCorrect() {
        User user = new User();
        user.setUsername("testUser");

        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(new BigDecimal(100));

        user.setBankAccount(bankAccount);

        PaymentInvoice paymentInvoice = new PaymentInvoice("testUser", new BigDecimal(55));

        when(userRepository.findByUsername("testUser")).thenReturn(user);

        paymentService.transaction(paymentInvoice);

        verify(userRepository).findByUsername("testUser");

        Assertions.assertEquals(bankAccount.getBalance(),
                new BigDecimal(45));
    }

    @Test
    public void testTransactionInsufficientFunds() {
        User user = new User();
        user.setUsername("testUser");

        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(new BigDecimal(50));

        user.setBankAccount(bankAccount);

        PaymentInvoice paymentInvoice = new PaymentInvoice("testUser", new BigDecimal(100));

        when(userRepository.findByUsername("testUser")).thenReturn(user);

        assertThrows(BalanceException.class, () -> paymentService.transaction(paymentInvoice));
    }

    @Test
    public void testRollbackTransaction() {
        User user = new User();
        user.setUsername("testUser");

        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(new BigDecimal(100));

        user.setBankAccount(bankAccount);

        PaymentInvoice paymentInvoice = new PaymentInvoice("testUser", new BigDecimal(50));

        when(userRepository.findByUsername("testUser")).thenReturn(user);

        paymentService.rollbackTransaction(paymentInvoice);

        verify(userRepository, times(1)).findByUsername("testUser");
    }
}