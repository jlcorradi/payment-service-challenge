package com.finreach.paymentservice.domain;

import java.util.Date;

public class Transaction {
    private String id;
    private String account;
    private Double amount;
    private Date created;

    public Transaction(String id, String account, Double amount, Date created) {
        this.id = id;
        this.account = account;
        this.amount = amount;
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return id;
    }
}
