package com.finreach.paymentservice.api.request;

public class CreateAccount {
    private Double balance;

    public CreateAccount() {
    }

    public CreateAccount(Double balance) {
        this.balance = balance;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
