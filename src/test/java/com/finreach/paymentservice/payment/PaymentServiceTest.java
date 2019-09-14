package com.finreach.paymentservice.payment;

import com.finreach.paymentservice.api.request.CreateAccount;
import com.finreach.paymentservice.api.request.CreatePayment;
import com.finreach.paymentservice.domain.Account;
import com.finreach.paymentservice.domain.BusinessException;
import com.finreach.paymentservice.domain.PaymentException;
import com.finreach.paymentservice.store.Accounts;
import com.finreach.paymentservice.store.Payments;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Test(expected = PaymentException.class)
    public void shouldNotCreatePaymentSameAccount() {
        final String id = Accounts.create(new CreateAccount(100d));
        paymentService.create(new CreatePayment(100d, id, id));
    }

    @Test(expected = BusinessException.class)
    public void shouldNotCreatePaymentInvalidAccount() {
        final String id = Accounts.create(new CreateAccount(100d));
        paymentService.create(new CreatePayment(100d, id, "NONEXISTING"));
    }

    @Test(expected = PaymentException.class)
    public void shouldNotCreatePaymentNegative() {
        final String sourceAccountId = Accounts.create(new CreateAccount(100d));
        final String destinationAccountId = Accounts.create(new CreateAccount(100d));

        try {
            paymentService.create(new CreatePayment(-1d, sourceAccountId, destinationAccountId));
        } catch (PaymentException ex) {
            assertEquals(PaymentException.AMOUNT_MUST_NOT_BE_NEGATIVE, ex.getMessage());
            throw ex;
        }
    }

    @Test
    public void shouldCreatePayment() {
        Payment payment = createPayment(100d);
        assertEquals(PaymentState.CREATED, payment.getState());
        assertNotNull(Payments.get(payment.getId()));
    }

    @Test
    public void shouldNotExecutePaymentInsufficientFund() {
        Payment payment = createPayment(200d);
        final Account sourceAccount = Accounts.get(payment.getSourceAccountId()).orElseThrow(
                () -> new IllegalArgumentException("sourceAccount"));
        sourceAccount.setBalance(199d);
        paymentService.execute(payment.getId());
        assertEquals("Payment has not had it's status set to REJECTED",
                Payments.get(payment.getId()).orElseThrow(() -> new RuntimeException()).getState(),
                PaymentState.REJECTED);
    }

    @Test(expected = PaymentException.class)
    public void shouldNotExecutePaymentCanceledPayment() {
        Payment payment = createPayment(200d);
        paymentService.cancel(payment.getId());

        try {
            paymentService.execute(payment.getId());
        } catch (PaymentException ex) {
            assertEquals(ex.getMessage(), String.format(PaymentException.INVALID_STATUS, payment.getState()));
            throw ex;
        }
    }

    @Test(expected = PaymentException.class)
    public void shouldNotExecutePaymentExecutedPayment() {
        Payment payment = createPayment(200d);
        paymentService.execute(payment.getId());

        try {
            paymentService.execute(payment.getId());
        } catch (PaymentException ex) {
            assertEquals(ex.getMessage(), String.format(PaymentException.INVALID_STATUS, payment.getState()));
            throw ex;
        }
    }

    @Test(expected = PaymentException.class)
    public void shouldNotCancelPaymentExecutedPayment() {
        Payment payment = createPayment(200d);
        paymentService.execute(payment.getId());

        try {
            paymentService.cancel(payment.getId());
        } catch (PaymentException ex) {
            assertEquals(ex.getMessage(), String.format(PaymentException.INVALID_STATUS, payment.getState()));
            throw ex;
        }
    }

    @Test
    public void shouldExecutePayment() {
        Payment payment = createPayment(300d);
        paymentService.execute(payment.getId());
        assertEquals(PaymentState.EXECUTED, payment.getState());
    }

    private Payment createPayment(Double amount) {
        final String accountIdSource = Accounts.create(new CreateAccount(amount));
        final String accountIdDest = Accounts.create(new CreateAccount(0d));
        return paymentService.create(new CreatePayment(amount, accountIdSource, accountIdDest));
    }
}