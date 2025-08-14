package com.shero.bookingservice.service;

import com.shero.bookingservice.client.InventoryServiceClient;
import com.shero.bookingservice.entity.Customer;
import com.shero.bookingservice.event.BookingEvent;
import com.shero.bookingservice.repository.CustomerRepository;
import com.shero.bookingservice.request.BookingRequest;
import com.shero.bookingservice.response.BookingResponse;
import com.shero.bookingservice.response.InventoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class BookingService {

    private final CustomerRepository customerRepository;
    private final InventoryServiceClient inventoryServiceClient;
    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    @Autowired
    public BookingService(final CustomerRepository customerRepository,
                          final InventoryServiceClient inventoryServiceClient, final KafkaTemplate<String, BookingEvent> kafkaTemplate) {
        this.customerRepository = customerRepository;
        this.inventoryServiceClient = inventoryServiceClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public BookingResponse createBooking(final BookingRequest request) {
        // Check if the user exists
        final Customer customer = customerRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Check if there is enough inventory
        final InventoryResponse inventoryResponse = inventoryServiceClient.getInventory(request.getEventId());
        System.out.println("Inventory Service Response: " + inventoryResponse);
        if (inventoryResponse == null || inventoryResponse.getCapacity() < request.getTicketCount()) {
            throw new RuntimeException("Not enough tickets available");
        }

        // create booking
        final BookingEvent bookingEvent = createBookingEvent(request, customer, inventoryResponse);

        // Publish the booking event to Kafka
        kafkaTemplate.send("booking", bookingEvent);
        log.info("Booking event published: {}", bookingEvent);
        return BookingResponse.builder()
                .userId(bookingEvent.getUserId())
                .eventId(bookingEvent.getEventId())
                .ticketCount(bookingEvent.getTicketCount())
                .totalPrice(bookingEvent.getTotalPrice())
                .build();
    }

    private BookingEvent createBookingEvent(final BookingRequest request,
                                          final Customer customer,
                                          final InventoryResponse inventoryResponse) {
        // Here you would typically save the booking to a database and publish an event
        // For simplicity, we are just returning a new BookingEvent instance
        return BookingEvent.builder()
                .userId(customer.getId())
                .eventId(request.getEventId())
                .ticketCount(request.getTicketCount())
                .totalPrice(inventoryResponse.getTicketPrice().multiply(BigDecimal.valueOf(request.getTicketCount())))
                .build();
    }
}
