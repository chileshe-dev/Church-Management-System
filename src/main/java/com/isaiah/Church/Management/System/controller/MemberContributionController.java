package com.isaiah.Church.Management.System.controller;

import com.isaiah.Church.Management.System.model.Contribution;
import com.isaiah.Church.Management.System.model.Member;
import com.isaiah.Church.Management.System.repository.ContributionRepository;
import com.isaiah.Church.Management.System.repository.MemberRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/member-contributions")
public class MemberContributionController {

    private final ContributionRepository contributionRepository;
    private final MemberRepository memberRepository;

    public MemberContributionController(
            ContributionRepository contributionRepository,
            MemberRepository memberRepository) {

        this.contributionRepository = contributionRepository;
        this.memberRepository = memberRepository;
    }


    // ==========================================
    // Make Contribution
    // ==========================================

    @PostMapping
    public ResponseEntity<?> makeContribution(
            @RequestBody ContributionRequest request,
            HttpSession session) {

        // Get logged-in member
        Integer memberId =
                (Integer) session.getAttribute("memberId");

        if (memberId == null) {

            return ResponseEntity
                    .status(401)
                    .body("Please login first.");
        }


        // Find member
        Member member =
                memberRepository
                        .findById(memberId)
                        .orElse(null);

        if (member == null) {

            return ResponseEntity
                    .badRequest()
                    .body("Member account not found.");
        }


        // Validate contribution type
        if (request.getContributionType() == null ||
                request.getContributionType().isBlank()) {

            return ResponseEntity
                    .badRequest()
                    .body("Contribution type is required.");
        }


        // Validate amount
        if (request.getAmount() == null ||
                request.getAmount() <= 0) {

            return ResponseEntity
                    .badRequest()
                    .body("Contribution amount must be greater than zero.");
        }


        // Validate date
        if (request.getContributionDate() == null) {

            return ResponseEntity
                    .badRequest()
                    .body("Contribution date is required.");
        }


        // Create contribution
        Contribution contribution =
                new Contribution();

        contribution.setMember(member);

        contribution.setContributionType(
                request.getContributionType()
        );

        contribution.setAmount(
                request.getAmount()
        );

        contribution.setContributionDate(
                request.getContributionDate()
        );


        Contribution saved =
                contributionRepository.save(contribution);


        return ResponseEntity.ok(saved);
    }


    // ==========================================
    // Get My Contributions
    // ==========================================

   @GetMapping
public ResponseEntity<?> getMyContributions(
        HttpSession session) {

    Integer memberId =
            (Integer) session.getAttribute("memberId");

    if (memberId == null) {

        return ResponseEntity
                .status(401)
                .body("Please login first.");
    }

    List<Contribution> contributions =
            contributionRepository
                    .findByMemberMemberId(memberId);

    double total = contributions.stream()
            .mapToDouble(Contribution::getAmount)
            .sum();

    Map<String, Object> response =
            new HashMap<>();

    response.put("contributions", contributions);
    response.put("total", total);

    return ResponseEntity.ok(response);
}


    // ==========================================
    // Contribution Request
    // ==========================================

    public static class ContributionRequest {

        private String contributionType;

        private Double amount;

        private LocalDate contributionDate;


        public String getContributionType() {
            return contributionType;
        }

        public void setContributionType(
                String contributionType) {

            this.contributionType =
                    contributionType;
        }


        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {

            this.amount = amount;
        }


        public LocalDate getContributionDate() {
            return contributionDate;
        }

        public void setContributionDate(
                LocalDate contributionDate) {

            this.contributionDate =
                    contributionDate;
        }
    }
}