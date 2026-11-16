package com.smkngun.dealership.dto;

import com.smkngun.dealership.model.CarStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Read-side DTO: what we send back to the view (or to a JSON client).
// Keeps the Hibernate entity out of our templates/JSON responses.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {
    private Long id;
    private String make;
    private String model;
    private Integer year;
    private BigDecimal price;
    private Integer mileage;
    private String color;
    private String bodyType;
    private String transmission;
    private String fuelType;
    private String imageUrl;
    private String description;
    private CarStatus status;
    private LocalDateTime createdAt;
}
