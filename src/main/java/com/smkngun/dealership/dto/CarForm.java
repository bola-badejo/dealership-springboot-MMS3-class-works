package com.smkngun.dealership.dto;

// Bean Validation annotations: Spring runs these BEFORE our controller method.
// If any fail, we get a BindingResult full of errors instead of bad data in the DB.
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// A "Form" DTO (Data Transfer Object) is what the HTML form posts back to us.
// We keep it separate from the Car entity so that bad or extra form fields
// can never sneak straight into the database.
@Data                       // Lombok: getters + setters + equals + hashCode + toString.
@NoArgsConstructor
public class CarForm {

    private Long id;        // Null when creating; populated when editing.

    @NotBlank(message = "Make is required")           // Must be non-null AND non-empty.
    @Size(max = 60)
    private String make;

    @NotBlank(message = "Model is required")
    @Size(max = 60)
    private String model;

    @NotNull(message = "Year is required")
    @Min(value = 1900, message = "Year is too old")
    @Max(value = 2100, message = "Year is too far in the future")
    private Integer year;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.00", inclusive = false, message = "Price must be positive")
    private BigDecimal price;

    @PositiveOrZero(message = "Mileage cannot be negative")
    private Integer mileage;

    @Size(max = 40) private String color;
    @Size(max = 40) private String bodyType;
    @Size(max = 30) private String transmission;
    @Size(max = 30) private String fuelType;
    @Size(max = 500) private String imageUrl;

    private String description;
}
