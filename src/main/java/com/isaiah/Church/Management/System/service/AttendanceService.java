package com.isaiah.Church.Management.System.service;

import com.isaiah.Church.Management.System.model.Attendance;
import com.isaiah.Church.Management.System.model.Event;
import com.isaiah.Church.Management.System.repository.AttendanceRepository;
import com.isaiah.Church.Management.System.repository.EventRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository repository;
    private final EventRepository eventRepository;

    public AttendanceService(
            AttendanceRepository repository,
            EventRepository eventRepository) {

        this.repository = repository;
        this.eventRepository = eventRepository;
    }

    public Attendance saveAttendance(Attendance attendance) {

        if (attendance.getEvent() == null ||
                attendance.getEvent().getEventId() == null) {

            throw new IllegalArgumentException(
                    "An event must be selected.");
        }

        Event event = eventRepository
                .findById(attendance.getEvent().getEventId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "The selected event does not exist."));

        if (event.getEventDate() == null) {

            throw new IllegalArgumentException(
                    "The selected event has no date.");
        }

        if (event.getEventDate().isAfter(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Attendance cannot be recorded for a future event.");
        }

        attendance.setEvent(event);

        return repository.save(attendance);
    }

    public Attendance updateAttendance(
            Integer id,
            Attendance updatedAttendance) {

        Attendance attendance =
                repository.findById(id).orElse(null);

        if (attendance != null) {

            if (updatedAttendance.getEvent() == null ||
                    updatedAttendance.getEvent().getEventId() == null) {

                throw new IllegalArgumentException(
                        "An event must be selected.");
            }

            Event event = eventRepository
                    .findById(
                            updatedAttendance
                                    .getEvent()
                                    .getEventId())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "The selected event does not exist."));

            if (event.getEventDate() == null) {

                throw new IllegalArgumentException(
                        "The selected event has no date.");
            }

            if (event.getEventDate().isAfter(LocalDate.now())) {

                throw new IllegalArgumentException(
                        "Attendance cannot be recorded for a future event.");
            }

            attendance.setMember(
                    updatedAttendance.getMember());

            attendance.setEvent(event);

            attendance.setStatus(
                    updatedAttendance.getStatus());

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

    public List<Attendance> getMemberAttendance(Integer memberId) {
        return repository.findByMemberMemberId(memberId);
    }
}