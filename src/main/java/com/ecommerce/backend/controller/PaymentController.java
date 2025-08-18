package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.payment.CreatePaymentRequest;
import com.ecommerce.backend.dto.payment.CreatePaymentResponse;
import com.ecommerce.backend.entity.user.UserEntity;
import com.ecommerce.backend.service.PaymentService;
import com.paypal.api.payments.Links;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/paypal")
public class PaymentController {

    @Value("${APPLICATION_URL}")
    private String applicationUrl;
    @Value("${FRONTEND_URL}")
    private String frontendUrl;

    private final PaymentService paypalService;

    @PostMapping("/create-payment")
    public ResponseEntity<CreatePaymentResponse> createPayment(@RequestBody CreatePaymentRequest request,
                                                               @AuthenticationPrincipal UserEntity user) {
        log.info("Received request to create payment for order ID: {}", request.getOrderId());
        try {
            String returnUrl = applicationUrl + "/api/v1/paypal/success";
            String cancelUrl = applicationUrl + "/api/v1/paypal/cancel";

            log.info("Constructing returnUrl: {}", returnUrl);
            log.info("Constructing cancelUrl: {}", cancelUrl);

            Payment payment = paypalService.createPayment(
                    request.getOrderId(),
                    returnUrl,
                    cancelUrl,
                    user
            );

            for (Links link : payment.getLinks()) {
                if ("approval_url".equals(link.getRel())) {
                    log.info("Successfully created PayPal payment session. Approval URL: {}", link.getHref());
                    return ResponseEntity.ok(new CreatePaymentResponse(link.getHref()));
                }
            }
            log.error("Approval URL not found in PayPal response for order ID: {}", request.getOrderId());
            return ResponseEntity.badRequest().body(new CreatePaymentResponse("Approval URL not found."));
        } catch (PayPalRESTException e) {
            log.error("PayPal REST exception during payment creation for order ID {}: {}", request.getOrderId(), e.getMessage(), e);
            return ResponseEntity.status(500).body(new CreatePaymentResponse("Error creating payment: " + e.getMessage()));
        }
    }

    @GetMapping("/success")
    public RedirectView paymentSuccess(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        log.info("Redirected from PayPal after a successful payment. paymentId: {}, PayerID: {}", paymentId, payerId);
        try {
            paypalService.executePayment(paymentId, payerId);
            log.info("Payment successfully executed. Redirecting to frontend success page.");
            return new RedirectView(frontendUrl + "/success-page");
        } catch (PayPalRESTException e) {
            log.error("PayPal REST exception during payment execution: {}", e.getMessage(), e);
            return new RedirectView(frontendUrl + "/error-page");
        }
    }

    @GetMapping("/cancel")
    public RedirectView paymentCancel() {
        log.info("Redirected from PayPal after payment cancellation. Redirecting to frontend cancel page.");
        return new RedirectView(frontendUrl + "/cancel-page");
    }
}