package com.finreach.paymentservice.api;

import com.finreach.paymentservice.api.request.CreatePayment;
import com.finreach.paymentservice.domain.BusinessException;
import com.finreach.paymentservice.domain.EntityNotFoundException;
import com.finreach.paymentservice.payment.Payment;
import com.finreach.paymentservice.payment.PaymentService;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final PaymentResourceAssembler paymentResourceAssembler;
    private final PaymentService paymentService;

    public PaymentController(PaymentResourceAssembler paymentResourceAssembler, PaymentService paymentService) {
        this.paymentResourceAssembler = paymentResourceAssembler;
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Resource<Payment>> create(@RequestBody CreatePayment request) {
        final Payment payment = paymentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentResourceAssembler.toResource(payment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource<Payment>> get(@PathVariable("id") String id) {
        return ResponseEntity.ok(paymentResourceAssembler.toResource(paymentService.get(id)));
    }

    @PutMapping("/execute/{id}")
    public ResponseEntity<Resource<Payment>> execute(@PathVariable("id") String id) {
        final Payment payment = paymentService.execute(id);
        return ResponseEntity.ok(paymentResourceAssembler.toResource(payment));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<Resource<Payment>> cancel(@PathVariable("id") String id) {
        final Payment payment = paymentService.cancel(id);
        return ResponseEntity.ok(paymentResourceAssembler.toResource(payment));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException ex) {
        HttpStatus status = ex instanceof EntityNotFoundException ?  HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(ex.getMessage());
    }
}
