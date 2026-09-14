package com.isaiah.Church.Management.System.controller;

import com.isaiah.Church.Management.System.model.MemberAccount;
import com.isaiah.Church.Management.System.repository.MemberAccountRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/member-account")
public class MemberAccountController {

    private final MemberAccountRepository repository;


    public MemberAccountController(
            MemberAccountRepository repository) {

        this.repository = repository;
    }


    @GetMapping("/me")
    public ResponseEntity<?> getCurrentMember(
            HttpSession session) {


        Integer accountId =
                (Integer) session.getAttribute(
                        "memberAccountId"
                );


        // Not logged in
        if (accountId == null) {

            return ResponseEntity
                    .status(401)
                    .body("Not logged in.");
        }


        MemberAccount account =
                repository.findById(accountId)
                        .orElse(null);


        if (account == null) {

            return ResponseEntity
                    .status(401)
                    .body("Member account not found.");
        }


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
                "email",
                account.getMember().getEmail()
        );

        response.put(
                "phone",
                account.getMember().getPhone()
        );

        response.put(
                "address",
                account.getMember().getAddress()
        );

        response.put(
                "status",
                account.getStatus()
        );


        return ResponseEntity.ok(response);
    }
}