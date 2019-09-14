package com.finreach.paymentservice.store;

import com.finreach.paymentservice.domain.Transaction;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class Transactions {

    private static final Map<String, Transaction> TRANSACTIONS = new HashMap<>();

    static Transaction create(String account, Double amount) {
        final String id = String.valueOf(System.nanoTime());
        final Date created = Date.from(Instant.now());
        final Transaction transaction = new Transaction(id, account, amount, created);
        TRANSACTIONS.put(id, transaction);

        return transaction;
    }

}
