package com.finreach.paymentservice.api;

import com.finreach.paymentservice.api.request.CreateAccount;
import com.finreach.paymentservice.domain.Account;
import com.finreach.paymentservice.store.Accounts;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @PostMapping
    public ResponseEntity<Account> create(@RequestBody CreateAccount request) {
        final String id = Accounts.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Accounts.get(id).get());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Account> get(@PathVariable("id") String id) {
        final Optional<Account> accountOpt = Accounts.get(id);
        if (!accountOpt.isPresent()) {
            return ResponseEntity.notFound()
                    .build();
        }

        return ResponseEntity.ok(accountOpt.get());
    }

}
