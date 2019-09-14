package com.finreach.paymentservice.statistics;

import com.finreach.paymentservice.domain.Transaction;
import com.finreach.paymentservice.util.MathUtils;

import java.util.List;

public class Statistic {

    private String accountId;
    private Double maxTrans;
    private Double minTrans;
    private Double avgTrans;
    private List<Transaction> transactions;

    public Statistic(String accountId, Double maxTrans, Double minTrans, Double avgTrans,
                     List<Transaction> transactions) {
        this.accountId = accountId;
        this.maxTrans = maxTrans;
        this.minTrans = minTrans;
        this.avgTrans = MathUtils.round(avgTrans);
        this.transactions = transactions;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Double getMaxTrans() {
        return maxTrans;
    }

    public void setMaxTrans(Double maxTrans) {
        this.maxTrans = maxTrans;
    }

    public Double getMinTrans() {
        return minTrans;
    }

    public void setMinTrans(Double minTrans) {
        this.minTrans = minTrans;
    }

    public Double getAvgTrans() {
        return avgTrans;
    }

    public void setAvgTrans(Double avgTrans) {
        this.avgTrans = avgTrans;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}