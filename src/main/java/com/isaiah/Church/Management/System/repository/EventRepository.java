package com.isaiah.Church.Management.System.repository;

import com.isaiah.Church.Management.System.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository
        extends JpaRepository<Event, Integer> {

    List<Event> findByEventDateGreaterThanEqualOrderByEventDateAsc(
            LocalDate date);
}