package com.smkngun.dealership.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// A Car is one vehicle listed on the lot.
// BigDecimal (not double) for price — floating-point can't represent money accurately.
@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String make;            // e.g., "Toyota"

    @Column(nullable = false, length = 60)
    private String model;           // e.g., "Camry"

    // "year" is a reserved word in some SQL dialects (H2, Postgres). Rename the column.
    @Column(name = "model_year", nullable = false)
    private Integer year;           // Model year, e.g., 2024.

    // precision=12 scale=2 = up to 9,999,999,999.99. Plenty for our needs.
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    private Integer mileage;        // Kilometres / miles on the odometer.

    @Column(length = 40)
    private String color;

    @Column(name = "body_type", length = 40)
    private String bodyType;        // Sedan, SUV, Truck...

    @Column(length = 30)
    private String transmission;    // Automatic / Manual.

    @Column(name = "fuel_type", length = 30)
    private String fuelType;        // Petrol, Diesel, Electric, Hybrid.

    @Column(name = "image_url", length = 500)
    private String imageUrl;        // Public URL to a stock photo.

    // columnDefinition="TEXT" tells the DB to use a large text column, not VARCHAR(255).
    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default                // Keep the default when using @Builder.
    private CarStatus status = CarStatus.AVAILABLE;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = CarStatus.AVAILABLE;
        }
    }
}
