package com.isaiah.Church.Management.System.controller;

import com.isaiah.Church.Management.System.model.Event;
import com.isaiah.Church.Management.System.service.EventService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@PreAuthorize("hasAnyRole('ADMIN','PASTOR')")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping
    public Event addEvent(@RequestBody Event event) {
        return service.saveEvent(event);
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return service.getAllEvents();
    }

    @GetMapping("/{id}")
    public Event getEvent(@PathVariable Integer id) {
        return service.getEventById(id);
    }

    @PutMapping("/{id}")
    public Event updateEvent(
            @PathVariable Integer id,
            @RequestBody Event event) {

        return service.updateEvent(id, event);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Integer id) {
        service.deleteEvent(id);
    }
}