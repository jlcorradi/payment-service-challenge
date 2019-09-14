package com.finreach.paymentservice.domain;


import java.util.ArrayList;
import java.util.List;

public class Account {
    private String id;
    private Double balance = 0d;
    private List<Transaction> transactions = new ArrayList<>();

    public Account(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void updateBalance(Double amount) {
        balance = Double.sum(balance, amount);
    }

    @Override
    public String toString() {
        return id;
    }
}
