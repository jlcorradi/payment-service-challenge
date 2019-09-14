package com.finreach.paymentservice.domain;

import com.finreach.paymentservice.payment.PaymentStatus;

public class PaymentException extends BusinessException {

    public static final String INVALID_STATUS = "The operation can not be performed because of payment current state: (%s)";
    public static final String SAME_ACCOUNT_NOT_ALLOWED = "Source account and destination account must not be the same";
    public static final String INSUFFICIENT_FUNDS = "Account does not have enough funds";
    public static final String AMOUNT_MUST_NOT_BE_NEGATIVE = "Amount must not be negative";

    private PaymentException(String message) {
        super(message);
    }

    public static PaymentException sameAccountNotAllowed() {
        return new PaymentException(SAME_ACCOUNT_NOT_ALLOWED);
    }

    public static PaymentException insufficientFunds() {
        return new PaymentException(INSUFFICIENT_FUNDS);
    }

    public static PaymentException invalidPaymentStatus(PaymentStatus paymentStatus) {
        return new PaymentException(String.format(INVALID_STATUS, paymentStatus));
    }

    public static PaymentException negativeAmount() {
        return new PaymentException(AMOUNT_MUST_NOT_BE_NEGATIVE);
    }
}
