package com.isaiah.Church.Management.System.repository;

import com.isaiah.Church.Management.System.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttendanceRepository
        extends JpaRepository<Attendance, Integer> {

    @Query("""
            SELECT a.status,
                   COUNT(a)
            FROM Attendance a
            GROUP BY a.status
            """)
    List<Object[]> getAttendanceSummary();

    // Get attendance records belonging to one member
    List<Attendance> findByMemberMemberId(Integer memberId);
}