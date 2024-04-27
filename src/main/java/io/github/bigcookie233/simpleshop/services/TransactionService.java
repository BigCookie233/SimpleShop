package io.github.bigcookie233.simpleshop.services;

import io.github.bigcookie233.simpleshop.entities.Transaction;
import io.github.bigcookie233.simpleshop.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction findTransactionByTransactionId(String transactionId) {
        return this.transactionRepository.findByTransactionId(transactionId);
    }

    public void saveTransaction(Transaction transaction) {
        this.transactionRepository.save(transaction);
    }
}
