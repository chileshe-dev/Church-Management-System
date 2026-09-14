package com.isaiah.Church.Management.System.controller;

import com.isaiah.Church.Management.System.model.Event;
import com.isaiah.Church.Management.System.service.EventService;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member-events")
public class MemberEventController {

    private final EventService eventService;

    public MemberEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<?> getUpcomingEvents(
            HttpSession session) {

        // Check if a member is logged in
        Integer memberId =
                (Integer) session.getAttribute("memberId");

        if (memberId == null) {
            return ResponseEntity
                    .status(401)
                    .body("Please login first.");
        }

        List<Event> events =
                eventService.getUpcomingEvents();

        return ResponseEntity.ok(events);
    }
}