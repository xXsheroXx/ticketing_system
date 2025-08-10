package com.example.shero.inventoryservice.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueInventoryResponse {
    private Long venueId;
    private String venueName;
    private Integer totalCapacity;
}
