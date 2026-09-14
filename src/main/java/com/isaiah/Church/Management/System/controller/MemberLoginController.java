package com.isaiah.Church.Management.System.controller;

import com.isaiah.Church.Management.System.model.MemberAccount;
import com.isaiah.Church.Management.System.repository.MemberAccountRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/member-login")
public class MemberLoginController {

    private final MemberAccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public MemberLoginController(
            MemberAccountRepository accountRepository,
            BCryptPasswordEncoder passwordEncoder) {

        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request,
            HttpSession session) {

        MemberAccount account =
                accountRepository
                        .findByUsername(request.getUsername())
                        .orElse(null);

        // Username does not exist
        if (account == null) {

            return ResponseEntity
                    .badRequest()
                    .body("Invalid username or password.");
        }

        // Check account status
        if (!"ACTIVE".equalsIgnoreCase(account.getStatus())) {

            return ResponseEntity
                    .badRequest()
                    .body("Your account is not active.");
        }

        // Check password
        if (!passwordEncoder.matches(
                request.getPassword(),
                account.getPassword())) {

            return ResponseEntity
                    .badRequest()
                    .body("Invalid username or password.");
        }

        // Store member account in session
        session.setAttribute(
                "memberAccountId",
                account.getAccountId()
        );

        session.setAttribute(
                "memberId",
                account.getMember().getMemberId()
        );

        session.setAttribute(
                "memberUsername",
                account.getUsername()
        );

        // Response
        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "accountId",
                account.getAccountId()
        );

        response.put(
                "memberId",
                account.getMember().getMemberId()
        );

        response.put(
                "username",
                account.getUsername()
        );

        response.put(
                "firstName",
                account.getMember().getFirstName()
        );

        response.put(
                "lastName",
                account.getMember().getLastName()
        );

        response.put(
                "status",
                account.getStatus()
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpSession session) {

        session.invalidate();

        return ResponseEntity.ok(
                "Logged out successfully."
        );
    }


    // ==============================
    // Login Request
    // ==============================

    public static class LoginRequest {

        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}