package com.example.shero.inventoryservice.service;

import com.example.shero.inventoryservice.entity.Event;
import com.example.shero.inventoryservice.entity.Venue;
import com.example.shero.inventoryservice.repository.EventRepository;
import com.example.shero.inventoryservice.repository.VenueRepository;
import com.example.shero.inventoryservice.response.AllEventsResponse;
import com.example.shero.inventoryservice.response.EventInventoryResponse;
import com.example.shero.inventoryservice.response.VenueInventoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    @Autowired
    public InventoryService(final EventRepository eventRepository, final VenueRepository venueRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
    }

    public List<AllEventsResponse> getAllEvents() {
        final List<Event> events = eventRepository.findAll();

        return events.stream()
                .map(event -> AllEventsResponse.builder()
                        .event(event.getName())
                        .capacity(event.getLeftCapacity())
                        .venue(event.getVenue())
                        .build()).collect(Collectors.toList());
    }

    public VenueInventoryResponse getVenueInformation(Long venueId) {
        final Venue venue = venueRepository.findById(venueId).orElse(null);

        return VenueInventoryResponse.builder()
                .venueId(venue.getId())
                .venueName(venue.getName())
                .totalCapacity(venue.getTotalCapacity())
                .build();
    }

    public EventInventoryResponse getEventInventory(Long eventId) {
        final Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));

        return EventInventoryResponse.builder()
                .event(event.getName())
                .capacity(event.getLeftCapacity())
                .venue(event.getVenue())
                .ticketPrice(event.getTicketPrice())
                .eventId(event.getId())
                .build();
    }
}
