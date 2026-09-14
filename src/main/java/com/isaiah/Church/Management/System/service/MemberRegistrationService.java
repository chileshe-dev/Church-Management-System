package com.isaiah.Church.Management.System.service;

import com.isaiah.Church.Management.System.model.Member;
import com.isaiah.Church.Management.System.model.MemberAccount;
import com.isaiah.Church.Management.System.repository.MemberAccountRepository;
import com.isaiah.Church.Management.System.repository.MemberRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberRegistrationService {

    private final MemberRepository memberRepository;
    private final MemberAccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public MemberRegistrationService(
            MemberRepository memberRepository,
            MemberAccountRepository accountRepository,
            BCryptPasswordEncoder passwordEncoder) {

        this.memberRepository = memberRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MemberAccount register(
            String firstName,
            String lastName,
            String email,
            String phone,
            String address,
            String username,
            String password) {

        // Check if username already exists
        if (accountRepository.existsByUsername(username)) {
            throw new RuntimeException(
                    "Username already exists"
            );
        }

        // Create member
        Member member = new Member();

        member.setFirstName(firstName);
        member.setLastName(lastName);
        member.setEmail(email);
        member.setPhone(phone);
        member.setAddress(address);

        member = memberRepository.save(member);

        // Create member account
        MemberAccount account = new MemberAccount();

        account.setMember(member);
        account.setUsername(username);

        // IMPORTANT:
        // Store encrypted password
        account.setPassword(
                passwordEncoder.encode(password)
        );

        account.setStatus("ACTIVE");

        return accountRepository.save(account);
    }
}