package com.isaiah.Church.Management.System.service;

import com.isaiah.Church.Management.System.model.Attendance;
import com.isaiah.Church.Management.System.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository repository;

    public AttendanceService(AttendanceRepository repository) {
        this.repository = repository;
    }

    public Attendance saveAttendance(Attendance attendance) {
        return repository.save(attendance);
    }

    public Attendance updateAttendance(
        Integer id,
        Attendance updatedAttendance) {

    Attendance attendance =
            repository.findById(id).orElse(null);

    if (attendance != null) {

        attendance.setMember(updatedAttendance.getMember());

        attendance.setEvent(updatedAttendance.getEvent());

        attendance.setStatus(updatedAttendance.getStatus());

        return repository.save(attendance);

    }

    return null;

}

    public List<Attendance> getAllAttendance() {
        return repository.findAll();
    }

    public Attendance getAttendanceById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteAttendance(Integer id) {
        repository.deleteById(id);
    }
}