package com.finreach.paymentservice.payment;

import com.finreach.paymentservice.api.request.CreatePayment;
import com.finreach.paymentservice.domain.Account;
import com.finreach.paymentservice.domain.EntityNotFoundException;
import com.finreach.paymentservice.domain.PaymentException;
import com.finreach.paymentservice.store.Accounts;
import com.finreach.paymentservice.store.Payments;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PrimaryPaymentService implements PaymentService {

    @Override
    public Payment create(CreatePayment createPayment) {
        Account source = Accounts.get(createPayment.getSourceAccountId()).orElseThrow(
                () -> new EntityNotFoundException("Account", createPayment.getSourceAccountId()));
        Account destination = Accounts.get(createPayment.getDestinationAccountId()).orElseThrow(
                () -> new EntityNotFoundException("Account", createPayment.getDestinationAccountId()));

        // Performing validations
        if (createPayment.getAmount() < 0) {
            throw PaymentException.negativeAmount();
        }

        if (source.equals(destination)) {
            throw PaymentException.sameAccountNotAllowed();
        }

        // Creating payment
        Payment payment = new Payment();
        payment.setId(String.valueOf(System.nanoTime()));
        payment.setCreatedAt(LocalDateTime.now());
        payment.setLastModifiedAt(LocalDateTime.now());
        payment.setSourceAccountId(source.getId());
        payment.setDestinationAccountId(destination.getId());
        payment.setAmount(createPayment.getAmount());
        payment.setState(PaymentState.CREATED);

        Payments.createOrUpdate(payment);
        return payment;
    }

    @Override
    public Payment execute(String id) {
        Payment payment = Payments.get(id).orElseThrow(() -> new EntityNotFoundException("Payment", id));
        Account source = Accounts.get(payment.getSourceAccountId()).orElseThrow(
                () -> new EntityNotFoundException("Account", payment.getSourceAccountId()));
        Account destination = Accounts.get(payment.getDestinationAccountId()).orElseThrow(
                () -> new EntityNotFoundException("Account", payment.getDestinationAccountId()));

        // Performing Validations
        if (PaymentState.CREATED != payment.getState()) {
            throw PaymentException.invalidPaymentStatus(payment.getState());
        }

        if (source.getBalance() - payment.getAmount() < 0) {
            // Marking payment as rejected due to insufficient funds
            payment.setState(PaymentState.REJECTED);
        } else {
            // Generating transactions
            Accounts.transaction(source.getId(), payment.getAmount() * -1);
            Accounts.transaction(destination.getId(), payment.getAmount());
            payment.setState(PaymentState.EXECUTED);
        }

        return payment;
    }

    @Override
    public Payment cancel(String id) {
        Payment payment = Payments.get(id).orElseThrow(() -> new EntityNotFoundException("Payment", id));

        // Performing validations
        if (PaymentState.CREATED != payment.getState()) {
            throw PaymentException.invalidPaymentStatus(payment.getState());
        }

        payment.setState(PaymentState.CANCELED);
        return payment;
    }

    @Override
    public Payment get(String id) {
        return Payments.get(id).orElseThrow(() -> new EntityNotFoundException("Payment", id));
    }
}
