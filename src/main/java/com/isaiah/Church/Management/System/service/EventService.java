package com.isaiah.Church.Management.System.service;

import com.isaiah.Church.Management.System.model.Event;
import com.isaiah.Church.Management.System.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public Event saveEvent(Event event) {
        return repository.save(event);
    }

    public List<Event> getAllEvents() {
        return repository.findAll();
    }

    public Event getEventById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteEvent(Integer id) {
        repository.deleteById(id);
    }

    public Event updateEvent(Integer id, Event updatedEvent) {

        Event event = getEventById(id);

        if (event != null) {
            event.setEventName(updatedEvent.getEventName());
            event.setEventDate(updatedEvent.getEventDate());
            event.setLocation(updatedEvent.getLocation());
            event.setDescription(updatedEvent.getDescription());

            return repository.save(event);
        }

        return null;
    }
      // Get events that are happening today or in the future
    public List<Event> getUpcomingEvents() {
        return repository
                .findByEventDateGreaterThanEqualOrderByEventDateAsc(
                        LocalDate.now());
    }
}