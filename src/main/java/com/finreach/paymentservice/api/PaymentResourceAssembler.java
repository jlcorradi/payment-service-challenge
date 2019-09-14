package com.finreach.paymentservice.api;

import com.finreach.paymentservice.payment.Payment;
import com.finreach.paymentservice.payment.PaymentState;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class PaymentResourceAssembler implements ResourceAssembler<Payment, Resource<Payment>> {
    @Override
    public Resource<Payment> toResource(Payment payment) {
        final Resource<Payment> paymentResource =
                new Resource<>(payment, linkTo(methodOn(PaymentController.class).get(payment.getId())).withSelfRel());

        if (PaymentState.CREATED == payment.getState()) {
            paymentResource.add(
                    linkTo(methodOn(PaymentController.class).execute(payment.getId())).withRel("execute"),
                    linkTo(methodOn(PaymentController.class).cancel(payment.getId())).withRel("cancel")
            );
        }

        return paymentResource;
    }
}
