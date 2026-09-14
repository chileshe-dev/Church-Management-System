package com.isaiah.Church.Management.System.repository;

import com.isaiah.Church.Management.System.model.MemberAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberAccountRepository
        extends JpaRepository<MemberAccount, Integer> {

    Optional<MemberAccount> findByUsername(String username);

    boolean existsByUsername(String username);
}