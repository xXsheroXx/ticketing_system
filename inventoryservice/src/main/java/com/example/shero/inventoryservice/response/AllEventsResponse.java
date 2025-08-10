package com.example.shero.inventoryservice.response;

import com.example.shero.inventoryservice.entity.Venue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllEventsResponse {
    private String event;
    private Long capacity;
    private Venue venue;
}
