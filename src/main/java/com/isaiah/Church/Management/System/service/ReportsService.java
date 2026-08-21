package com.isaiah.Church.Management.System.service;

import com.isaiah.Church.Management.System.dto.AttendanceSummaryDTO;
import com.isaiah.Church.Management.System.dto.ContributionTypeDTO;
import com.isaiah.Church.Management.System.dto.MonthlyContributionDTO;
import com.isaiah.Church.Management.System.repository.AttendanceRepository;
import com.isaiah.Church.Management.System.repository.ContributionRepository;

import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportsService {

    private final ContributionRepository contributionRepository;
    private final AttendanceRepository attendanceRepository;

    public ReportsService(
            ContributionRepository contributionRepository,
            AttendanceRepository attendanceRepository) {

        this.contributionRepository = contributionRepository;
        this.attendanceRepository = attendanceRepository;
    }


    // ==========================================
    // Monthly Contributions
    // ==========================================

    public List<MonthlyContributionDTO> getMonthlyContributions() {

        List<Object[]> results =
                contributionRepository.getMonthlyContributions();

        List<MonthlyContributionDTO> list =
                new ArrayList<>();

        for (Object[] row : results) {

            int monthNumber =
                    ((Number) row[0]).intValue();

            double amount =
                    ((Number) row[1]).doubleValue();

            list.add(
                    new MonthlyContributionDTO(
                            Month.of(monthNumber).name(),
                            amount
                    )
            );
        }

        return list;
    }


    // ==========================================
    // Contribution Types
    // ==========================================

    public List<ContributionTypeDTO> getContributionTypes() {

        List<Object[]> results =
                contributionRepository.getContributionTypes();

        List<ContributionTypeDTO> list =
                new ArrayList<>();

        for (Object[] row : results) {

            String type =
                    row[0].toString();

            long count =
                    ((Number) row[1]).longValue();

            list.add(
                    new ContributionTypeDTO(
                            type,
                            count
                    )
            );
        }

        return list;
    }


    // ==========================================
    // Attendance Summary
    // ==========================================

    public List<AttendanceSummaryDTO> getAttendanceSummary() {

        List<Object[]> results =
                attendanceRepository.getAttendanceSummary();

        List<AttendanceSummaryDTO> list =
                new ArrayList<>();

        for (Object[] row : results) {

            String status =
                    row[0].toString();

            long total =
                    ((Number) row[1]).longValue();

            list.add(
                    new AttendanceSummaryDTO(
                            status,
                            total
                    )
            );
        }

        return list;
    }
}