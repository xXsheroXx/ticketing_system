package com.shero.bookingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "customer")
public class Customer {
    @Id
    private Long id;
    private String name;
    private String email;
    private String address;
}
