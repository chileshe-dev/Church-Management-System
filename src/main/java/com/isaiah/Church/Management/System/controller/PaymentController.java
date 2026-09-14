package com.isaiah.Church.Management.System.controller;

import com.isaiah.Church.Management.System.model.Contribution;
import com.isaiah.Church.Management.System.model.Payment;
import com.isaiah.Church.Management.System.repository.ContributionRepository;
import com.isaiah.Church.Management.System.repository.PaymentRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/member-payments")
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final ContributionRepository contributionRepository;


    public PaymentController(
            PaymentRepository paymentRepository,
            ContributionRepository contributionRepository) {

        this.paymentRepository = paymentRepository;
        this.contributionRepository = contributionRepository;
    }


    // ==========================================
    // Make Payment
    // ==========================================

    @PostMapping 

    // ==========================================
// Update Payment Status
// ==========================================

@PutMapping("/{paymentId}/status")
public ResponseEntity<?> updatePaymentStatus(
        @PathVariable Integer paymentId,
        @RequestParam String status) {

    // Find payment
    Payment payment =
            paymentRepository.findById(paymentId).orElse(null);

    if (payment == null) {
        return ResponseEntity
                .badRequest()
                .body("Payment not found.");
    }

    // Validate status
    if (!status.equalsIgnoreCase("PENDING") &&
        !status.equalsIgnoreCase("COMPLETED") &&
        !status.equalsIgnoreCase("FAILED")) {

        return ResponseEntity
                .badRequest()
                .body("Invalid payment status. Use PENDING, COMPLETED or FAILED.");
    }

    // Update status
    payment.setStatus(status.toUpperCase());

    Payment updated =
            paymentRepository.save(payment);

    // Response
    Map<String, Object> response =
            new HashMap<>();

    response.put(
            "paymentId",
            updated.getPaymentId()
    );

    response.put(
            "contributionId",
            updated.getContribution().getContributionId()
    );

    response.put(
            "amount",
            updated.getAmount()
    );

    response.put(
            "method",
            updated.getMethod()
    );

    response.put(
            "status",
            updated.getStatus()
    );

    response.put(
            "transactionReference",
            updated.getTransactionReference()
    );

    response.put(
            "createdAt",
            updated.getCreatedAt()
    );

    return ResponseEntity.ok(response);
}
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


        // Find contribution
        Contribution contribution =
                contributionRepository
                        .findById(request.getContributionId())
                        .orElse(null);


        if (contribution == null) {

            return ResponseEntity
                    .badRequest()
                    .body("Contribution not found.");
        }


        // Security check:
        // Make sure this contribution belongs
        // to the logged-in member.
        if (contribution.getMember() == null ||
                !contribution.getMember()
                        .getMemberId()
                        .equals(memberId)) {

            return ResponseEntity
                    .status(403)
                    .body("You cannot pay for this contribution.");
        }


        // Check if already paid
        if (paymentRepository
                .findByContributionContributionId(
                        contribution.getContributionId()
                )
                .isPresent()) {

            return ResponseEntity
                    .badRequest()
                    .body("This contribution has already been paid.");
        }


        // Validate payment method
        if (request.getMethod() == null ||
                request.getMethod().isBlank()) {

            return ResponseEntity
                    .badRequest()
                    .body("Payment method is required.");
        }


        // Create payment
        Payment payment =
                new Payment();


        payment.setAmount(
                contribution.getAmount()
        );


        payment.setMethod(
                request.getMethod()
        );


        // For now we mark the payment as
        // PENDING because we haven't connected
        // to a real mobile-money provider yet.
        payment.setStatus("PENDING");


        payment.setContribution(
                contribution
        );


        payment.setCreatedAt(
                LocalDateTime.now()
        );

        // Generate transaction reference

        String transactionReference =
        "CMS-" +
        LocalDateTime.now()
                .format(
                    DateTimeFormatter.ofPattern(
                        "yyyyMMddHHmmss"
                    )
                ) +
        "-" +
        System.currentTimeMillis() % 10000;

        payment.setTransactionReference(
                        transactionReference
        );


        Payment saved =
                paymentRepository.save(payment);


        // Response
        Map<String, Object> response =
                new HashMap<>();


        response.put(
                "paymentId",
                saved.getPaymentId()
        );

        response.put(
                "contributionId",
                contribution.getContributionId()
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
                "createdAt",
                saved.getCreatedAt()
        );

        response.put(
        "transactionReference",
        saved.getTransactionReference()
);


        return ResponseEntity.ok(response);
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