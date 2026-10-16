package com.isaiah.Church.Management.System.controller;

import com.isaiah.Church.Management.System.model.Payment;
import com.isaiah.Church.Management.System.service.PaymentService;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/member-payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // ==========================================
    // Make Payment
    // ==========================================

    @PostMapping
    public ResponseEntity<?> makePayment(
            @RequestBody PaymentRequest request,
            HttpSession session) {

        // Get logged-in member
        Integer memberId =
                (Integer) session.getAttribute("memberId");

        if (memberId == null) {
            return ResponseEntity
                    .status(401)
                    .body("Please login first.");
        }

        // Validate contribution ID
        if (request.getContributionId() == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Contribution ID is required.");
        }

        try {

            Payment saved = paymentService.createPayment(
                    request.getContributionId(),
                    request.getMethod(),
                    memberId
            );

            Map<String, Object> response =
                    new HashMap<>();

            response.put(
                    "paymentId",
                    saved.getPaymentId()
            );

            response.put(
                    "contributionId",
                    saved.getContribution()
                            .getContributionId()
            );

            response.put(
                    "amount",
                    saved.getAmount()
            );

            response.put(
                    "method",
                    saved.getMethod()
            );

            response.put(
                    "status",
                    saved.getStatus()
            );

            response.put(
                    "transactionReference",
                    saved.getTransactionReference()
            );

            response.put(
                    "phoneNumber",
                    saved.getPhoneNumber()
            );

            response.put(
                    "createdAt",
                    saved.getCreatedAt()
            );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }


    // ==========================================
    // Update Payment Status
    // ==========================================

   
   
    // Check payment status directly with MTN
    @PostMapping("/{paymentId}/refresh-status")
    public ResponseEntity<?> refreshPaymentStatus(
            @PathVariable Integer paymentId,
            HttpSession session) {

        Integer memberId =
                (Integer) session.getAttribute("memberId");

        if (memberId == null) {
            return ResponseEntity
                    .status(401)
                    .body("Please login first.");
        }

        // STEP 1: Find the payment before contacting MTN.
        Payment existingPayment =
                paymentService.getPaymentById(paymentId);

        if (existingPayment == null) {
            return ResponseEntity
                    .status(404)
                    .body("Payment not found.");
        }

        // STEP 2: Verify ownership before contacting MTN.
        if (existingPayment.getContribution() == null ||
                existingPayment.getContribution().getMember() == null ||
                !existingPayment.getContribution().getMember()
                        .getMemberId().equals(memberId)) {

            return ResponseEntity
                    .status(403)
                    .body("You cannot access this payment.");
        }

        // STEP 3: Only now contact MTN to refresh the status.
        try {
            Payment updated =
                    paymentService.refreshPaymentStatus(paymentId);

            Map<String, Object> response = new HashMap<>();
            response.put("paymentId", updated.getPaymentId());
            response.put("status", updated.getStatus());
            response.put("provider", updated.getProvider());
            response.put(
                    "transactionReference",
                    updated.getTransactionReference());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        } catch (IllegalStateException e) {
            return ResponseEntity
                    .internalServerError()
                    .body(e.getMessage());
        }
    }



    // ==========================================
    // Payment Request
    // ==========================================

    public static class PaymentRequest {

        private Integer contributionId;

        private String method;


        public Integer getContributionId() {

            return contributionId;
        }


        public void setContributionId(
                Integer contributionId) {

            this.contributionId =
                    contributionId;
        }


        public String getMethod() {

            return method;
        }


        public void setMethod(String method) {

            this.method = method;
        }
    }
}