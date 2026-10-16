
package com.isaiah.Church.Management.System.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.isaiah.Church.Management.System.model.Contribution;
import com.isaiah.Church.Management.System.model.Payment;
import com.isaiah.Church.Management.System.repository.ContributionRepository;
import com.isaiah.Church.Management.System.repository.PaymentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ContributionRepository contributionRepository;
    private final MtnMomoService mtnMomoService;

    public PaymentService(
            PaymentRepository paymentRepository,
            ContributionRepository contributionRepository,
            MtnMomoService mtnMomoService) {

        this.paymentRepository = paymentRepository;
        this.contributionRepository = contributionRepository;
        this.mtnMomoService = mtnMomoService;
    }

    @Transactional
    public Payment createPayment(
            Integer contributionId,
            String method,
            Integer memberId) {

        Contribution contribution =
                contributionRepository.findById(contributionId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Contribution not found."));

        if (contribution.getMember() == null ||
                !contribution.getMember()
                        .getMemberId().equals(memberId)) {
            throw new IllegalArgumentException(
                    "You cannot pay for this contribution.");
        }

        Optional<Payment> existingPayment =
                paymentRepository
                        .findByContributionContributionId(
                                contributionId);

        if (existingPayment.isPresent()) {
            throw new IllegalArgumentException(
                    "A payment record already exists for this contribution.");
        }

        if (method == null ||
                !method.equalsIgnoreCase("Mobile Money")) {
            throw new IllegalArgumentException(
                    "For this test, the payment method must be Mobile Money.");
        }

        Payment payment = new Payment();
        payment.setAmount(contribution.getAmount());
        payment.setMethod("Mobile Money");
        payment.setStatus("PENDING");
        payment.setContribution(contribution);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setProvider("MTN");

        // This is a test-only sandbox integration.
        // Never use a real member's number for this test.
        String sandboxPhoneNumber = "46733123470";

        payment.setPhoneNumber(sandboxPhoneNumber);

        String transactionReference =
                "CMS-" +
                LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern(
                                "yyyyMMddHHmmss"))
                + "-" +
                System.currentTimeMillis() % 10000;

        String mtnReference =
                mtnMomoService.generateReferenceId();

        payment.setTransactionReference(
                transactionReference);
        payment.setProviderReference(mtnReference);

        // Save the record before calling MTN.
        payment = paymentRepository.saveAndFlush(payment);

        try {
            // Sandbox test only: MTN expects EUR.
            // Do not send a ZMW contribution amount as EUR.
            MtnMomoService.PaymentRequestResult result =
                    mtnMomoService.requestPayment(
                            "100",
                            sandboxPhoneNumber,
                            transactionReference,
                            mtnReference);

            if (result.getHttpStatus() != 202) {
                payment.setStatus("FAILED");
            }

            payment.setUpdatedAt(LocalDateTime.now());
            return paymentRepository.save(payment);

        } catch (Exception e) {
            // Keep PENDING if the request outcome is uncertain:
            // MTN may have received the request despite a network error.
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);

            throw new IllegalStateException(
                    "Could not confirm MTN request acceptance. "
                    + "Check the payment status before retrying.");
        }
    }

    @Transactional
    public Payment refreshPaymentStatus(Integer paymentId) {

        Payment payment =
                paymentRepository.findById(paymentId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Payment not found."));

        if (!"MTN".equalsIgnoreCase(payment.getProvider())) {
            throw new IllegalArgumentException(
                    "This payment is not an MTN payment.");
        }

        if (payment.getProviderReference() == null ||
                payment.getProviderReference().isBlank()) {
            throw new IllegalArgumentException(
                    "MTN payment reference is missing.");
        }

        JsonNode result =
                mtnMomoService.getPaymentStatus(
                        payment.getProviderReference());

        if (result == null || result.get("status") == null) {
            throw new IllegalStateException(
                    "MTN did not return a payment status.");
        }

        String mtnStatus =
                result.get("status").asText();

        if ("SUCCESSFUL".equalsIgnoreCase(mtnStatus)) {
            payment.setStatus("COMPLETED");
        } else if ("FAILED".equalsIgnoreCase(mtnStatus)) {
            payment.setStatus("FAILED");
        } else {
            payment.setStatus("PENDING");
        }

        if (result.hasNonNull("financialTransactionId")) {
            payment.setProviderReference(
                    payment.getProviderReference());
        }

        payment.setUpdatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    public Payment getPaymentById(Integer paymentId) {
        return paymentRepository.findById(paymentId)
                .orElse(null);
    }
}
