package com.finreach.paymentservice.payment;

import com.finreach.paymentservice.api.request.CreatePayment;

public interface PaymentService {

    Payment create(CreatePayment createPayment);

    void execute(String id);

    void cancel(String id);

}
