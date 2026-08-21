package com.isaiah.Church.Management.System.repository;

import com.isaiah.Church.Management.System.model.Ministry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MinistryRepository
        extends JpaRepository<Ministry, Integer> {
}