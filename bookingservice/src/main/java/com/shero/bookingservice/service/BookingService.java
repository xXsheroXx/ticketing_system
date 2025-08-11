package com.shero.bookingservice.service;

import com.shero.bookingservice.client.InventoryServiceClient;
import com.shero.bookingservice.entity.Customer;
import com.shero.bookingservice.repository.CustomerRepository;
import com.shero.bookingservice.request.BookingRequest;
import com.shero.bookingservice.response.BookingResponse;
import com.shero.bookingservice.response.InventoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final CustomerRepository customerRepository;
    private final InventoryServiceClient inventoryServiceClient;

    @Autowired
    public BookingService(final CustomerRepository customerRepository,
                          final InventoryServiceClient inventoryServiceClient) {
        this.customerRepository = customerRepository;
        this.inventoryServiceClient = inventoryServiceClient;
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

        return BookingResponse.builder().build();
    }
}
