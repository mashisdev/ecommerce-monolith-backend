package com.ecommerce.backend.service;

import com.ecommerce.backend.entity.order.Order;
import com.ecommerce.backend.entity.order.OrderStatus;
import com.ecommerce.backend.exception.resource.ResourceNotFoundException;
import com.ecommerce.backend.repository.OrderRepository;
import com.paypal.api.payments.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${APPLICATION_URL}")
    private String applicationUrl;

    private final OrderRepository orderRepository;
    private final APIContext apiContext;

    public Payment createPayment(UUID orderId) throws PayPalRESTException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));

        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(order.getTotalPrice().toString());

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("Payment for Order ID: " + order.getOrderTrackingNumber());

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setReturnUrl(applicationUrl + "/paypal/success?paymentId={paymentId}&PayerID={PayerID}");
        redirectUrls.setCancelUrl(applicationUrl + "/paypal/cancel");

        payment.setRedirectUrls(redirectUrls);
        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        Payment executedPayment = payment.execute(apiContext, paymentExecution);

        if ("approved".equalsIgnoreCase(executedPayment.getState())) {
            Transaction transaction = executedPayment.getTransactions().getFirst();
            UUID orderId = UUID.fromString(transaction.getCustom());

            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

            order.setStatus(OrderStatus.PAID);
            orderRepository.save(order);
        }

        return executedPayment;
    }
}