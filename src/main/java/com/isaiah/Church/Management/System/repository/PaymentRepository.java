package com.isaiah.Church.Management.System.repository;

import com.isaiah.Church.Management.System.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository
        extends JpaRepository<Payment, Integer> {

    Optional<Payment> findByContributionContributionId(
            Integer contributionId
    );
}