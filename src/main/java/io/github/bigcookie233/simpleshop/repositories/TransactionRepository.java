package io.github.bigcookie233.simpleshop.repositories;

import io.github.bigcookie233.simpleshop.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Transaction findByTransactionId(String transactionId);
}
