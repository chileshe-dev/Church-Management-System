package com.isaiah.Church.Management.System.controller;

import com.isaiah.Church.Management.System.model.Attendance;
import com.isaiah.Church.Management.System.service.AttendanceService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
@PreAuthorize("hasAnyRole('ADMIN','PASTOR')")
public class AttendanceController {

    private final AttendanceService service;

    public AttendanceController(AttendanceService service) {
        this.service = service;
    }

    @PostMapping
    public Attendance addAttendance(
            @RequestBody Attendance attendance) {

        return service.saveAttendance(attendance);
    }

    @GetMapping
    public List<Attendance> getAllAttendance() {
        return service.getAllAttendance();
    }

    @GetMapping("/{id}")
    public Attendance getAttendance(
            @PathVariable Integer id) {

        return service.getAttendanceById(id);
    }


    @PutMapping("/{id}")
public Attendance updateAttendance(
        @PathVariable Integer id,
        @RequestBody Attendance attendance) {

    return service.updateAttendance(id, attendance);

}

    @DeleteMapping("/{id}")
    public void deleteAttendance(
            @PathVariable Integer id) {

        service.deleteAttendance(id);
    }
}