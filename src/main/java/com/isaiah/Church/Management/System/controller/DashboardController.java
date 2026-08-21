package com.isaiah.Church.Management.System.controller;

import com.isaiah.Church.Management.System.dto.DashboardResponse;
import com.isaiah.Church.Management.System.repository.*;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@PreAuthorize("isAuthenticated()")
public class DashboardController {

    private final MemberRepository memberRepository;
    private final ContributionRepository contributionRepository;
    private final EventRepository eventRepository;
    private final AttendanceRepository attendanceRepository;
    private final MinistryRepository ministryRepository;
    private final UserRepository userRepository;

    public DashboardController(
            MemberRepository memberRepository,
            ContributionRepository contributionRepository,
            EventRepository eventRepository,
            AttendanceRepository attendanceRepository,
            MinistryRepository ministryRepository,
            UserRepository userRepository) {

        this.memberRepository = memberRepository;
        this.contributionRepository = contributionRepository;
        this.eventRepository = eventRepository;
        this.attendanceRepository = attendanceRepository;
        this.ministryRepository = ministryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public DashboardResponse getDashboard() {

        return new DashboardResponse(
                memberRepository.count(),
                contributionRepository.count(),
                eventRepository.count(),
                attendanceRepository.count(),
                ministryRepository.count(),
                userRepository.count()
        );
    }
}