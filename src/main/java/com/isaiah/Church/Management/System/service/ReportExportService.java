package com.isaiah.Church.Management.System.service;

import com.isaiah.Church.Management.System.model.Attendance;
import com.isaiah.Church.Management.System.model.Contribution;
import com.isaiah.Church.Management.System.repository.AttendanceRepository;
import com.isaiah.Church.Management.System.repository.ContributionRepository;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ReportExportService {

    private final ContributionRepository contributionRepository;
    private final AttendanceRepository attendanceRepository;

    public ReportExportService(
            ContributionRepository contributionRepository,
            AttendanceRepository attendanceRepository) {

        this.contributionRepository = contributionRepository;
        this.attendanceRepository = attendanceRepository;
    }


    // =========================================================
    // PDF EXPORT
    // =========================================================

    public byte[] generatePDF() throws Exception {

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        Document document =
                new Document(PageSize.A4);

        PdfWriter.getInstance(
                document,
                outputStream
        );

        document.open();


        // -----------------------------------------------------
        // TITLE
        // -----------------------------------------------------

        Font titleFont =
                new Font(
                        Font.HELVETICA,
                        20,
                        Font.BOLD
                );

        Paragraph title =
                new Paragraph(
                        "Church Management System",
                        titleFont
                );

        title.setAlignment(Element.ALIGN_CENTER);

        document.add(title);


        Font subtitleFont =
                new Font(
                        Font.HELVETICA,
                        14,
                        Font.BOLD
                );

        Paragraph subtitle =
                new Paragraph(
                        "Church Reports",
                        subtitleFont
                );

        subtitle.setAlignment(Element.ALIGN_CENTER);

        document.add(subtitle);

        document.add(
                new Paragraph(" ")
        );


        // -----------------------------------------------------
        // CONTRIBUTION SUMMARY
        // -----------------------------------------------------

        document.add(
                new Paragraph(
                        "Contribution Summary",
                        subtitleFont
                )
        );

        Double total =
                contributionRepository
                        .getTotalContributionAmount();

        if (total == null) {
            total = 0.0;
        }

        document.add(
                new Paragraph(
                        String.format(
                                "Total Contributions: K %.2f",
                                total
                        )
                )
        );

        document.add(
                new Paragraph(" ")
        );


        // -----------------------------------------------------
        // CONTRIBUTIONS TABLE
        // -----------------------------------------------------

        List<Contribution> contributions =
                contributionRepository.findAll();

        PdfPTable contributionTable =
                new PdfPTable(5);

        contributionTable.setWidthPercentage(100);

        contributionTable.addCell("ID");
        contributionTable.addCell("Member");
        contributionTable.addCell("Type");
        contributionTable.addCell("Amount");
        contributionTable.addCell("Date");


        for (Contribution contribution : contributions) {

            contributionTable.addCell(
                    String.valueOf(
                            contribution.getContributionId()
                    )
            );

            String memberName = "";

            if (contribution.getMember() != null) {

                memberName =
                        contribution.getMember().getFirstName()
                        + " "
                        + contribution.getMember().getLastName();
            }

            contributionTable.addCell(
                    memberName
            );

            contributionTable.addCell(
                    contribution.getContributionType()
            );

            Double amount =
                    contribution.getAmount();

            if (amount == null) {
                amount = 0.0;
            }

            contributionTable.addCell(
                    String.format(
                            "K %.2f",
                            amount
                    )
            );

            contributionTable.addCell(
                    String.valueOf(
                            contribution.getContributionDate()
                    )
            );
        }

        document.add(
                contributionTable
        );

        document.add(
                new Paragraph(" ")
        );


        // -----------------------------------------------------
        // ATTENDANCE SUMMARY
        // -----------------------------------------------------

        document.add(
                new Paragraph(
                        "Attendance Summary",
                        subtitleFont
                )
        );

        document.add(
                new Paragraph(" ")
        );

        List<Object[]> attendanceResults =
                attendanceRepository
                        .getAttendanceSummary();

        PdfPTable attendanceTable =
                new PdfPTable(2);

        attendanceTable.setWidthPercentage(50);

        attendanceTable.addCell("Status");
        attendanceTable.addCell("Total");


        for (Object[] row : attendanceResults) {

            attendanceTable.addCell(
                    String.valueOf(row[0])
            );

            attendanceTable.addCell(
                    String.valueOf(row[1])
            );
        }

        document.add(
                attendanceTable
        );

        document.add(
                new Paragraph(" ")
        );


        // -----------------------------------------------------
        // FOOTER
        // -----------------------------------------------------

        Paragraph footer =
                new Paragraph(
                        "Generated by Church Management System"
                );

        footer.setAlignment(
                Element.ALIGN_CENTER
        );

        document.add(
                footer
        );

        document.close();

        return outputStream.toByteArray();
    }


    // =========================================================
    // EXCEL EXPORT
    // =========================================================

    public byte[] generateExcel() throws Exception {

        Workbook workbook =
                new XSSFWorkbook();


        // =====================================================
        // CONTRIBUTIONS SHEET
        // =====================================================

        Sheet contributionSheet =
                workbook.createSheet(
                        "Contributions"
                );


        // Header style
        CellStyle headerStyle =
                workbook.createCellStyle();

        org.apache.poi.ss.usermodel.Font headerFont =
        workbook.createFont();

        headerFont.setBold(true);

        headerStyle.setFont(
                headerFont
        );


        // Title
        Row titleRow =
                contributionSheet.createRow(0);

        Cell titleCell =
                titleRow.createCell(0);

        titleCell.setCellValue(
                "Church Management System - Contributions"
        );

        titleCell.setCellStyle(
                headerStyle
        );


        // Headers
        Row headerRow =
                contributionSheet.createRow(2);

        String[] headers = {
                "ID",
                "Member",
                "Type",
                "Amount",
                "Date"
        };


        for (int i = 0; i < headers.length; i++) {

            Cell cell =
                    headerRow.createCell(i);

            cell.setCellValue(
                    headers[i]
            );

            cell.setCellStyle(
                    headerStyle
            );
        }


        // Contribution data
        List<Contribution> contributions =
                contributionRepository.findAll();


        int rowNumber = 3;

        for (Contribution contribution : contributions) {

            Row row =
                    contributionSheet.createRow(
                            rowNumber++
                    );


            row.createCell(0)
                    .setCellValue(
                            contribution
                                    .getContributionId()
                    );


            String memberName = "";

            if (contribution.getMember() != null) {

                memberName =
                        contribution.getMember()
                                .getFirstName()
                        + " "
                        + contribution.getMember()
                                .getLastName();
            }

            row.createCell(1)
                    .setCellValue(
                            memberName
                    );


            row.createCell(2)
                    .setCellValue(
                            contribution
                                    .getContributionType()
                    );


            Double amount =
                    contribution.getAmount();

            if (amount == null) {
                amount = 0.0;
            }

            row.createCell(3)
                    .setCellValue(
                            amount
                    );


            row.createCell(4)
                    .setCellValue(
                            String.valueOf(
                                    contribution
                                            .getContributionDate()
                            )
                    );
        }


        // Auto-size columns
        for (int i = 0; i < 5; i++) {

            contributionSheet
                    .autoSizeColumn(i);
        }


        // =====================================================
        // ATTENDANCE SHEET
        // =====================================================

        Sheet attendanceSheet =
                workbook.createSheet(
                        "Attendance Summary"
                );


        Row attendanceTitle =
                attendanceSheet.createRow(0);

        Cell attendanceTitleCell =
                attendanceTitle.createCell(0);

        attendanceTitleCell.setCellValue(
                "Attendance Summary"
        );

        attendanceTitleCell.setCellStyle(
                headerStyle
        );


        Row attendanceHeader =
                attendanceSheet.createRow(2);

        attendanceHeader
                .createCell(0)
                .setCellValue("Status");

        attendanceHeader
                .createCell(1)
                .setCellValue("Total");


        attendanceHeader
                .getCell(0)
                .setCellStyle(headerStyle);

        attendanceHeader
                .getCell(1)
                .setCellStyle(headerStyle);


        List<Object[]> attendanceResults =
                attendanceRepository
                        .getAttendanceSummary();


        int attendanceRowNumber = 3;


        for (Object[] result :
                attendanceResults) {

            Row row =
                    attendanceSheet.createRow(
                            attendanceRowNumber++
                    );

            row.createCell(0)
                    .setCellValue(
                            String.valueOf(
                                    result[0]
                            )
                    );

            row.createCell(1)
                    .setCellValue(
                            ((Number) result[1])
                                    .longValue()
                    );
        }


        attendanceSheet.autoSizeColumn(0);
        attendanceSheet.autoSizeColumn(1);


        // =====================================================
        // CREATE EXCEL FILE
        // =====================================================

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        workbook.write(
                outputStream
        );

        workbook.close();

        return outputStream.toByteArray();
    }
}