package com.finreach.paymentservice.store;

import com.finreach.paymentservice.payment.Payment;

import java.util.HashMap;
import java.util.Optional;

public class Payments {

    private static final HashMap<String, Payment> PAYMENTS = new HashMap<>();

    public static void createOrUpdate(final Payment payment) {
        PAYMENTS.put(payment.getId(), payment);
    }

    public static Optional<Payment> get(String id) {
        return Optional.ofNullable(PAYMENTS.get(id));
    }

}
