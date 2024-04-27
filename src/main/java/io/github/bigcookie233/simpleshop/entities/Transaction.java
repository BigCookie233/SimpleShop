package io.github.bigcookie233.simpleshop.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(generator = "transactionIdGenerator")
    @GenericGenerator(name = "transactionIdGenerator", strategy = "io.github.bigcookie233.simpleshop.generators.TransactionIdGenerator")
    private String transactionId;
    @ManyToOne
    private Product product;
    private double amount;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    public Transaction() {
    }

    public Transaction(Product product, double amount) {
        this.product = product;
        this.amount = amount;
        this.status = TransactionStatus.PENDING;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Product getProduct() {
        return product;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}
