package com.isaiah.Church.Management.System.controller;

import com.isaiah.Church.Management.System.model.Attendance;
import com.isaiah.Church.Management.System.service.AttendanceService;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member-attendance")
public class MemberAttendanceController {

    private final AttendanceService attendanceService;

    public MemberAttendanceController(
            AttendanceService attendanceService) {

        this.attendanceService = attendanceService;
    }

    @GetMapping
    public ResponseEntity<?> getMyAttendance(
            HttpSession session) {

        Integer memberId =
                (Integer) session.getAttribute("memberId");

        if (memberId == null) {
            return ResponseEntity
                    .status(401)
                    .body("Please login first.");
        }

        List<Attendance> attendance =
                attendanceService.getMemberAttendance(memberId);

        return ResponseEntity.ok(attendance);
    }
}