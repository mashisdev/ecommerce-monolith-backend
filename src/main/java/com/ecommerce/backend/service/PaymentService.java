package com.ecommerce.backend.service;

import com.ecommerce.backend.entity.order.Order;
import com.ecommerce.backend.entity.order.OrderStatus;
import com.ecommerce.backend.entity.user.UserEntity;
import com.ecommerce.backend.exception.order.InvalidOrderStatusException;
import com.ecommerce.backend.exception.resource.ResourceNotFoundException;
import com.ecommerce.backend.exception.resource.UnauthorizedActionException;
import com.ecommerce.backend.repository.OrderRepository;
import com.paypal.api.payments.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${APPLICATION_URL}")
    private String applicationUrl;

    private final OrderRepository orderRepository;
    private final APIContext apiContext;

    public Payment createPayment(
            UUID orderId,
            String returnUrl,
            String cancelUrl,
            UserEntity user
    ) throws PayPalRESTException {
        log.info("Starting payment creation for orderId: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order not found with ID: {}", orderId);
                    return new ResourceNotFoundException("Order not found with id: " + orderId);
                });
        log.info("Order found. Total price: {}", order.getTotalPrice());

        if (!order.getUser().equals(user)) {
            log.error("User {} attempted to access an order that does not belong to them. Order ID: {}", user.getEmail(), orderId);
            throw new UnauthorizedActionException("You are not authorized to perform this action on this order.");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            log.error("Order with ID {} is not in PENDING status. Current status: {}", orderId, order.getStatus());
            throw new InvalidOrderStatusException("Cannot create a payment for an order that is not in PENDING status. Current status is " + order.getStatus());
        }

        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(order.getTotalPrice().toString());

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("Payment for Order ID: " + order.getOrderTrackingNumber());
        transaction.setCustom(orderId.toString());

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl(returnUrl);
        redirectUrls.setCancelUrl(cancelUrl);

        payment.setRedirectUrls(redirectUrls);

        log.info("Attempting to create PayPal payment session...");
        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        log.info("Executing payment for paymentId: {} and payerId: {}", paymentId, payerId);

        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment executedPayment = payment.execute(apiContext, paymentExecution);
        log.info("PayPal payment state: {}", executedPayment.getState());

        if ("approved".equalsIgnoreCase(executedPayment.getState())) {
            Transaction transaction = executedPayment.getTransactions().get(0);
            UUID orderId = UUID.fromString(transaction.getCustom());
            log.info("Payment approved. Updating order with ID: {}", orderId);

            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> {
                        log.error("Order not found after successful payment with ID: {}", orderId);
                        return new IllegalArgumentException("Order not found with id: " + orderId);
                    });

            order.setStatus(OrderStatus.PAID);
            orderRepository.save(order);
            log.info("Order with ID: {} updated to PAID status.", orderId);
        }

        return executedPayment;
    }
}