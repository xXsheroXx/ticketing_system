package com.example.shero.inventoryservice.controller;

import com.example.shero.inventoryservice.response.AllEventsResponse;
import com.example.shero.inventoryservice.response.EventInventoryResponse;
import com.example.shero.inventoryservice.response.VenueInventoryResponse;
import com.example.shero.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/inventory/events")
    public @ResponseBody List<AllEventsResponse> getInventoryEvents() {
        return inventoryService.getAllEvents();
    }

    @GetMapping("/inventory/venue/{venueId}")
    public @ResponseBody VenueInventoryResponse inventoryByVenueId(@PathVariable("venueId") Long venueId) {
        return inventoryService.getVenueInformation(venueId);
    }

    @GetMapping("/inventory/event/{eventId}")
    public @ResponseBody EventInventoryResponse inventoryByEventId(@PathVariable("eventId") Long eventId) {
        return inventoryService.getEventInventory(eventId);
    }

    @PutMapping("/inventory/event/{eventId}/capacity/{capacity}")
    public ResponseEntity<Void> updateEventCapacity(@PathVariable("eventId") Long eventId,
                                                    @PathVariable("capacity") Long ticketsBooked) {
        inventoryService.updateEventCapacity(eventId, ticketsBooked);
        return ResponseEntity.ok().build();

    }
}
