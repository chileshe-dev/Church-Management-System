package com.isaiah.Church.Management.System.controller;

import com.isaiah.Church.Management.System.dto.AttendanceSummaryDTO;
import com.isaiah.Church.Management.System.dto.ContributionTypeDTO;
import com.isaiah.Church.Management.System.dto.MonthlyContributionDTO;
import com.isaiah.Church.Management.System.service.ReportExportService;
import com.isaiah.Church.Management.System.service.ReportsService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/reports")
@PreAuthorize("hasAnyRole('ADMIN','PASTOR','TREASURER')")
public class ReportsController {

    private final ReportsService service;

    private final ReportExportService exportService;


    public ReportsController(
            ReportsService service,
            ReportExportService exportService) {

        this.service = service;

        this.exportService =
                exportService;
    }


    // =====================================================
    // MONTHLY CONTRIBUTIONS
    // =====================================================

    @GetMapping("/monthly-contributions")
    public List<MonthlyContributionDTO>
    monthlyContributions() {

        return service.getMonthlyContributions();
    }


    // =====================================================
    // CONTRIBUTION TYPES
    // =====================================================

    @GetMapping("/contribution-types")
    public List<ContributionTypeDTO>
    contributionTypes() {

        return service.getContributionTypes();
    }


    // =====================================================
    // ATTENDANCE SUMMARY
    // =====================================================

    @GetMapping("/attendance-summary")
    public List<AttendanceSummaryDTO>
    attendanceSummary() {

        return service.getAttendanceSummary();
    }


    // =====================================================
    // PDF EXPORT
    // =====================================================

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPDF()
            throws Exception {

        byte[] pdf =
                exportService.generatePDF();


        return ResponseEntity.ok()

                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=church-report.pdf"
                )

                .contentType(
                        MediaType.APPLICATION_PDF
                )

                .body(pdf);
    }


    // =====================================================
    // EXCEL EXPORT
    // =====================================================

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportExcel()
            throws Exception {

        byte[] excel =
                exportService.generateExcel();


        return ResponseEntity.ok()

                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=church-report.xlsx"
                )

                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )

                .body(excel);
    }
}