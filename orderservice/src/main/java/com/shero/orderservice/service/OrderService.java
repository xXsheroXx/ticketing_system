package com.shero.orderservice.service;

import com.shero.bookingservice.event.BookingEvent;
import com.shero.orderservice.client.InventoryServiceClient;
import com.shero.orderservice.entity.Order;
import com.shero.orderservice.repository.orderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

    private final orderRepository orderRepository;
    private final InventoryServiceClient inventoryServiceClient;

    @Autowired
    public OrderService(orderRepository orderRepository, InventoryServiceClient inventoryServiceClient) {
        this.orderRepository = orderRepository;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    @KafkaListener(topics = "booking", groupId = "order-service")
    public void orderEvent(BookingEvent bookingEvent) {
        log.info("Received order event: {}", bookingEvent);

        // Create order object for DB
        Order order = createOrder(bookingEvent);
        orderRepository.saveAndFlush(order);

        // update inventory
        inventoryServiceClient.updateInventory(order.getEventId(), order.getTicketCount());
        log.info("Order processed and inventory updated for event ID: {}", order.getEventId());
    }

    private Order createOrder(BookingEvent bookingEvent) {
        return Order.builder()
                .costumerId(bookingEvent.getUserId())
                .eventId(bookingEvent.getEventId())
                .ticketCount(bookingEvent.getTicketCount())
                .totalPrice(bookingEvent.getTotalPrice())
                .build();
    }
}
