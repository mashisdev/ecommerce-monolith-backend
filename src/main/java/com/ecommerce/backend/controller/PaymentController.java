package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.payment.CreatePaymentRequest;
import com.ecommerce.backend.dto.payment.CreatePaymentResponse;
import com.ecommerce.backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/paypal")
public class PaymentController {

    @Value("${APPLICATION_URL}")
    private String applicationUrl;

    private final PaymentService paypalService;

    @PostMapping("/create-payment")
    public ResponseEntity<CreatePaymentResponse> createPayment(@RequestBody CreatePaymentRequest request) {
        try {
            Payment payment = paypalService.createPayment(request.getOrderId());
            for (com.paypal.api.payments.Links link : payment.getLinks()) {
                if ("approval_url".equals(link.getRel())) {
                    return ResponseEntity.ok(new CreatePaymentResponse(link.getHref()));
                }
            }
            return ResponseEntity.badRequest().body(new CreatePaymentResponse("Approval URL not found."));
        } catch (PayPalRESTException e) {
            return ResponseEntity.status(500).body(new CreatePaymentResponse("Error creating payment: " + e.getMessage()));
        }
    }

    @GetMapping("/success")
    public RedirectView paymentSuccess(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            paypalService.executePayment(paymentId, payerId);
            return new RedirectView(applicationUrl + "/success-page");
        } catch (PayPalRESTException e) {
            return new RedirectView(applicationUrl + "/error-page");
        }
    }

    @GetMapping("/cancel")
    public RedirectView paymentCancel() {
        return new RedirectView(applicationUrl + "/cancel-page");
    }
}
