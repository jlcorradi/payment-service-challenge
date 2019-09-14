package com.finreach.paymentservice.store;

import com.finreach.paymentservice.api.request.CreateAccount;
import com.finreach.paymentservice.domain.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Accounts {

    private static final Map<String, Account> ACCOUNTS = new HashMap<>();

    public static String create(CreateAccount request) {
        final String id = String.valueOf(System.nanoTime());
        final Account account = new Account(id);
        account.setBalance(request.getBalance());
        ACCOUNTS.put(id, account);
        return id;
    }

    public static void transaction(String id, Double amount) {
        final Optional<Account> accountOpt = get(id);
        if (!accountOpt.isPresent()) {
            return;
        }

        Account account = accountOpt.get();
        account.addTransaction(Transactions.create(id, amount));
        account.updateBalance(amount);
        ACCOUNTS.put(id, account);
    }

    public static Optional<Account> get(String id) {
        return Optional.ofNullable(ACCOUNTS.get(id));
    }

    public static List<Account> all() {
        return new ArrayList<>(ACCOUNTS.values());
    }

}
