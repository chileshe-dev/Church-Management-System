package com.isaiah.Church.Management.System.controller;

import com.isaiah.Church.Management.System.model.Ministry;
import com.isaiah.Church.Management.System.model.MinistryMembership;
import com.isaiah.Church.Management.System.repository.MinistryRepository;
import com.isaiah.Church.Management.System.service.MinistryMembershipService;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member-ministries")
public class MemberMinistryController {

    private final MinistryMembershipService membershipService;
    private final MinistryRepository ministryRepository;

    public MemberMinistryController(
            MinistryMembershipService membershipService,
            MinistryRepository ministryRepository) {

        this.membershipService = membershipService;
        this.ministryRepository = ministryRepository;
    }

    // Get all available ministries
    @GetMapping
    public ResponseEntity<?> getAllMinistries() {

        List<Ministry> ministries =
                ministryRepository.findAll();

        return ResponseEntity.ok(ministries);
    }

    // Get ministries joined by the logged-in member
    @GetMapping("/my")
    public ResponseEntity<?> getMyMinistries(
            HttpSession session) {

        Integer memberId =
                (Integer) session.getAttribute("memberId");

        if (memberId == null) {
            return ResponseEntity
                    .status(401)
                    .body("Please login first.");
        }

        return ResponseEntity.ok(
                membershipService.getMemberMinistries(memberId)
        );
    }

    // Join a ministry
    @PostMapping("/{ministryId}/join")
    public ResponseEntity<?> joinMinistry(
            @PathVariable Integer ministryId,
            HttpSession session) {

        Integer memberId =
                (Integer) session.getAttribute("memberId");

        if (memberId == null) {
            return ResponseEntity
                    .status(401)
                    .body("Please login first.");
        }

        try {

            MinistryMembership membership =
                    membershipService.joinMinistry(
                            memberId,
                            ministryId);

            return ResponseEntity.ok(membership);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // Leave a ministry
    @DeleteMapping("/{ministryId}/leave")
    public ResponseEntity<?> leaveMinistry(
            @PathVariable Integer ministryId,
            HttpSession session) {

        Integer memberId =
                (Integer) session.getAttribute("memberId");

        if (memberId == null) {
            return ResponseEntity
                    .status(401)
                    .body("Please login first.");
        }

        try {

            membershipService.leaveMinistry(
                    memberId,
                    ministryId);

            return ResponseEntity.ok(
                    "You have left the ministry.");

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}