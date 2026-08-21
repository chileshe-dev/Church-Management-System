package com.isaiah.Church.Management.System.repository;

import com.isaiah.Church.Management.System.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository
        extends JpaRepository<Event, Integer> {
}