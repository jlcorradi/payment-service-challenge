package com.finreach.paymentservice.store;

import com.finreach.paymentservice.api.request.CreateAccount;
import com.finreach.paymentservice.domain.Account;

import java.util.*;

public class Accounts {

    private static final Map<String, Account> ACCOUNTS = new HashMap<>();

    public static String create(CreateAccount request) {
        final String id = String.valueOf(System.nanoTime());
        final Account account = new Account(id);
        account.setBalance(request.getBalance());
        ACCOUNTS.put(id, account);
        return id;
    }

    public static void transaction(String accountId, Double amount) {
        final Optional<Account> accountOpt = get(accountId);
        if (!accountOpt.isPresent()) {
            return;
        }

        Account account = accountOpt.get();
        account.addTransaction(Transactions.create(accountId, amount));
        account.updateBalance(amount);
        ACCOUNTS.put(accountId, account);
    }

    public static Optional<Account> get(String id) {
        return Optional.ofNullable(ACCOUNTS.get(id));
    }

    public static List<Account> all() {
        return new ArrayList<>(ACCOUNTS.values());
    }

}
