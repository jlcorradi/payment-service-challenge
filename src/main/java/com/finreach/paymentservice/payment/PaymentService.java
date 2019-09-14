package com.finreach.paymentservice.payment;

import com.finreach.paymentservice.api.request.CreatePayment;

public interface PaymentService {

    Payment create(CreatePayment createPayment);

    Payment execute(String id);

    Payment cancel(String id);

    Payment get(String id);
}
