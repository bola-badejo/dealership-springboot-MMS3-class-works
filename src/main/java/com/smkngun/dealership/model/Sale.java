package com.smkngun.dealership.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// A Sale records one transaction: this Customer bought this Car on this date.
@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @ManyToOne: many Sales can reference the same Car in theory, but in practice
    // our business rule marks the Car SOLD on purchase. unique=true on the FK
    // enforces "a car can only be sold once" at the database level.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false, unique = true)
    private Car car;

    // @ManyToOne on customer: a single customer can have MANY sales over time.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // The final agreed price. May differ from the car's sticker price.
    @Column(name = "sale_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "sale_date", nullable = false, updatable = false)
    private LocalDateTime saleDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @PrePersist
    void onCreate() {
        this.saleDate = LocalDateTime.now();
    }
}
