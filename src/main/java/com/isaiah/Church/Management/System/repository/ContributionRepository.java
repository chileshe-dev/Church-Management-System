package com.isaiah.Church.Management.System.repository;

import com.isaiah.Church.Management.System.model.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContributionRepository
        extends JpaRepository<Contribution, Integer> {
}