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
    private int productAmount;
    private double amount;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    private String minecraftId;

    public Transaction() {
    }

    public Transaction(Product product, int productAmount, double amount, String minecraftId) {
        this.product = product;
        this.productAmount = productAmount;
        this.amount = amount;
        this.minecraftId = minecraftId;
        this.status = TransactionStatus.PENDING;
    }

    public String getMinecraftId() {
        return minecraftId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public int getProductAmount() {
        return productAmount;
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
