package com.clothingstore.ClothingStorePaymentServer.repositories;

import com.clothingstore.ClothingStorePaymentServer.models.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<BankAccount, Long> {
    BankAccount findByCardNumber(Long cardNumber);
}
